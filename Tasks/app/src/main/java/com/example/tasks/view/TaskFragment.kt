package com.example.tasks.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tasks.adapters.TaskItemAdapter
import com.example.tasks.view_models.TaskDatabase
import com.example.tasks.view_models.TasksViewModel
import com.example.tasks.view_models.TasksViewModelFactory
import com.example.tasks.databinding.FragmentTaskBinding



class TasksFragment : Fragment() {
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel = initDataBaseAndTasksViewModel()
        binding.tasksViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner //Назначаем владельца жизненного цикла макета, чтобы он мог реагировать на обновления данных Live Data.

        val adapter = TaskItemAdapter {
            taskId ->
            viewModel.onTaskClicked(taskId)
        }
        binding.tasksList.adapter = adapter


        viewModel.tasks.observe(viewLifecycleOwner, Observer { it?.let {
            adapter.submitList(it)
        }})

        viewModel.navigateToTask.observe(viewLifecycleOwner, Observer { taskId -> taskId?.let {
            val action = TasksFragmentDirections.actionTasksFragmentToEditTaskFragment(taskId)
            this.findNavController().navigate(action)
            viewModel.onTaskNavigated()
        }})

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDataBaseAndTasksViewModel() : TasksViewModel {
        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        return ViewModelProvider(
            this, viewModelFactory).get(TasksViewModel::class.java)

    }
}