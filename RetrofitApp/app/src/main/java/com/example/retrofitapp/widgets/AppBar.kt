package com.example.retrofitapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.example.retrofitapp.ui.theme.PurpleGrey40
import com.example.retrofitapp.ui.theme.customWhite
import com.example.retrofitapp.view_models.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onAddClick: ()-> Unit,
    onEditClick: ()-> Unit,
    usersViewModel: UsersViewModel
) {
    val state by usersViewModel.state
    val isSelected = state.selectedUser != null
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary, titleContentColor = customWhite
        ),
        title = {
            Text(
                "Пользователи", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            Icon(modifier = Modifier
                .clickable {
                    onAddClick.invoke()
                }
                .size(24.dp),
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "bluetoothSearching",
                tint = customWhite)

            Icon(modifier = Modifier
                .clickable(
                    enabled = isSelected
                ) {
                    state.selectedUser?.id?.let { usersViewModel.deleteUser(it) }
                }
                .size(24.dp),
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = if(isSelected) customWhite else PurpleGrey40
            )

            Icon(modifier = Modifier
                .clickable(
                    enabled = isSelected
                ) {
                    onEditClick.invoke()
                }
                .size(24.dp),
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit",
                tint = if(isSelected) customWhite else PurpleGrey40
            )
        }
    )
}

