package com.example.retrofitapp.screens


import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.example.retrofitapp.di.serviceLocator
import com.example.retrofitapp.ui.theme.customRed
import com.example.retrofitapp.view_models.UsersViewModel
import com.example.retrofitapp.widgets.AppBar
import com.example.retrofitapp.widgets.UsersScreenBody

@Composable
fun UsersScreen(
    onAddClick: () -> Unit, onEditClick: () -> Unit
) {
    val usersViewModel = serviceLocator<UsersViewModel>()
    val state by usersViewModel.state
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        AppBar(
            onAddClick, onEditClick, usersViewModel
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
        UsersScreenBody(padding, usersViewModel)
        usersViewModel.showErrorSnackBar(
            state, scope, snackbarHostState
        )
    }
}



