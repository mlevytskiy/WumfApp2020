package com.core.dynamicfeature.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.api.api.CheckRegistrationRequest
import com.app.api.api.LoginRequest
import com.app.api.api.RegistrationRequest
import com.app.api.api.WumfApi
import com.core.dynamicfeature.fragment.EnterPhoneNumberFragmentDirections
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.viewmodel.AnyFragmentBaseViewModel
import com.core.wumfapp2020.viewmodel.ResultStatus
import com.core.wumfapp2020.viewmodel.SharedViewModel
import com.library.Event
import com.library.core.SingleLiveEvent
import com.library.postEvent
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.*
import krafts.alex.tg.*
import krafts.alex.tg.EnterCode
import krafts.alex.tg.EnterPassword
import krafts.alex.tg.EnterPhone
import org.drinkless.td.libcore.telegram.TdApi
import retrofit2.await

class EnterPhoneNumberViewModel2 @AssistedInject constructor(private val sharedViewModel: SharedViewModel, private val client: TgClient,
                                                             private val userInfoRepository: UserInfoRepository,
                                                             private var wumfApi: WumfApi
): AnyFragmentBaseViewModel() {

    private val directions = EnterPhoneNumberFragmentDirections.Companion

    @AssistedInject.Factory
    interface Factory {
        fun create(): EnterPhoneNumberViewModel2
    }

    private val showNextButtonInProgressStateMutable = SingleLiveEvent<Event<Unit>>()
    val showNextButtonInProgressState: LiveData<Event<Unit>> = showNextButtonInProgressStateMutable

    private val showNextButtonInNormalStateMutable = SingleLiveEvent<Event<Unit>>()
    val showNextButtonInNormalState: LiveData<Event<Unit>> = showNextButtonInNormalStateMutable

    private val showEnterCodeStateMutable = SingleLiveEvent<Event<Unit>>()
    val showEnterCodeState: LiveData<Event<Unit>> = showEnterCodeStateMutable

    private val showSuccessMutable = SingleLiveEvent<Event<MyInfo>>()
    val showSuccess: LiveData<Event<MyInfo>> = showSuccessMutable

    val passwordAlias = ObservableField<String>("")
    val phoneNumber = ObservableField<String>("")
    val code = ObservableField<String>("")

    val waitResultFromTelegramPasswordScreen = ObservableField<Boolean>(false)
    val loginToTelegram = MediatorLiveData<LoginToTelegram>().also {mediator->
        mediator.postValue(LoginToTelegram.ENTER_NUMBER)
        mediator.addSource(client.loginState) {
            passwordAlias.set("")
            it?.let {authState->
                when(authState) {
                    EnterPhone -> {
                        mediator.postValue(LoginToTelegram.ENTER_NUMBER)
                    }
                    EnterCode -> {
                        mediator.postValue(LoginToTelegram.ENTER_CODE)
                    }
                    is EnterPassword -> {
                        mediator.postValue(LoginToTelegram.ENTER_PASSWORD)
                        passwordAlias.set(authState.hint)
                        if (waitResultFromTelegramPasswordScreen.get() != true) {
                            waitResultFromTelegramPasswordScreen.set(true)
                            navigate(directions.actionEnterPhoneNumberToEnterTelegramPassword())
                        }
                    }
                    AuthOk -> {
                        mediator.postValue(LoginToTelegram.LOGIN_TO_TELEGRAM_SUCCESS)
                        handleSuccessLoginInTelegram()
                    }
                }
            }
        }
    }

    fun showEnterCodeUI() {
        showNextButtonInProgressStateMutable.postEvent()
        startBgJob {
            delay(1000)
            showEnterCodeStateMutable.postEvent()
        }
    }

    fun navigateToTelegramPassword() {
        navigate(directions.actionEnterPhoneNumberToEnterTelegramPassword())
    }

    fun showProgressAfterEnteredCode() {
        showNextButtonInProgressStateMutable.postEvent()
    }

    fun moveToDetectPhoneNumberIfNeed(): Boolean {
        if (userInfoRepository.getPhoneNumberFromSystem() == null) {
            navigate(directions.actionEnterPhoneNumberToDetectPhoneNumber())
            return true
        }
        return false
    }

    fun saveCountryMCC(mcc: Int) {
        userInfoRepository.setCountryMCC(mcc)
    }

    fun setPhoneNumberFromMemory() {
        userInfoRepository.getPhoneNumberFromSystem()?.let {
            if (it.isNotEmpty()) {
                phoneNumber.set(it)
            }
        }
    }

    fun testNav() {
//        navigate(directions.actionEnterPhoneNumberToEnterTelegramPassword())
    }

    fun onClickNextButton() {
        when (loginToTelegram.value) {
            LoginToTelegram.ENTER_NUMBER -> {
                phoneNumber.get()?.let {
                    sendPhoneToTelegram(phoneNumber = it)
                }
            }
            LoginToTelegram.ENTER_CODE -> {
                code.get()?.let {
                    sendCodeToTelegram(code = it)
                }
            }
        }
    }

    private fun sendPhoneToTelegram(phoneNumber: String) {
        asyncCall(ifBlockNotCalled = {
            showNextButtonInNormalStateMutable.postEvent()
            loginToTelegram.postValue(LoginToTelegram.ENTER_NUMBER)
        }) {
            showNextButtonInProgressStateMutable.postEvent()
            "phoneNumber=$phoneNumber".log()
            loginToTelegram.postValue(LoginToTelegram.ENTER_NUMBER_ALREADY_SENT)
            client.sendAuthPhone(phoneNumber)
        }
    }

    private fun sendCodeToTelegram(code: String) {
        asyncCall(ifBlockNotCalled = {
            showEnterCodeStateMutable.postEvent()
            loginToTelegram.postValue(LoginToTelegram.ENTER_CODE)
        }) {
            showNextButtonInProgressStateMutable.postEvent()
            loginToTelegram.postValue(LoginToTelegram.ENTER_CODE_ALREADY_SENT)
            client.sendAuthCode(code)
        }
    }

    private fun handleSuccessLoginInTelegram() {
        asyncCall(ifBlockNotCalled = {
            showEnterCodeStateMutable.postEvent()
            loginToTelegram.postValue(LoginToTelegram.LOGIN_TO_TELEGRAM_SUCCESS)
        }) {
            val me = client.getMe()
            val contacts = client.getContacts()
            val hasAccount = retryIO(times = 3) { wumfApi.checkReg(CheckRegistrationRequest(me.id.toString())).await() }.hasInDb

            var contactsAmount = 0
            val token = if (hasAccount) {
                val result = wumfApi.login(prepareLoginRequest(me, contacts)).await()
                contactsAmount = when {
                    result.friendsList.isNullOrEmpty() -> 0
                    result.friendsList.contains(",") -> result.friendsList.split(",").size
                    else -> 1
                }
                result.token
            } else {
                val result = wumfApi.registration(prepareRegistrationRequest(me, contacts)).await()
                contactsAmount = when {
                    result.friendsList.isNullOrEmpty() -> 0
                    result.friendsList.contains(",") -> result.friendsList.split(",").size
                    else -> 1
                }
                result.token
            }
            val dataForDialog = prepareDataForSuccessDialog(me, contacts, contactsAmount)
            userInfoRepository.setToken(token)
            showSuccessMutable.postEvent(dataForDialog)
            delay(1000)
            navigateToHome()
        }
    }

    fun handleOnBoardingResult() {
        if (waitResultFromTelegramPasswordScreen.get() == true) {
            val isPasswordForTelegramEntered: Boolean = sharedViewModel.status == ResultStatus.SUCCESS
            if (isPasswordForTelegramEntered) {
                val password = sharedViewModel.data as String
                asyncCall(ifBlockNotCalled = {
                    loginToTelegram.postValue(LoginToTelegram.ENTER_NUMBER)
                }, errorHandler = {
                    super.handleError(it)
                    waitResultFromTelegramPasswordScreen.set(true)
                }) {
                    showNextButtonInProgressStateMutable.postEvent()
                    waitResultFromTelegramPasswordScreen.set(false)
                    client.sendAuthPassword(password)
                }
            }
        }
    }

    suspend fun prepareDataForSuccessDialog(me: TdApi.User, contacts: TdApi.Users, contactsAmount: Int): MyInfo {
        var file: TdApi.File? = null
        me.profilePhoto?.big?.let {
            file = if (it.local.canBeDownloaded) {
                if (!it.local.isDownloadingCompleted) {
                    client.downloadFile(it.id)
                } else {
                    it
                }
            } else {
                null
            }
        }
        val name = me.firstName
        return MyInfo(image = file, name = name, contactsAmount = contactsAmount)
    }

    fun navigateToHome() {
        sharedViewModel.status = ResultStatus.SUCCESS
        popBack()
    }

    fun prepareLoginRequest(me: TdApi.User, contacts: TdApi.Users): LoginRequest {
        val userId = me.id.toString()
        val friendsList = contacts.userIds.joinToString(",")
        return LoginRequest(userId = userId, friendsList = friendsList, passwordHash = "test")
    }

    fun prepareRegistrationRequest(me: TdApi.User, contacts: TdApi.Users): RegistrationRequest {
        val userId = me.id.toString()
        val userName = me.firstName
        val friendsList = contacts.userIds.joinToString(",")
        return RegistrationRequest(userId = userId, friendsList = friendsList, createdPasswordHash = "test", displayName = userName,
            country = "ua"
        )
    }

    class MyInfo(val image: TdApi.File?, val name: String, val contactsAmount: Int)

    enum class LoginToTelegram {
        ENTER_NUMBER,
        ENTER_NUMBER_ALREADY_SENT,
        ENTER_CODE,
        ENTER_CODE_ALREADY_SENT,
        ENTER_PASSWORD,
        LOGIN_TO_TELEGRAM_SUCCESS,
        ENTER_PASSWORD_ALREADY_SENT,
        SUCCESS,
        FAILED
    }

}