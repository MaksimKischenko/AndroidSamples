package com.example.retrofitapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.retrofitapp.di.serviceLocator
import com.example.retrofitapp.models.Roots
import com.example.retrofitapp.screens.UserAddScreen
import com.example.retrofitapp.screens.UsersScreen
import com.example.retrofitapp.screens.UsersScreenLiveData
import com.example.retrofitapp.view_models.UsersViewModel

@Composable
fun NavigationScope() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Roots.Users.routeName + "/{userId}"
    ) {

        var newUserId:Int


        composable(
            Roots.Users.routeName + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            backStackEntry ->
            if(backStackEntry.arguments?.isEmpty == false){
                newUserId = backStackEntry.arguments?.getInt("userId")?:0
            }
            UsersScreen(
                onAddClick = { navController.navigate(Roots.UserAdd.routeName) },
                onEditClick = { navController.navigate(Roots.UserEdit.routeName) },
            )
        }
        composable(Roots.UserAdd.routeName) {
            UserAddScreen(
                onAddedClick = { userId ->
                    navController.navigate(Roots.Users.routeName + "/" + userId)
                },
            )
        }
        composable(Roots.UserEdit.routeName) {
        }
    }
}

