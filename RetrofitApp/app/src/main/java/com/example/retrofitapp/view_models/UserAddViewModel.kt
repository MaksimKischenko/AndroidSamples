package com.example.retrofitapp.view_models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.retrofitapp.remote_models.User
import com.example.retrofitapp.remote_models.UserActionRequest
import com.example.retrofitapp.remote_models.UserActionRequestWrapper
import com.example.retrofitapp.remote_models.UserTypeRoleById
import com.example.retrofitapp.repositories.UserRepository
import kotlinx.coroutines.launch

data class UserAddScreenState(
    val userName: String = "",
    val nameFocusRequester: FocusRequester = FocusRequester(),
    val login: String = "",
    val loginFocusRequester: FocusRequester = FocusRequester(),
    val password: String = "",
    val passwordFocusRequester: FocusRequester = FocusRequester(),
    var userTypeRoleById: UserTypeRoleById? = null,
    val isBlocked: Boolean = false,
    val isLoading: Boolean = false,
    var exceptionMessage: String? = null,
    var addedUser: User? = null
)

class UserAddViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _state = mutableStateOf(UserAddScreenState())

    val state: State<UserAddScreenState> = _state

    fun updateUsername(input: String) {
        _state.value = _state.value.copy(userName = input)
    }

    fun updateLogin(input: String) {
        _state.value = _state.value.copy(login = input)
    }

    fun updatePassword(input: String) {
        _state.value = _state.value.copy(password = input)
    }

    fun updateUserRole(userTypeRoleById: UserTypeRoleById) {
        _state.value = _state.value.copy(userTypeRoleById = userTypeRoleById)
    }

    fun updateBlockedStatus(isBlocked: Boolean) {
        _state.value = _state.value.copy(isBlocked = isBlocked)
    }


    suspend fun addUser(): User? {
        var user: User? = null
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = userRepository.add(
                UserActionRequestWrapper(
                    UserActionRequest(
                        dateAdd = System.currentTimeMillis(),
                        name = state.value.userName,
                        login = _state.value.login,
                        password = _state.value.password,
                        type = _state.value.userTypeRoleById?.id,
                        blocked = _state.value.isBlocked
                    )
                )
            )) {
                is Either.Left -> _state.value = _state.value.copy(
                    isLoading = false, exceptionMessage = result.value.toString()
                )

                is Either.Right -> {
                    val addedUser = result.value?.collectionModel?.embedded?.users?.first()
                    _state.value = _state.value.copy(
                        isLoading = false, addedUser = addedUser
                    )
                    user = _state.value.addedUser
                }
            }
        }.join()
        return user
    }
}