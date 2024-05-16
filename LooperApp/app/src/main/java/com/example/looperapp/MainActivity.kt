package com.example.looperapp

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.looperapp.ui.theme.LooperAppTheme
import java.util.Timer
import java.util.TimerTask

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("MyLog", "MAIN: ${Thread.currentThread().name}")
        setContent {
            LooperAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
) {
    val countState = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        runHandlerTimer(countState)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "${countState.intValue}")
    }
}

private fun runLopperTimer() {
    val looperThread = Thread {
        Looper.prepare()
        val looperHandler = Handler(Looper.myLooper()!!)

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                looperHandler.post {
                    Log.d("MyLog", "LOOPER: ${Thread.currentThread().name}")
                }
            }
        }, 0, 3000)

        Looper.loop()
    }
    looperThread.start()
}

private fun runHandlerTimer(countState: MutableIntState) {
    val handlerThread = HandlerThread("MyHandlerThread")
    handlerThread.start()
    val handler = Handler(handlerThread.looper) //Looper.getMainLooper()
    val timer = Timer()
    timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            handler.post {
                countState.intValue += 1
                Log.d("MyLog", "HANDLER: ${Thread.currentThread().name}")
            }
        }
    }, 0, 3000)
}

private fun runSimpleTimer(countState: MutableIntState) {
    val timer = Timer()
    timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            countState.intValue += 1
            Log.d("MyLog", "HANDLER: ${Thread.currentThread().name}")
        }
    }, 0, 3000)
}
