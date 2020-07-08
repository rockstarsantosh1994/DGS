package com.aspl.chat.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ansh on 25/08/18.
 */
@Parcelize
data class User(val uid: String,
                val name: String,
                val status: String,
                val profileImageUrl: String?,
                val gcm_token:String?) : Parcelable {
    constructor() : this("", "", "","","")
}