package com.wmt.hemal.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("username") var username: String,
    @SerializedName("email") var email: String,
    @SerializedName("profile_pic") var profilePic: String,
    @SerializedName("created_at") var createdAt: String,
    @SerializedName("updated_at") var updatedAt: String,
    @SerializedName("id") var id: Int
)