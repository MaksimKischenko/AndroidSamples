package com.example.jobserviceapp

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


class CountingJobService : JobService() {
    init {
        Log.d("MyLog", "INITED: ${Thread.currentThread().name}")
    }
    private var count = 0
    private var isJobRunning = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("MyLog", "onStartJob: ${Thread.currentThread().name}")
        count++
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        isJobRunning = false
        return true
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountingScreen()
        }

    }
}


@Composable
fun CountingScreen() {
    val flexMillis = 59 * 60 * 1000L
    val extras = PersistableBundle()
    extras.putInt("task", 22158)
    val jobScheduler = LocalContext.current.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    val jobInfo = JobInfo.Builder(1, ComponentName(LocalContext.current, CountingJobService::class.java))
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        .setPeriodic(    60 * 60 * 1000, flexMillis)
        .setExtras(extras)
        .build()
    jobScheduler.schedule(jobInfo)

}

@Preview
@Composable
fun PreviewCountingScreen() {
    CountingScreen()
}