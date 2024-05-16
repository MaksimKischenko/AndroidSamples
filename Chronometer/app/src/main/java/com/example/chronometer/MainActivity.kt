package com.example.chronometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import com.example.chronometer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var stopwatch: Chronometer
    var running = false
    var offset: Long = 0;
    private val OFFSET_KEY = "offset"
    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.d("MyLog", "onSaveInstanceState")
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

//    override fun onStop() {
//        super.onStop()
//        Log.d("MyLog", "onStop")
//        if (running) {
//            saveOffset()
//            stopwatch.stop()
//        }
//    }

    //    override fun onRestart() {
//        super.onRestart()
//        Log.d("MyLog", "onRestart")
//        if (running) {
//            setBaseTime()
//            stopwatch.start()
//            offset = 0
//        }
//    }

//    Если снова взглянуть на диаграмму жизненного цикла, методы onPause() и onResume(),
//    помимо методов onStop(), onStart() и onRestart(), вызываются при каждой остановке и перезапуске активности.
//    Так как мы хотим, чтобы приложение вело себя оди- наково независимо от того,
//    приостанавливается активность или останавливается, можно воспользоваться методами onPause() и onResume()
//    в обеих ситуациях.

    override fun onPause() {
        super.onPause()
        Log.d("MyLog", "onPause")
        if (running) {
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyLog", "onResume")
        if (running) {
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MyLog", "onStart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        stopwatch = findViewById(R.id.stopwatch)

        if(savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }


        binding.startButton.setOnClickListener {
            if(!running) {
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }


        binding.pauseButton.setOnClickListener {
            if(running) {
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }


        binding.resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }


    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}

