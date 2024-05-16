package com.example.retrofitapp.di

import androidx.compose.runtime.Composable
import com.example.retrofitapp.controllers.RetrofitController
import com.example.retrofitapp.repositories.UserRepository
import com.example.retrofitapp.view_models.UserAddViewModel
import com.example.retrofitapp.view_models.UsersViewModel
import com.example.retrofitapp.view_models.UsersViewModelLiveData
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf


@Composable
inline fun <reified T> serviceLocator(
    retrofitController:RetrofitController = koinInject<RetrofitController>(),
    usersRepository:UserRepository = koinInject<UserRepository>(parameters = { parametersOf(retrofitController) }),
):T {
    if (T::class == UsersViewModel::class) {
        val usersViewModel =  koinViewModel<UsersViewModel>(parameters = { parametersOf(usersRepository) })
        return usersViewModel as T
    }
    if (T::class == UsersViewModelLiveData::class) {
        val usersViewModelLiveData =  koinViewModel<UsersViewModelLiveData>(parameters = { parametersOf(usersRepository) })
        return usersViewModelLiveData as T
    }
    if (T::class == UserAddViewModel::class) {
        val userAddViewModel = koinViewModel<UserAddViewModel>(parameters = { parametersOf(usersRepository) })
        return userAddViewModel as T
    }
    throw IllegalArgumentException("Unsupported type")
}