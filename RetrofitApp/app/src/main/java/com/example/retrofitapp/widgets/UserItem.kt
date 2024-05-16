package com.example.retrofitapp.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import com.example.retrofitapp.remote_models.User
import com.example.retrofitapp.ui.theme.customBlack
import com.example.retrofitapp.ui.theme.customBlue
import com.example.retrofitapp.ui.theme.customGray

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserItem(user: User?, selected: Boolean, onTap: (item: User) -> Unit) {
    val isItemSelected = remember {
        mutableStateOf(selected)
    }
    ListItem(
        icon = {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(40.dp)
                    .background(customBlue)
            )
        },
        text = {
            Text(
                text = user?.name ?: "",
                style = TextStyle(
                    color = customBlack
                ),
                fontSize = 18.sp,
            )
        },
        secondaryText = {
            Text(
                text = user?.email ?: "",
                style = TextStyle(
                    color = customBlack
                ),
                fontSize = 14.sp,
            )
        },
        overlineText = {
            Text(
                text = user?.type?.name?:"",
                style = TextStyle(
                    color = customGray
                ),
                fontSize = 12.sp,
            )
        },
        trailing = {
            Checkbox(
                checked = isItemSelected.value, //isItemSelected.value,
                onCheckedChange = {
                    isItemSelected.value = it
                    onTap.invoke(user!!)
                }
            )
        }
    )
}
