package com.example.quishcatcher.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST("api/login")
    fun login(
        @Body payload: LoginRequest,
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("api/register")
    fun register(
        @Body payload: RegistrationRequest,
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("api/forget-password")
    fun forgetPassword(
        @Body payload: ForgetPasswordRequest,
    ): Call<ForgetPasswordResponse>


    @Headers("Content-Type: application/json")
    @POST("api/scan")
    fun scanUrl(
        @Body payload: ScanUrlRequest,
        @Header("Authorization") auth: String
    ): Call<ScanResponse>

    @Headers("Content-Type: application/json")
    @POST("api/logout")
    fun logout(
        @Header("Authorization") auth: String
    ): Call<LogoutResponse>


}