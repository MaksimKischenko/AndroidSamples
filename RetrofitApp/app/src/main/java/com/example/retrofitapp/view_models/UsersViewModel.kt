package com.example.retrofitapp.view_models


import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.retrofitapp.remote_models.User
import com.example.retrofitapp.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class UsersScreenState(
    val isLoading: Boolean = false,
    var mutableUsers: List<User>? = emptyList(),
    var selectedUser: User? = null,
    var exceptionMessage: String? = null
)

class UsersViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _state = mutableStateOf(UsersScreenState())
    val state: State<UsersScreenState> = _state

    init {
        Log.d("MyLog", "INIT VIEW MODEL")
        loadUsers()
    }

    fun loadUsers() {
        Log.d("MyLog", "loadUsers")
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result =  userRepository.getAll() ) {
                is Either.Left -> _state.value = _state.value.copy(
                    isLoading = false, exceptionMessage = result.value.toString()
                )
                is Either.Right -> _state.value = _state.value.copy(
                    isLoading = false, mutableUsers = result.value
                )
            }
        }
    }

    fun updateNewUser(newUser: User?) {

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val resultList = _state.value.mutableUsers?.toMutableList() ?: mutableListOf()
            if (newUser != null) {
                resultList.add(newUser)
            }
            _state.value = _state.value.copy(
                selectedUser = null, mutableUsers = resultList, isLoading = false
            )
        }
    }

    fun getById(userId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = async { userRepository.getById(userId) }.await()) {
                is Either.Left -> _state.value = _state.value.copy(
                    isLoading = false, exceptionMessage = result.value.toString()
                )

                is Either.Right -> {
                    val resultList = _state.value.mutableUsers?.toMutableList() ?: mutableListOf()
                    val user = result.value?.collectionModel?.embedded?.users?.first()
                    delay(500)
                    if (user != null) {
                        resultList.add(user)
                    }
                    _state.value = _state.value.copy(
                        selectedUser = null, mutableUsers = resultList.toList(), isLoading = false
                    )
                }
            }
        }
    }


    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = async { userRepository.deleteById(userId) }.await()) {
                is Either.Left -> _state.value = _state.value.copy(
                    isLoading = false, exceptionMessage = result.value.toString()
                )

                is Either.Right -> {
                    val resultList = _state.value.mutableUsers?.toMutableList() ?: mutableListOf()
                    resultList.removeIf { it.id == userId }
                    _state.value = _state.value.copy(
                        selectedUser = null, mutableUsers = resultList.toList(), isLoading = false
                    )
                }
            }
        }
    }

    fun selectUser(user: User) {
        if (_state.value.selectedUser == null) {
            _state.value = _state.value.copy(selectedUser = user)
        } else {
            _state.value = _state.value.copy(selectedUser = null)
        }
    }

    fun showErrorSnackBar(
        state: UsersScreenState, scope: CoroutineScope, snackbarHostState: SnackbarHostState
    ) {
        if (state.exceptionMessage != null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.exceptionMessage!!,
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }
}