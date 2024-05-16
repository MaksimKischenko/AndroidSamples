package com.example.systemservicesapp

import android.accounts.AccountManager
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.systemservicesapp.ui.theme.SystemServicesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemServicesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SystemServices(activity = this@MainActivity, onTap = {startLocationPermissionRequest()})
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("MyLog", "PERMISSION GRANTED")
            cameraSettings(
                this, this@MainActivity
            )
        } else {
            Log.d("MyLog", "PERMISSION NOT GRANTED")
            // PERMISSION NOT GRANTED
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

}

@Composable
fun SystemServices(
    context: Context = LocalContext.current,
    activity: Activity,
    onTap:() -> Unit
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(onClick = {
            locationSettings(
                context, activity
            )
        }) {
            Text(text = "locationSettings")
        }
        OutlinedButton(onClick = {
            accountSettings(
                context, activity
            )
        }) {
            Text(text = "accountSettings")
        }
        OutlinedButton(onClick = {
            activitySettings(
                context, activity
            )
        }) {
            Text(text = "activitySettings")
        }
        OutlinedButton(onClick = {
            onTap.invoke()
        }) {
            Text(text = "cameraSettings")
        }
        OutlinedButton(onClick = {
            batterySettings(context, activity)
        }) {
            Text(text = "batterySettings")
        }
    }
}


private fun locationSettings(context: Context, activity: Activity) {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        ActivityCompat.startActivity(context, Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), null)
    } else {

    }
}


private fun accountSettings(context: Context, activity: Activity) {
    val manager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
    Toast.makeText(context, "ACCOUNTS: ${manager.accounts}", Toast.LENGTH_LONG).show()
}

private fun activitySettings(context: Context, activity: Activity) {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningProcesses = manager.runningAppProcesses
    for (processInfo in runningProcesses) {
        Toast.makeText(context, "Process Name: ${processInfo.processName}, " +
                "\nImportance: ${processInfo.importance}", Toast.LENGTH_LONG).show()
//        manager.killBackgroundProcesses(processInfo.processName)

        val memoryInfo = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(memoryInfo)
        Log.d("MyLog", "Total Memory: ${memoryInfo.totalMem}")
    }

    val runningTasks = manager.getRunningTasks(1)
    val currentActivity = runningTasks[0].topActivity
    Toast.makeText(context, "Current Activity: $currentActivity", Toast.LENGTH_LONG).show()


    val runningServices = manager.getRunningServices(Int.MAX_VALUE)
    for (serviceInfo in runningServices) {
        Log.d("MyLog", "Service Name: ${serviceInfo.service.className}")
    }

    val processInfo = manager.getProcessMemoryInfo(intArrayOf(android.os.Process.myPid()))
    Log.d("MyLog", "Total PSS: ${processInfo[0].totalPss}")
}


private fun batterySettings(context: Context, activity: Activity) {
    val manager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    manager.isCharging
    manager.getIntProperty(1)
    Toast.makeText(context, "manager.isCharging: ${manager.isCharging}", Toast.LENGTH_LONG).show()
}



private fun cameraSettings(context: Context, activity: Activity) {
    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = manager.cameraIdList[0] // Получаем идентификатор камеры
        // Открываем камеру

        ActivityCompat.startActivity(context, Intent(MediaStore.ACTION_IMAGE_CAPTURE), null)


        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                // Камера открыта, можно выполнять дальнейшие действия
                Log.d("MyLog", "Camera: ${camera.id}")
            }

            override fun onDisconnected(camera: CameraDevice) {
                Log.d("MyLog", "onDisconnected ${camera}")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                Log.d("MyLog", "onError ${error}")
            }
        }, null)
    } else {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA), 1)
    }
}