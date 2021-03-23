package com.wmt.hemal.model

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("status") var status: String,
    @SerializedName("message") var message: String
)
