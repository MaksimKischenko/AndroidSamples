package com.example.retrofitapp.repositories


import com.example.retrofitapp.controllers.RetrofitController
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.retrofitapp.remote.UserApi
import com.example.retrofitapp.remote_models.User
import com.example.retrofitapp.remote_models.UserActionRequestWrapper
import com.example.retrofitapp.remote_models.UsersWrapper
import kotlinx.coroutines.coroutineScope
import java.net.ConnectException


class UserRepository(retrofitController: RetrofitController) {

    private var userApi: UserApi? =
        retrofitController.getRetrofitInstance().create(UserApi::class.java)

    suspend fun getAll(): Either<Exception, List<User>?>  {
        return try {
            userApi?.getAll()?.collectionModel?.embedded?.users.right()
        } catch (e: ConnectException) {
            e.left()
        }
    }

    suspend fun getById(userId: Int): Either<Exception, UsersWrapper?>  {
        return try {
            userApi?.getById(userId).right()
        } catch (e: Exception) {
            e.left()
        }
    }

    suspend fun deleteById(userId: Int): Either<Exception, Map<String, String>?>  {
        return try {
            userApi?.deleteById(userId).right()
        } catch (e: Exception) {
            e.left()
        }
    }

    suspend fun add(userActionWrapper: UserActionRequestWrapper): Either<Exception, UsersWrapper?>  {
        return try {
            userApi?.add(userActionWrapper).right()
        } catch (e: Exception) {
            e.left()
        }
    }

}