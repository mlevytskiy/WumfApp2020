package com.core.wumfapp2020.viewmodel

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.app.api.api.HeaderInterceptor
import com.core.wumfapp2020.memory.HomeStateRepository
import com.core.wumfapp2020.memory.MyAppsCollectionRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.library.Event
import com.library.core.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import krafts.alex.tg.TgClient
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val manager: SplitInstallManager,
                                           val sharedViewModel: SharedViewModel,
                                           private val userInfoRepository: UserInfoRepository,
                                           private val appsRepository: MyAppsCollectionRepository,
                                           private val headerInterceptor: HeaderInterceptor,
                                           private val client: TgClient,
                                           private val homeStateRepository: HomeStateRepository
): AnyFragmentBaseViewModel() {

//    private val directions = PreOnBoardingFragmentDirections.Companion

    private val logOutMutable = SingleLiveEvent<Event<Unit>>()
    val logOut: LiveData<Event<Unit>> = logOutMutable

    val inProgress = ObservableBoolean(false)
    val hasPhoto: Boolean
    val photo: String?
    val name: String?
    val surname: String?
    val phoneNumber = ObservableField<String>()
    val hasProfile = true

    init {
        photo = userInfoRepository.getTelegramUser()?.photo
        hasPhoto = !photo.isNullOrEmpty()
        name = userInfoRepository.getTelegramUser()?.name
        surname = userInfoRepository.getTelegramUser()?.surname
        phoneNumber.set(userInfoRepository.getTelegramUser()?.phoneNumber)
    }

    fun onClickLogOut() {
        logOutMutable.postEvent(Event(Unit))
    }

    fun logOut(refreshUI: ()->Unit) {
        inProgress.set(true)
        asyncCall(ifBlockNotCalled = {
            inProgress.set(false)
        }) {
            client.logOut()
            client.destroy()
            delay(1000) //I don't process authorizationStateClosed state that why we have delay here
            appsRepository.clear()
            headerInterceptor.removeToken()
            userInfoRepository.clear()
            homeStateRepository.clear()
            inProgress.set(false)
            GlobalScope.launch(Dispatchers.Main) {
                refreshUI()
            }
        }
    }

}