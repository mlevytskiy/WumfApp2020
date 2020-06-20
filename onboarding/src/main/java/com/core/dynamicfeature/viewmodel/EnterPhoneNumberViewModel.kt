package com.core.dynamicfeature.viewmodel

import android.util.Log
import com.app.api.api.WumfApi
import com.core.wumfapp2020.memory.RegistrationInfo
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.viewmodel.SharedViewModel
import com.library.core.BaseViewModel
import com.library.telegramkotlinapi.SimpleTelegramApi
import com.library.telegramkotlinapi.TelegramUser
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class EnterPhoneNumberViewModel @AssistedInject constructor(val sharedViewModel: SharedViewModel, private val userInfoRepository: UserInfoRepository,
                                                            client: Client, private var wumfApi: WumfApi): BaseViewModel() {


//    private val directions = EnterPhoneNumberFragmentDirections.Companion

    @AssistedInject.Factory
    interface Factory {
        fun create(): EnterPhoneNumberViewModel
    }

    override fun handleException(e: Throwable) {
//        showToast("error happens")
    }

    var phoneNumber: String = ""
    var code: String = ""
    var state: State = State.ENTER_PHONE_NUMBER

    private val telegramApi = SimpleTelegramApi().client(client)

    fun getSystemPhoneNumber() = userInfoRepository.getPhoneNumberFromSystem()

    fun getStatusBarHeight() = userInfoRepository.getStatusBarHeight()

    fun onClickNextButton() {
        when(state) {
            State.ENTER_PHONE_NUMBER -> {
                sendPhoneToTelegram(phoneNumber)
            }
            State.ENTER_CODE -> {
                sendCodeToTelegram(code)
            }
        }
    }

    fun sendCodeToTelegram(code: String) {

        suspend fun applyDeffered(deffered: Deferred<Unit>, mintime: Long) {
            scope.async { delay(mintime) }.await()
            deffered.await()
        }

        fun getRegistrationInfo(userInfo: TelegramUser, hasRegistrationInWumf: Boolean): RegistrationInfo {
            val registrationInfo = RegistrationInfo()
            registrationInfo.isRegWumfChecked = true
            registrationInfo.hasRegistration = false
            registrationInfo.telegramId = userInfo.id
            registrationInfo.name = userInfo.name
            registrationInfo.photo = userInfo.photo
            return registrationInfo
        }

        fun getContactsStr(users: TdApi.Users?): String {
            users?.let {
                val usersStr = users.userIds.joinToString(",")
                return usersStr
            } ?:run {
                return ""
            }
        }

//        postEvent(ShowNextButtonInProgressState())
        state = State.PROGRESS
        startBgJob {
            var isCorrectCode: Boolean? = null

            var isRegistrationCompleted: Boolean? = null

            applyDeffered( async {
                isCorrectCode = telegramApi.checkVerificationCode(code)
                if (isCorrectCode == true) {
                    val userInfo = telegramApi.getUserInfo()

                    userInfo?.let {
//                        val checkRegistration =
//                            wumfApi.checkReg(CheckRegistrationRequest(userInfo.id.toString()))
//                                .await()
//                        repository.setTelegramUser(
//                            getRegistrationInfo(
//                                userInfo,
//                                checkRegistration.hasInDb
//                            )
//                        )
//                        Log.i("testr", "hasInDb=" + checkRegistration.hasInDb)

                        val contacts = telegramApi.getContacts()
                        Log.i("testr", "contacts.count=" + contacts?.totalCount)

//                        if (checkRegistration.hasInDb) {
//                            val data = LoginRequest(
//                                userInfo.id.toString(),
//                                getContactsStr(contacts),
//                                "123"
//                            )
//                            val response = wumfApi.login(data).await()
//                            repository.setToken(response.token)
//                        } else {
//                            val data = RegistrationRequest(
//                                userInfo.id.toString(),
//                                getContactsStr(contacts),
//                                "123",
//                                userInfo.name,
//                                "ua"
//                            )
//                            val response = wumfApi.registration(data).await()
//                            repository.setToken(response.token)
//                        }
                        isRegistrationCompleted = true
                    }

                }
            }, 2000)

            if (isRegistrationCompleted == true) {
//                postEvent(ShowSuccess())
                delay(1000)
                navigateToHome()
            } else {
                //?
            }
        }
    }

    fun sendPhoneToTelegram(phoneNumber: String) {
//        postEvent(ShowNextButtonInProgressState())
        state = State.PROGRESS
        startBgJob {
            var sendPhoneNumberResult: SimpleTelegramApi.AuthWithPhoneResult? = null
            val sendPhoneNumberOperation = scope.async {
                sendPhoneNumberResult = telegramApi.authWithPhone(phoneNumber)
            }
            val delayOperation = scope.async { delay(1000) }
            delayOperation.await()
            sendPhoneNumberOperation.await()

            when (sendPhoneNumberResult) {
                SimpleTelegramApi.AuthWithPhoneResult.SUCCESS -> {
//                    showToast("onClickSendPhone success")
                }
                SimpleTelegramApi.AuthWithPhoneResult.ERROR -> {
//                    showToast("onClickSendPhone error")
                }
                SimpleTelegramApi.AuthWithPhoneResult.ERROR_TOO_MANY_REQUESTS -> {
//                    showToast("Too many requests")
                }
            }

            state = State.ENTER_CODE
//            postEvent(ShowEnterCodeState())
        }
    }

    fun navigateToHome() {
//        navigate(EnterPhoneNumberFragmentDirections.actionPhoneNumberToHome())
    }

    enum class State {
        ENTER_PHONE_NUMBER,
        ENTER_CODE,
        PROGRESS,
        ERROR_SENDING_PHONE_NUMBER,
        ERROR_SENDING_CODE
    }
}