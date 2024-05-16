package com.example.retrofitapp.remote_models

import com.google.gson.annotations.SerializedName

enum class UserTypeRoleById(val id: Int, val displayName: String) {
    @SerializedName("2")
    USER(2, "Пользователь"),
    @SerializedName("1")
    ADMIN(1, "Администратор"),
    @SerializedName("3")
    UNKNOWN(3, "Неизвестно"),
    @SerializedName("4")
    USER_VIEWER(4, "Просмотр пользователей")
}