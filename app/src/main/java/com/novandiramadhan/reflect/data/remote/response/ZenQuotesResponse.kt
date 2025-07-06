package com.novandiramadhan.reflect.data.remote.response

import com.google.gson.annotations.SerializedName

data class ZenQuotesResponse(
    @SerializedName("q")
    val quote: String,

    @SerializedName("a")
    val author: String,
)
