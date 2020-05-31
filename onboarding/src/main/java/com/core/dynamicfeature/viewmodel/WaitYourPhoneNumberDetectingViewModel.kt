package com.core.dynamicfeature.viewmodel

//import android.os.CountDownTimer
//import androidx.databinding.ObservableField
//import androidx.lifecycle.ViewModel
//import androidx.navigation.NavController
//import com.core.sample.ShowDetectedPhoneNumberEvent
//import com.core.sample.databinding.FrgWaitYourPhoneNumberDetectingBinding
//import com.core.sample.fragment.WaitYourPhoneNumberDetectingFragmentDirections
//import com.core.sample.memory.UserInfoRepository
//import com.library.core.BaseViewModel
//import com.library.core.di.ViewModelKey
//import dagger.Module
//import dagger.Provides
//import dagger.multibindings.IntoMap
//import kotlinx.coroutines.delay
//import java.lang.Exception
//import javax.inject.Inject
//
//private val CHANGE_FAKE_PHONE_NUMBER_INTERVAL = 450L
//private val PHONE_NUMBER_DETECTING_SECONDS = 4
//private val COUNT_DOWN_TIMER_STEP = CHANGE_FAKE_PHONE_NUMBER_INTERVAL * 2
//private val COUNT_DOWN_TIMER_DURATION = COUNT_DOWN_TIMER_STEP * PHONE_NUMBER_DETECTING_SECONDS
//
//@Module
//class WaitYourPhoneNumberDetectingModule {
//    @Provides
//    @IntoMap
//    @ViewModelKey(WaitYourPhoneNumberDetectingViewModel::class)
//    fun bindViewModelKey(repository: UserInfoRepository): ViewModel =
//        WaitYourPhoneNumberDetectingViewModel(
//            repository
//        )
//}
//
//class WaitYourPhoneNumberDetectingViewModel @Inject constructor(private var repository: UserInfoRepository): BaseViewModel<FrgWaitYourPhoneNumberDetectingBinding>() {
//
//    val phoneNumberDetectingSeconds = ObservableField(PHONE_NUMBER_DETECTING_SECONDS)
//
//    private val phoneNumberDetectingTimer = object : CountDownTimer(
//        COUNT_DOWN_TIMER_DURATION,
//        COUNT_DOWN_TIMER_STEP
//    ) {
//
//        @Volatile
//        private var isRunning = false
//
//        override fun onTick(millisUntilFinished: Long) {
//            phoneNumberDetectingSeconds.set(phoneNumberDetectingSeconds.get()!! - 1)
//            if (phoneNumberDetectingSeconds.get() == 1) {
//                postEvent(ShowDetectedPhoneNumberEvent())
//            }
//        }
//
//        override fun onFinish() {
//            isRunning = false
//            phoneNumberDetectingSeconds.set(1)
//        }
//
//        fun startTask() {
//            if (!isRunning) {
//                isRunning = true
//                start()
//            }
//        }
//
//        fun cancelTask() {
//            isRunning = false
//            cancel()
//        }
//    }
//
//    fun handleTestError() {
//        handleException(Exception("Some error"))
//    }
//
//    override fun handleException(e: Throwable) {
//
//    }
//
//    fun isNeedDetectPhoneNumber(): Boolean {
//        val result = !repository.isPhoneNumberHintShowed()
//        return result
//    }
//
//    fun cantDetectPhoneNumber() {
//        repository.setPhoneNumberFromSystem("")
//        nextScreenWithDelay()
//    }
//
//    fun detectPhoneNumber(phoneNumber: String) {
//        repository.setPhoneNumberFromSystem(phoneNumber)
//        nextScreenWithDelay(phoneNumber)
//    }
//
//    private fun nextScreenWithDelay(phoneNumber: String="") {
//        startBgJob {
//            delay(100)//Todo fixme (onActivityResult called when activity paused)
//            nextScreen(phoneNumber)
//        }
//    }
//
//    fun nextScreen(phoneNumber: String="", navController: NavController? = null) {
//        val nextScreenDirections = WaitYourPhoneNumberDetectingFragmentDirections.actionSelectToAttractions(phoneNumber)
//        navController?.let {
//            navController.navigate(nextScreenDirections)
//        } ?:run {
//            navigate(nextScreenDirections)
//        }
//
//    }
//
//
//
//    fun isPhoneNumberDetectingStarted(): Boolean {
//        return phoneNumberDetectingSeconds.get() != PHONE_NUMBER_DETECTING_SECONDS
//    }
//
//    fun interruptPhoneNumberDetecting() {
//        phoneNumberDetectingTimer.cancelTask()
//    }
//
//    fun startPhoneNumberDetecting() {
//        phoneNumberDetectingTimer.startTask()
//    }
//
//    fun resumePhoneNumberDetecting() {
//        phoneNumberDetectingTimer.startTask()
//    }
//
//    fun getStatusBarHeight() = repository.getStatusBarHeight()
//
//}