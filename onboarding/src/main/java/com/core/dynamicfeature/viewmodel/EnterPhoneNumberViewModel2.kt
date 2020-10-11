package com.core.dynamicfeature.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.api.api.CheckRegistrationRequest
import com.app.api.api.LoginRequest
import com.app.api.api.RegistrationRequest
import com.app.api.api.WumfApi
import com.core.dynamicfeature.fragment.EnterPhoneNumberFragmentDirections
import com.core.wumfapp2020.api.Friend
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.util.FriendsUtils
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
import wumf.com.detectphone.AppCountryDetector

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
    val loginToTelegram = MediatorLiveData<LoginToTelegram>().also { mediator ->
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

            val wumfContactsIds: List<Friend>
            val token = if (hasAccount) {
                val loginRequest = prepareLoginRequest(me, contacts)
                val loginResponse = wumfApi.login(loginRequest).await()
                wumfContactsIds = loginResponse.friendsList
                loginResponse.token
            } else {
                val registrationResponse = wumfApi.registration(prepareRegistrationRequest(me, contacts)).await()
                wumfContactsIds = registrationResponse.friendsList
                registrationResponse.token
            }
            val dataForDialog = prepareDataForSuccessDialog(me = me, contacts = contacts, wumfContacts = wumfContactsIds.map { it.id } )
            val users = FriendsUtils.getFullInfoFriends(friends = wumfContactsIds, client = client)
            dataForDialog.friends.addAll(users)
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

    suspend fun prepareDataForSuccessDialog(me: TdApi.User, contacts: TdApi.Users, wumfContacts: List<Int>): MyInfo {
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
        val contactsIds = contacts.userIds.toList()
        return MyInfo(image = file, name = name, friendIds = wumfContacts, contacts = contactsIds, telegramId = me.id, phoneNumber = phoneNumber.get())
    }

    fun navigateToHome() {
        sharedViewModel.status = ResultStatus.SUCCESS
        popBack()
    }

    fun prepareLoginRequest(me: TdApi.User, contacts: TdApi.Users): LoginRequest {
        val userId = me.id.toString()
        val friendsList = contacts.userIds.joinToString(",")
        "friendsList=$friendsList".log()
        return LoginRequest(userId = userId, friendsList = friendsList, passwordHash = "test")
    }

    fun prepareRegistrationRequest(me: TdApi.User, contacts: TdApi.Users): RegistrationRequest {
        val userId = me.id.toString()
        val userName = me.firstName
        val friendsList = contacts.userIds.joinToString(",")
        val lastDetectedCountry = AppCountryDetector.getLastDetectedCountryByPhoneCode()
        val lastDetectedCountryShortStr = lastDetectedCountry?.code ?: "ua"
        return RegistrationRequest(userId = userId, friendsList = friendsList, createdPasswordHash = "test", displayName = userName,
            country = lastDetectedCountryShortStr
        )
    }

    class MyInfo(val image: TdApi.File?, val name: String, val telegramId: Int, val phoneNumber: String?,
                 val friends: ArrayList<TdApi.User> = ArrayList(), val contacts: List<Int>, val friendIds: List<Int>)

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