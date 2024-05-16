package com.example.tasks.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope

import com.example.tasks.models.Task
import com.example.tasks.models.TaskDao
import kotlinx.coroutines.launch



class TasksViewModel(val dao: TaskDao) : ViewModel(){
    var newTaskName = ""

    val tasks = dao.getAll()

    private val _navigateToTask = MutableLiveData<Long?>()

    val navigateToTask: LiveData<Long?>
        get() = _navigateToTask

    fun addTask() {
        viewModelScope.launch {
            val task = Task()
            task.taskName = newTaskName
            dao.insert(task)
        }
    }

    fun onTaskClicked(taskId: Long) {
        _navigateToTask.value = taskId
    }

    fun onTaskNavigated() {
        _navigateToTask.value = null
    }
}