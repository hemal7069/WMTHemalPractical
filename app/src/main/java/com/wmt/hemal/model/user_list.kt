package com.wmt.hemal.model

import com.google.gson.annotations.SerializedName


data class UserList(
    @SerializedName("meta") var meta: Meta,
    @SerializedName("data") var `data`: UserData
)

data class UserData(
    @SerializedName("users") var users: List<User>,
    @SerializedName("pagination") var pagination: Pagination
)

data class Pagination(
    @SerializedName("total") var total: Int,
    @SerializedName("perPage") var perPage: Int,
    @SerializedName("page") var page: String,
    @SerializedName("lastPage") var lastPage: Int
)
