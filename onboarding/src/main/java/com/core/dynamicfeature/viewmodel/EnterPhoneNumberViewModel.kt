//package com.core.dynamicfeature.viewmodel
//
//import android.util.Log
//import androidx.databinding.ObservableField
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.app.api.api.CheckRegistrationRequest
//import com.app.api.api.LoginRequest
//import com.app.api.api.RegistrationRequest
//import com.app.api.api.WumfApi
//import com.core.dynamicfeature.fragment.EnterPhoneNumberFragmentDirections
//import com.core.wumfapp2020.memory.impl.RegistrationInfo
//import com.core.wumfapp2020.memory.UserInfoRepository
//import com.core.wumfapp2020.viewmodel.ResultStatus
//import com.core.wumfapp2020.viewmodel.SharedViewModel
//import com.library.core.BaseViewModel
//import com.library.telegramkotlinapi.TelegramApi
//import com.library.telegramkotlinapi.TelegramApi.AuthWithPhoneResult
//import com.library.telegramkotlinapi.TelegramApi.TrueOrError
//import com.library.telegramkotlinapi.TelegramUser
//import com.squareup.inject.assisted.AssistedInject
//import kotlinx.coroutines.Deferred
//import kotlinx.coroutines.async
//import kotlinx.coroutines.delay
//import org.drinkless.td.libcore.telegram.Client
//import org.drinkless.td.libcore.telegram.TdApi
//import retrofit2.await
//import java.io.IOException
//
//class EnterPhoneNumberViewModel @AssistedInject constructor(val sharedViewModel: SharedViewModel, private val userInfoRepository: UserInfoRepository,
//                                                            private var wumfApi: WumfApi,
//                                                            private val telegramApi: TelegramApi): BaseViewModel() {
//
//    private val showNextButtonInProgressStateMutable = MutableLiveData<Unit>()
//    val showNextButtonInProgressState: LiveData<Unit> = showNextButtonInProgressStateMutable
//
//    private val showEnterCodeStateMutable = MutableLiveData<Unit>()
//    val showEnterCodeState: LiveData<Unit> = showEnterCodeStateMutable
//
//    private val showSuccessMutable = MutableLiveData<Unit>()
//    val showSuccess: LiveData<Unit> = showSuccessMutable
//
//    val phoneNumber = ObservableField<String>("")
//
//    private val directions = EnterPhoneNumberFragmentDirections.Companion
//
//    fun setPhoneNumberFromMemory() {
//        userInfoRepository.getPhoneNumberFromSystem()?.let {
//            phoneNumber.set(it)
//        }
//    }
//
//    @AssistedInject.Factory
//    interface Factory {
//        fun create(): EnterPhoneNumberViewModel
//    }
//
//    override fun handleException(e: Throwable) {
//        toast("error happens")
//    }
//
//    var code = ObservableField<String>("")
//    var state: State = State.ENTER_PHONE_NUMBER
//
//    fun getSystemPhoneNumber() = userInfoRepository.getPhoneNumberFromSystem()
//
//    fun getStatusBarHeight() = userInfoRepository.getStatusBarHeight()
//
//    fun onClickNextButton() {
//        when(state) {
//            State.ENTER_PHONE_NUMBER -> {
//                phoneNumber.get()?.let {
//                    sendPhoneToTelegram(it)
//                }
//            }
//            State.ENTER_CODE -> {
//                sendCodeToTelegram(code.get()!!)
//            }
//        }
//    }
//
//    fun moveToDetectPhoneNumberIfNeed() {
//        if (userInfoRepository.getPhoneNumberFromSystem() == null) {
//            navigate(directions.actionEnterPhoneNumberToDetectPhoneNumber())
//        }
//    }
//
//    fun sendCodeToTelegram(code: String) {
//        Log.i("testr1", "code=$code")
//        suspend fun applyDeffered(deffered: Deferred<Unit>, mintime: Long) {
//            scope.async { delay(mintime) }.await()
//            deffered.await()
//        }
//
//        fun getRegistrationInfo(userInfo: TelegramUser, hasRegistrationInWumf: Boolean): RegistrationInfo {
//            val registrationInfo = RegistrationInfo()
//            registrationInfo.isRegWumfChecked = true
//            registrationInfo.hasRegistration = false
//            registrationInfo.telegramId = userInfo.id
//            registrationInfo.name = userInfo.name
//            registrationInfo.photo = userInfo.photo
//            return registrationInfo
//        }
//
//        fun getContactsStr(users: TdApi.Users?): String {
//            users?.let {
//                val usersStr = users.userIds.joinToString(",")
//                return usersStr
//            } ?:run {
//                return ""
//            }
//        }
//
//        showNextButtonInProgressStateMutable.postEvent(Unit)
//        state = State.PROGRESS
//        startBgJob {
//            var isCorrectCode: TrueOrError? = null
//
//            var isRegistrationCompleted: Boolean? = null
//
//            applyDeffered( async {
//                isCorrectCode = telegramApi.checkVerificationCode(code)
//                Log.i("testr1", "checkVerificationCode isCorrect=" + isCorrectCode?.value)
//                if (isCorrectCode?.value == false) {
//                    Log.i("testr1", "checkVerificationCode error=" + isCorrectCode?.errorCode + " " + isCorrectCode?.errorMessage)
//                }
//                if (isCorrectCode?.value == true) {
//                    val userInfo = telegramApi.getUserInfo()
//                    Log.i("testr1", "userInfo id=" + userInfo?.id)
//                    userInfo?.let {
//                        val checkRegistration =
//                            retryIO(times = 3) { wumfApi.checkReg(CheckRegistrationRequest(userInfo.id.toString())).await() }
//                        userInfoRepository.setTelegramUser(
//                            getRegistrationInfo(
//                                userInfo,
//                                checkRegistration.hasInDb
//                            )
//                        )
//                        Log.i("testr1", "hasInDb=" + checkRegistration.hasInDb)
//                        val contacts = telegramApi.getContacts()
//                        contacts?.users?.let {
//                            Log.i("testr1", "contacts.count=" + it.totalCount)
//                        } ?:run {
//                            Log.i("testr1", "contacts error= ${contacts?.errorCode} ${contacts?.errorMessage} ${contacts?.exc?.toString()}")
//                        }
//
//                        if (checkRegistration.hasInDb) {
//                            val data = LoginRequest(
//                                userInfo.id.toString(),
//                                getContactsStr(contacts.users),
//                                "123"
//                            )
//                            val response = wumfApi.login(data).await()
//                            userInfoRepository.setToken(response.token)
//                        } else {
//                            val data = RegistrationRequest(
//                                userInfo.id.toString(),
//                                getContactsStr(contacts.users),
//                                "123",
//                                userInfo.name,
//                                "ua"
//                            )
//                            val response = wumfApi.registration(data).await()
//                            userInfoRepository.setToken(response.token)
//                        }
//                        isRegistrationCompleted = true
//                    }
//
//                }
//            }, 2000)
//
//            if (isRegistrationCompleted == true) {
//                showSuccessMutable.postEvent(Unit)
//                delay(1000)
//                navigateToHome()
//            } else {
//                //?
//            }
//        }
//    }
//
//    suspend fun <T> retryIO(
//        times: Int = Int.MAX_VALUE,
//        initialDelay: Long = 100, // 0.1 second
//        maxDelay: Long = 30000,    // 30 second
//        factor: Double = 2.0,
//        block: suspend () -> T): T
//    {
//        var currentDelay = initialDelay
//        repeat(times - 1) {
//            try {
//                return block()
//            } catch (e: IOException) {
//                // you can log an error here and/or make a more finer-grained
//                // analysis of the cause to see if retry is needed
//            }
//            delay(currentDelay)
//            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
//        }
//        return block() // last attempt
//    }
//
//    fun sendPhoneToTelegram(phoneNumber: String) {
//        showNextButtonInProgressStateMutable.postEvent(Unit)
//        state = State.PROGRESS
//        startBgJob {
//            var sendPhoneNumberResult: AuthWithPhoneResult? = null
//            val sendPhoneNumberOperation = scope.async {
//                sendPhoneNumberResult = telegramApi.authWithPhone(phoneNumber)
//            }
//            val delayOperation = scope.async { delay(1000) }
//            delayOperation.await()
//            sendPhoneNumberOperation.await()
//
//            val code = sendPhoneNumberResult?.errorCode
//            val msg = sendPhoneNumberResult?.errorMsg
//            when (sendPhoneNumberResult) {
//                AuthWithPhoneResult.SUCCESS -> {
//                    toast("onClickSendPhone success")
//                }
//                AuthWithPhoneResult.ERROR -> {
//                    Log.i("testr1", "onClickSendPhone error code=$code msg=$msg")
//                    toast("onClickSendPhone error code=$code msg=$msg")
//                }
//                AuthWithPhoneResult.ERROR_TOO_MANY_REQUESTS -> {
//                    Log.i("testr1", "Too many requests code=$code msg=$msg")
//                    toast("Too many requests code=$code msg=$msg")
//                }
//            }
//            state = State.ENTER_CODE
//            showEnterCodeStateMutable.postEvent(Unit)
//        }
//    }
//
//    fun saveCountryMCC(mcc: Int) {
//        userInfoRepository.setCountryMCC(mcc)
//    }
//
//    fun navigateToHome() {
//        sharedViewModel.status = ResultStatus.SUCCESS
//        popBack()
//        //navigate(directions.actionEnterPhoneNumberToPreOnBoarding())
//    }
//
//    enum class State {
//        ENTER_PHONE_NUMBER,
//        ENTER_CODE,
//        PROGRESS,
//        ERROR_SENDING_PHONE_NUMBER,
//        ERROR_SENDING_CODE
//    }
//}