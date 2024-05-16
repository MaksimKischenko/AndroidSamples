package com.example.retrofitapp.view_models


import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.retrofitapp.remote_models.User
import com.example.retrofitapp.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch



class UsersViewModelLiveData(private val userRepository: UserRepository) : ViewModel() {

    private var _users = MutableLiveData<List<User>?>()
    val users: LiveData<List<User>?>
        get() = _users

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _selectedUser = MutableLiveData<User?>()
    val selectedUser: LiveData<User?>
        get() = _selectedUser

    private var _exceptionMessage = MutableLiveData<String?>()



    init {
        Log.d("MyLog", "INIT LIVE DATA")
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = async { userRepository.getAll() }.await()) {
                is Either.Left -> _exceptionMessage.value =  result.value.toString()
                is Either.Right ->{
                    _isLoading.value = false
                    _users.value = result.value
                }
            }
        }
    }

    fun updateNewUser(newUser: User?) {
        viewModelScope.launch {
            _isLoading.value = true
            val resultList = _users.value?.toMutableList()?: mutableListOf()
            if (newUser != null) {
                resultList.add(newUser)
            }
            _users.value = resultList.toList()
            _selectedUser.value = null
            _isLoading.value = false

        }
    }

    fun getById(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = async { userRepository.getById(userId) }.await()) {
                is Either.Left -> _exceptionMessage.value =  result.value.toString()
                is Either.Right -> {
                    val resultList = _users.value?.toMutableList()?: mutableListOf()
                    val user = result.value?.collectionModel?.embedded?.users?.first()
                    if(user != null) {
                        if(!resultList.contains(user)) {
                            resultList.add(user)
                        }
                    }
                    _selectedUser.value = null
                    _isLoading.value = false
                    _users.value = resultList.toList()
                }
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = async { userRepository.deleteById(userId) }.await()) {
                is Either.Left -> _exceptionMessage.value =  result.value.toString()
                is Either.Right -> {
                    val resultList = _users.value?.toMutableList()?: mutableListOf()
                    resultList.removeIf { it.id == userId }
                    _selectedUser.value = null
                    _isLoading.value = false
                    _users.value = resultList.toList()
                }
            }
        }
    }

    fun selectUser(user: User) {
        viewModelScope.launch {
            if (_selectedUser.value == null) {
                _selectedUser.value = user
            } else {
                _selectedUser.value = null
            }
        }
    }

    fun showErrorSnackBar(
        scope: CoroutineScope, snackbarHostState: SnackbarHostState
    ) {
        if (_exceptionMessage.value != null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = _exceptionMessage.value?:"Неизвестная ошибка",
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }
}

