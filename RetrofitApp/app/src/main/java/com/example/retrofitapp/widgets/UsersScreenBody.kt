package com.example.retrofitapp.widgets


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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.retrofitapp.ui.theme.customBrown
import com.example.retrofitapp.ui.theme.customWhite
import com.example.retrofitapp.view_models.UsersViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UsersScreenBody(
    padding: PaddingValues,
    usersViewModel: UsersViewModel,
) {
    val state by usersViewModel.state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = usersViewModel::loadUsers,
    )

    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn() {
            items(count = state.mutableUsers?.count() ?: 0) { index ->
                UserItem(state.mutableUsers?.get(index),
                    selected = state.mutableUsers?.get(index) == state.selectedUser,
                    onTap = {
                        usersViewModel.selectUser(it)
                    }
                )
            }
        }
        PullRefreshIndicator(
            refreshing = usersViewModel.state.value.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = customBrown,
            contentColor = customWhite
        )
    }
}

