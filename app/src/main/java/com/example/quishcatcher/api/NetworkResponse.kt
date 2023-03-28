package com.example.quishcatcher.api

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("user"  ) var user  : User?   = User(),
    @SerializedName("token" ) var token : String? = null

)
data class User (

    @SerializedName("id"                ) var id              : Int?    = null,
    @SerializedName("username"          ) var username        : String? = null,
    @SerializedName("name"              ) var name            : String? = null,
    @SerializedName("email"             ) var email           : String? = null,
    @SerializedName("email_verified_at" ) var emailVerifiedAt : String? = null,
    @SerializedName("is_admin"          ) var isAdmin         : Int?    = null,
    @SerializedName("status"            ) var status          : Int?    = null,
    @SerializedName("created_at"        ) var createdAt       : String? = null,
    @SerializedName("updated_at"        ) var updatedAt       : String? = null

)

data class ForgetPasswordResponse (

    @SerializedName("message" ) var message : String? = null

)

data class ScanResponse (

    @SerializedName("isSafe"  ) var isSafe  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null

)


data class LogoutResponse (

    @SerializedName("message" ) var message : String? = null

)