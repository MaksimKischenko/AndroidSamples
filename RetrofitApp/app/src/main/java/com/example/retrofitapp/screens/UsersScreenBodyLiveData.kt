package com.example.retrofitapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.retrofitapp.ui.theme.customBrown
import com.example.retrofitapp.ui.theme.customWhite
import com.example.retrofitapp.view_models.UsersViewModelLiveData
import com.example.retrofitapp.widgets.UserItem


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UsersScreenBodyLiveData(
    padding: PaddingValues,
    usersViewModelLiveData: UsersViewModelLiveData,
) {
    val isLoading = usersViewModelLiveData.isLoading.observeAsState().value
    val users = usersViewModelLiveData.users.observeAsState().value

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading?:false,
        onRefresh = usersViewModelLiveData::loadUsers,
    )

    SideEffect {
        Log.d("MyLog", "SideEffect")
    }


    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn() {
            items(count = usersViewModelLiveData.users.value?.count() ?: 0) { index ->
                UserItem(users?.get(index), selected = false, onTap = {
                    usersViewModelLiveData.selectUser(it)
                })
            }
        }
        PullRefreshIndicator(
            refreshing = isLoading?:false,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = customBrown,
            contentColor = customWhite
        )
    }
}

