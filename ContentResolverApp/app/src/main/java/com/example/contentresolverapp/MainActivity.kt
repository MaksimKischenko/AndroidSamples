package com.example.contentresolverapp

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contentresolverapp.ui.theme.ContentResolverAppTheme

class MainActivity : ComponentActivity() {
    private val REQUEST_CODE_READ_CONTACTS = 1
    private var READ_CONTACTS_GRANTED = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            READ_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            );
        }
        startLocationPermissionRequest()


        setContent {

            ContentResolverAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    if (READ_CONTACTS_GRANTED) {
                        getContacts(LocalContext.current)
                    }
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("MyLog", "PERMISSION GRANTED")
            // PERMISSION GRANTED
        } else {
            Log.d("MyLog", "PERMISSION NOT GRANTED")
            // PERMISSION NOT GRANTED
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }




    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ContentResolverAppTheme {
            Greeting("Android")
        }
    }


    private fun getCalls(context: Context) {
        val callLogUri = CallLog.Calls.CONTENT_URI
        val cursor = contentResolver.query(callLogUri, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if(cursor.getColumnIndex(CallLog.Calls.NUMBER) != -1) {
                    val index = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                    val callNumber = cursor.getString(index)
                    val callType = cursor.getInt(index)
                }
            }
            cursor.close()
        }
    }

    private fun getContacts(context: Context) {
        val contacts = ArrayList<String>()
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {

                // получаем каждый контакт
                val displayNameIndex =
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                val contact = if (displayNameIndex != -1) {
                    cursor.getString(displayNameIndex)
                } else {
                    "Нет данных"
                }
                // добавляем контакт в список
                contacts.add(contact)

            }
            cursor.close()
        }
        Log.d("MyLog", "INFO: $contacts")
    }
}

