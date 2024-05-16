package com.example.roomdb

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras

import com.example.roomdb.data.MainDb
import com.example.roomdb.data.NameEntity
import kotlinx.coroutines.launch

//тут нет контекста и не может быть - так как после разрушения активити сама модел выживает!!!
//Поэтому создаем фабрику
class MainViewModel(val database: MainDb) : ViewModel() {
    var nameEntity: NameEntity? = null
    val textState = mutableStateOf("")
    val itemsList = database.dao.getAll()

    fun insertItem() = viewModelScope.launch {
        val newItem = nameEntity?.copy(name = textState.value)
            ?: NameEntity(name = textState.value)
        database.dao.insertItem(newItem)
        nameEntity = null
        textState.value = ""
    }

    fun deleteItem(item:NameEntity) = viewModelScope.launch {
        database.dao.delete(item)
    }

    @Suppress("UNCHECKED_CAST")
    companion object{
        val factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return MainViewModel(database = database) as T
            }
        }
    }
}