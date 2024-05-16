package com.example.retrofitapp.remote_models

import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("self") val selfHref: SelfHref,
    @SerializedName("users") val entityHref: EntityHref?
)

data class SelfHref(
    @SerializedName("href") val href: String
)

data class EntityHref(
    @SerializedName("href") val href: String
)