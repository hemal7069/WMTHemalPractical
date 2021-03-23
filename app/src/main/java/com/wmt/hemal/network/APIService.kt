package com.wmt.hemal.network

import com.wmt.hemal.model.Register
import com.wmt.hemal.model.UserList
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @FormUrlEncoded
    @POST(AppURL.REGISTER)
    fun registerUser(
        @Field("username") userName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Register>

    @GET(AppURL.USERS_LIST)
    fun userList(
        @HeaderMap headerMap: Map<String, String>,
        @Query("page") page: String
    ): Call<UserList>
}