package com.example.quishcatcher.api

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email"    ) var email    : String? = null,
    @SerializedName("password" ) var password : String? = null
)

data class RegistrationRequest (
    @SerializedName("name"     ) var name     : String? = null,
    @SerializedName("email"    ) var email    : String? = null,
    @SerializedName("password" ) var password : String? = null
)

data class ForgetPasswordRequest (
    @SerializedName("email"    ) var email    : String? = null
)

data class LogoutRequest (
    @SerializedName("token"    ) var token    : String? = null
)

data class ScanUrlRequest (
    @SerializedName("url"      ) var url      : String? = null
)