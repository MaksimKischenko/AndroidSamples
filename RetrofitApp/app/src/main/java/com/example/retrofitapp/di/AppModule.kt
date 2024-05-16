package com.example.retrofitapp.di


import com.example.retrofitapp.controllers.RetrofitController
import com.example.retrofitapp.repositories.UserRepository
import com.example.retrofitapp.view_models.UserAddViewModel
import com.example.retrofitapp.view_models.UsersViewModel
import com.example.retrofitapp.view_models.UsersViewModelLiveData
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
// implementation ("io.insert-koin:koin-androidx-compose:3.5.3")
val appModule = module {
    single { RetrofitController() }
    single { UserRepository(get()) }
    single { UsersViewModel(get())}
    single { UsersViewModelLiveData(get())}
    viewModel { UserAddViewModel(get())}
}
