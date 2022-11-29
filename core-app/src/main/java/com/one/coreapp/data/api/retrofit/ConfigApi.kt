package com.one.coreapp.data.api.retrofit

import com.one.coreapp.entities.Config
import retrofit2.http.GET
import retrofit2.http.Headers

interface ConfigApi {

    @Headers(value = ["enableDecrypt:false"])
    @GET("https://raw.githubusercontent.com/hoanganhtuan95ptit/Config/main/config.json")
    suspend fun fetchConfig(): Config
}