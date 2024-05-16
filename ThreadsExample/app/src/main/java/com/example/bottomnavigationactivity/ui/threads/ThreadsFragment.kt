package com.example.bottomnavigationactivity.ui.threads

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bottomnavigationactivity.databinding.FragmentThreadsExampleBinding
import java.text.SimpleDateFormat
import java.util.*

class ThreadsFragment : Fragment() {

    private var _binding: FragmentThreadsExampleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var count: Int = 0
    var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(ThreadsViewModel::class.java)
        _binding = FragmentThreadsExampleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        textView = binding.textHome

//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView?.text = it
//        }

        val buttonThreadController: Button = binding.firstThread
        val handler = Looper.myLooper()?.let { Handler(it) }

        buttonThreadController.setOnClickListener {
            handler?.post(object :Runnable {
                override fun run() {
                    val dateFormat = SimpleDateFormat("HH:mm:ss")
                    val activeTime = dateFormat.format(Date())
                    textView?.text = activeTime
                    handler.postDelayed( this,1000)
                }
            })
        }

        val progressBar : ProgressBar = binding.progressBar
        val buttonSecondThreadController : Button = binding.secondThread
        val handler2 = Looper.myLooper()?.let { Handler(it) } //Looper.getMainLooper()



        buttonSecondThreadController.setOnClickListener {
            handler2?.post(object :Runnable {
                override fun run() {
                    handler2.post {
                        count +=10;
                        progressBar.progress = count
                        if (count == 100)
                            count = 0
                    }
                    handler2?.postDelayed(this, 1000)
                }
            })
        }
//        buttonSecondThreadController.setOnClickListener {
//            val r: Runnable = object : Runnable {
//                override fun run() {
//                    handler2?.post {
//                        count ++;
//                        progressBar.progress = count
//                    }
//                    handler2?.postDelayed(this, 1000)
//                }
//            }
//            handler2?.postDelayed(r, 1000)
//        }

        return root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putString("time", textView?.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            textView?.text = savedInstanceState.getString("time")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


