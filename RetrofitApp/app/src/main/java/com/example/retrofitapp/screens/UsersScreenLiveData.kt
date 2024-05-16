package com.example.retrofitapp.screens

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.retrofitapp.di.serviceLocator
import com.example.retrofitapp.ui.theme.customRed
import com.example.retrofitapp.view_models.UsersViewModelLiveData
import com.example.retrofitapp.widgets.AppBarLiveData

@Composable
fun UsersScreenLiveData(
    onAddClick: () -> Unit, onEditClick: () -> Unit
) {
    val usersLiveDataViewModel = serviceLocator<UsersViewModelLiveData>()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        AppBarLiveData(
            onAddClick, onEditClick, usersLiveDataViewModel
        )
    }, snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState
        ) {
            Snackbar(
                containerColor = customRed,
                snackbarData = it
            )
        }
    }) { padding ->
        UsersScreenBodyLiveData(padding, usersLiveDataViewModel)
        usersLiveDataViewModel.showErrorSnackBar(
            scope, snackbarHostState
        )
    }
}
