package com.example.retrofitapp.remote_models

import com.google.gson.annotations.SerializedName

data class UserActionRequestWrapper(
    @SerializedName("User") val user: UserActionRequest?
)

data class UserActionRequest(
    @SerializedName("date_add") val dateAdd: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("login") val login: String?,
    @SerializedName("type_id") val type: Int?,
    @SerializedName("password") val password: String?,
    @SerializedName("blocked") val blocked: Boolean?,
)