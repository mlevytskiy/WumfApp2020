package com.core.wumfapp2020.memory.impl

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Friend(var name: String = "",
             var surname: String = "",
             var photo: String? = null,
             var telegramId: Int = 0,
             var phoneNumber: String? = null,
             var apps: List<String> = emptyList()): Parcelable