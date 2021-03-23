package com.wmt.hemal.model

import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("meta") var meta: Meta,
    @SerializedName("data") var `data`: Data
)

data class Data(
    @SerializedName("user") var user: User,
    @SerializedName("token") var token: Token
)

data class Token(
    @SerializedName("type") var type: String,
    @SerializedName("token") var token: String,
    @SerializedName("refreshToken") var refreshToken: String
)