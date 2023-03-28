package com.example.quishcatcher.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(
        @Body payload: LoginRequest,
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/register")
    fun register(
        @Body payload: RegistrationRequest,
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/forget-password")
    fun forgetPassword(
        @Body payload: ForgetPasswordRequest,
    ): Call<ForgetPasswordResponse>


    @Headers("Content-Type: application/json")
    @POST("/forget-password")
    fun scanUrl(
        @Body payload: ScanUrlRequest,
    ): Call<ScanResponse>


}