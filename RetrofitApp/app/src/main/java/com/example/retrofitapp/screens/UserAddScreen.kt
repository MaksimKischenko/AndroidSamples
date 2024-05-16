package com.example.retrofitapp.screens


import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.di.serviceLocator
import com.example.retrofitapp.remote_models.UserTypeRoleById
import com.example.retrofitapp.ui.theme.customBlue
import com.example.retrofitapp.ui.theme.customRed
import com.example.retrofitapp.view_models.UserAddViewModel
import com.example.retrofitapp.view_models.UsersViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddScreen(
    onAddedClick: (userId: Int) -> Unit
) {
    val userAddViewModel = serviceLocator<UserAddViewModel>()
    val usersViewModel = serviceLocator<UsersViewModel>()
    val state by userAddViewModel.state
    var expanded by remember {
        mutableStateOf(false)
    }
    SystemBackButtonListener(
        userAddViewModel = userAddViewModel,
        onAddedClick = onAddedClick
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Добавление пользователя", style = TextStyle(
                fontSize = 24.sp
            )
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { state.nameFocusRequester.requestFocus() }),
            label = {
                Text(text = "Имя пользователя")
            },

            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = customBlue,
                unfocusedIndicatorColor = customBlue,
                errorIndicatorColor = customRed
            ),
            value = state.userName,
            onValueChange = { userAddViewModel.updateUsername(it) },
//            isError = state.userName.isEmpty(),
//            supportingText = { Text(text = "Error")}

        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(state.nameFocusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                state.loginFocusRequester.requestFocus()
            }),
            label = {
                Text(text = "Логин")
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = customBlue,
                unfocusedIndicatorColor = customBlue,
                errorIndicatorColor = customRed
            ),
            value = state.login,
            onValueChange = { userAddViewModel.updateLogin(it) },
//            isError = state.userName.isEmpty()
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(state.loginFocusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    state.passwordFocusRequester.requestFocus()
                    expanded = true
                }
            ),
            label = {
                Text(text = "Пароль")
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = customBlue,
                unfocusedIndicatorColor = customBlue,
                errorIndicatorColor = customRed
            ),
            value = state.password,
            onValueChange = { userAddViewModel.updatePassword(it) },
//            isError = state.userName.isEmpty()
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .focusRequester(state.passwordFocusRequester)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = customBlue,
                        unfocusedIndicatorColor = customBlue,
                        errorIndicatorColor = customRed
                    ),
                    value = state.userTypeRoleById?.displayName ?: "Роль пользователя",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    UserTypeRoleById.entries.forEach { item ->
                        DropdownMenuItem(text = { Text(text = item.displayName) }, onClick = {
                            userAddViewModel.updateUserRole(item)
                            expanded = false
                        })
                    }
                }
            }
        }
        Switch(
            checked = state.isBlocked,
            onCheckedChange = {
                userAddViewModel.updateBlockedStatus(it)
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End),
            onClick = {
                userAddViewModel.viewModelScope.launch {
                    val newUser = userAddViewModel.addUser()
                    usersViewModel.updateNewUser(newUser)
                    onAddedClick.invoke(newUser?.id ?: 0)
                    Log.d("MyLog", "login: ${newUser?.login}")
                }
            }
        ) {
            Text(text = "Добавить пользователя")
        }
    }
}

@Composable
fun SystemBackButtonListener(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    backDispatcherOwner: OnBackPressedDispatcherOwner? = LocalOnBackPressedDispatcherOwner.current,
    backDispatcher: OnBackPressedDispatcher? = backDispatcherOwner?.onBackPressedDispatcher,
    userAddViewModel: UserAddViewModel,
    onAddedClick: (userId: Int) -> Unit
) {
    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("MyLog", "backDispatcher")
                userAddViewModel.viewModelScope.launch {
                    onAddedClick.invoke(0)
                }
            }
        }
    }
    backDispatcher?.addCallback(lifecycleOwner, callback)
}

