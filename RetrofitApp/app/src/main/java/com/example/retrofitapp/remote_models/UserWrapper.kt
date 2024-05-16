package com.example.retrofitapp.remote_models

import com.google.gson.annotations.SerializedName

data class UsersWrapper(
    @SerializedName("CollectionModel") val collectionModel: CollectionModel?
)

data class CollectionModel(
    @SerializedName("_embedded") val embedded: Embedded?
)

data class Embedded(
    @SerializedName("users") val users: List<User>,
    @SerializedName("_links") val links: Links?
)