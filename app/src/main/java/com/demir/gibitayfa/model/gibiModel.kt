package com.demir.gibitayfa.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class gibiModel(
    val title:String,
    val url:String,
    val user:String,
    var isPlay:Boolean=false
):Parcelable{
    constructor():this("","","")
}
