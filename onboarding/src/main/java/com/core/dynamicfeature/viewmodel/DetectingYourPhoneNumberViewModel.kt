package com.core.dynamicfeature.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.core.dynamicfeature.fragment.DetectingYourPhoneNumberFragmentDirections
import com.core.wumfapp2020.fragment.PreOnBoardingFragmentDirections
import com.core.wumfapp2020.memory.UserInfoRepository
import com.library.core.BaseViewModel
import com.library.core.SingleLiveEvent
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.delay

private val CHANGE_FAKE_PHONE_NUMBER_INTERVAL = 450L
private val PHONE_NUMBER_DETECTING_SECONDS = 4
private val COUNT_DOWN_TIMER_STEP = CHANGE_FAKE_PHONE_NUMBER_INTERVAL * 2
private val COUNT_DOWN_TIMER_DURATION = COUNT_DOWN_TIMER_STEP * PHONE_NUMBER_DETECTING_SECONDS

class DetectingYourPhoneNumberViewModel @AssistedInject constructor(private val repository: UserInfoRepository): BaseViewModel() {

    private val directions = DetectingYourPhoneNumberFragmentDirections.Companion

    val phoneNumberDetectingSeconds = ObservableField(PHONE_NUMBER_DETECTING_SECONDS)

    private val showDetectPhoneNumberMutable = SingleLiveEvent<Unit>()
    val showDetectPhoneNumber: LiveData<Unit> = showDetectPhoneNumberMutable

    private val phoneNumberDetectingTimer = object : CountDownTimer(COUNT_DOWN_TIMER_DURATION, COUNT_DOWN_TIMER_STEP) {

        @Volatile
        private var isRunning = false

        override fun onTick(millisUntilFinished: Long) {
            phoneNumberDetectingSeconds.set(phoneNumberDetectingSeconds.get()!! - 1)
            if (phoneNumberDetectingSeconds.get() == 1) {
                showDetectPhoneNumberMutable.postEvent(Unit)
            }
        }

        override fun onFinish() {
            isRunning = false
            phoneNumberDetectingSeconds.set(1)
        }

        fun startTask() {
            if (!isRunning) {
                isRunning = true
                start()
            }
        }

        fun cancelTask() {
            isRunning = false
            cancel()
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): DetectingYourPhoneNumberViewModel
    }

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

    fun isNeedDetectPhoneNumber(): Boolean {
        val result = !repository.isPhoneNumberHintShowed()
        return result
    }

    fun cantDetectPhoneNumber() {
        repository.setPhoneNumberFromSystem("")
        nextScreenWithDelay()
    }

    fun detectPhoneNumber(phoneNumber: String) {
        repository.setPhoneNumberFromSystem(phoneNumber)
        nextScreenWithDelay(phoneNumber)
    }

    private fun nextScreenWithDelay(phoneNumber: String? = null) {
//        startBgJob {
//            delay(100)//Todo fixme (onActivityResult called when activity paused)
        Log.i("testr", "phoneNumber=" + phoneNumber)
            navigate(directions.actionDetectingYourPhoneNumberToEnterPhoneNumber(detectedPhone = phoneNumber))
//        }
    }

    fun nextScreen() {
        nextScreenWithDelay(repository.getPhoneNumberFromSystem())
//        navigate(directions.actionDetectingYourPhoneNumberToEnterPhoneNumber())
    }

    fun isPhoneNumberDetectingStarted(): Boolean {
        return phoneNumberDetectingSeconds.get() != PHONE_NUMBER_DETECTING_SECONDS
    }

    fun interruptPhoneNumberDetecting() {
        phoneNumberDetectingTimer.cancelTask()
    }

    fun startPhoneNumberDetecting() {
        phoneNumberDetectingTimer.startTask()
    }

    fun resumePhoneNumberDetecting() {
        phoneNumberDetectingTimer.startTask()
    }

}