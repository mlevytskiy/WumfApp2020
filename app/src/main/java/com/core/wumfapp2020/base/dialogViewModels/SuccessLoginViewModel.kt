package com.core.wumfapp2020.base.dialogViewModels

import androidx.databinding.ObservableField
import com.app.api.api.HeaderInterceptor
import com.app.api.api.WumfApi
import com.app.api.api.executeRetrofit
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.StringRes
import com.core.wumfapp2020.memory.FriendsRepository
import com.core.wumfapp2020.memory.MyAppsCollectionRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.memory.impl.RegistrationInfo
import com.library.core.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import org.drinkless.td.libcore.telegram.TdApi

class SuccessLoginViewModel @AssistedInject constructor(@Assisted val image: TdApi.File?,
                                                        @Assisted val name: String, @Assisted val contactsAmount: Int,
                                                        @Assisted private val telegramId: Int?,
                                                        @Assisted private val phoneNumber: String?,
                                                        @Assisted val dismissDialog: ()->Unit,
                                                        @Assisted private val allContacts: List<Int>,
                                                        @Assisted private val contactsWithWumf: List<TdApi.User>,
                                                        private val wumfApi: WumfApi,
                                                        private val appsRepository: MyAppsCollectionRepository,
                                                        private val headerInterceptor: HeaderInterceptor,
                                                        private val userInfoRepository: UserInfoRepository,
                                                        private val friendsRepository: FriendsRepository): BaseViewModel() {

    val photoFilePath: ObservableField<String?> = ObservableField("")

    init {
        progress.set(true)
    }

    fun doWork() {
        startBgJob {
            val deferredList = listOf(
                async {
                    var hasPhoto = image?.local?.isDownloadingActive == true || image?.local?.isDownloadingCompleted == true
                    val registrationInfo = RegistrationInfo()
                    if (hasPhoto) {
                        for (i in 1..30) {
                            delay(100)
                            if (photoFilePath.get().isNullOrEmpty() && (image?.local?.isDownloadingCompleted == true)) {
                                photoFilePath.set(image.local.path)
                                registrationInfo.photo = image.local.path
                                break
                            }
                        }
                    }
                    registrationInfo.hasRegistration = true
                    registrationInfo.isRegWumfChecked = true
                    registrationInfo.name = name
                    registrationInfo.telegramId = telegramId
                    registrationInfo.phoneNumber = phoneNumber
                    userInfoRepository.setTelegramUser(registrationInfo)
                    friendsRepository.setAllContacts(allContacts)
                    friendsRepository.setWumfContacts(contactsWithWumf)
                },
                async {
                    updateToken()
                    val response = executeRetrofit(wumfApi.getApps())
                    response?.apps?.let {
                        if (it.isNotEmpty()) {
                            if (!it.contains(",")) {
                                appsRepository.replaceAppsInMemory(listOf(it))
                            } else {
                                appsRepository.replaceAppsInMemory(it.split(","))
                            }
                        }
                    }

                },
                async {
                    delay(3000)
                }
            )
            deferredList.awaitAll()
            dismissDialog()
        }
    }

    private fun updateToken() {
        userInfoRepository.getToken()?.let {
            headerInterceptor.updateToken(it)
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(image: TdApi.File?, name: String, contactsAmount: Int, telegramId: Int?, phoneNumber: String?,
                   allContacts: List<Int>, contactsWithWumf: List<TdApi.User>, dismissDialog: ()->Unit): SuccessLoginViewModel
    }

}