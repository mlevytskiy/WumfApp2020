package com.core.wumfapp2020.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class FailedResultReason {
    NO_INTERNET_CONNECTION,
    ERROR_WHILE_CONNECTING_TO_TELEGRAM,
    ERROR_WHILE_CONNECTING_TO_SERVER,
    CANCELED_BY_USER
}

@Parcelize
class SuccessOnBoardingResult(val userId: Long): Parcelable

@Parcelize
class FailedOnBoardingResult(val failedResultReason: FailedResultReason): Parcelable