package com.wmt.hemal.network

import androidx.multidex.BuildConfig
import com.wmt.hemal.utility.Logger
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/***
 * Visit below link when going to implement the API
 * https://github.com/probelalkhan/kotlin-retrofit-tutorial/blob/master/app/src/main/java/simplifiedcoding/net/kotlinretrofittutorial/activities/LoginActivity.kt
 */
object RetrofitClient {
    private const val REQUEST_TIME_OUT = 120

    /***
     * Get Http Client
     */
    private fun getHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient
            .Builder()
            .connectTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .method(original.method, original.body)

                val request = requestBuilder.build()

                if (BuildConfig.DEBUG) {
                    Logger.e("Request: $request")
                }

                chain.proceed(request)
            }

        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        httpClient.addInterceptor(logging)

        // ClearText Specification Added for resolving the error
        httpClient.connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))

        return httpClient.build()
    }

    val instance: APIService by lazy {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(AppURL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient())
            .build()

        retrofit.create(APIService::class.java)
    }
}