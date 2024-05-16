package com.example.retrofitapp.remote_models

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("user_id") val id:Int,
    @SerializedName("name") val name:String?,
    @SerializedName("login") val login:String?,
    @SerializedName("password") val password:String?,
    @SerializedName("blocked") val blocked:Boolean?,
    @SerializedName("type_id") val type:UserTypeRoleById?,
    @SerializedName("description") val description:String?,
    @SerializedName("dateAdd") val dateAdd:Int?,
    @SerializedName("dateBlocked") val dateBlocked:Int?,
    @SerializedName("phoneNumber") val phoneNumber:String?,
    @SerializedName("email") val email:String,
    @SerializedName("blockedName") val blockedName:String?,
    @SerializedName("passwordExpire") val passwordExpire:Int?,
    @SerializedName("dateChangePassword") val dateChangePassword:Int?,
    @SerializedName("_links") val links:Links?,
)
