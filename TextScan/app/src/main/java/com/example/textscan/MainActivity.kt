package com.example.textscan

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.ImageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.textscan.ui.theme.TextScanTheme
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MainActivity : ComponentActivity() {
    private val REQUEST_CODE_READ_CONTACTS = 1
    private var READ_CONTACTS_GRANTED = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            READ_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_CODE_READ_CONTACTS
            );
        }
        startLocationPermissionRequest()
        setContent {
            TextScanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageGalleryScreenPreview()
                }
            }
        }
    }





    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("MyLog", "PERMISSION GRANTED")
            val DATA_PATH: String  = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
            val TESSDATA: String  = "tessdata";
            prepareTesseract(DATA_PATH, TESSDATA, this)
        } else {
            Log.d("MyLog", "PERMISSION NOT GRANTED")
            // PERMISSION NOT GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
    }

}





@RequiresApi(Build.VERSION_CODES.P)
@Preview
@Composable
fun ImageGalleryScreenPreview() {
    ImageGalleryScreen(onImageSelected = {})
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageGalleryScreen(onImageSelected: (Bitmap) -> Unit) {
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var recText by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        try {
            uri?.let {
                val contentResolver: ContentResolver = context.contentResolver
                val fileSource = ImageDecoder.createSource(contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(
                    fileSource
                ) { decoder: ImageDecoder, _: ImageInfo?, _: ImageDecoder.Source? ->
                    decoder.isMutableRequired = true
                }
                selectedImageBitmap = bitmap
                onImageSelected(bitmap)
                recText = recognizeTextFromImage(selectedImageBitmap!!)
                Log.d("MyLog", "TEXT: $recText")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedImageBitmap != null) {
            Image(
                bitmap = selectedImageBitmap!!.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.size(200.dp)
            )
        } else {
            Text(text = "Выберите изображение из галереи", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = recText)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { getContent.launch("image/*") }) {
            Text("Выбрать изображение")
        }
    }
}



private fun prepareTesseract(
    dataPath:String,
    tessData:String,
    context: Context
) {
    try {

        prepareDirectory(dataPath + tessData)
        Log.d("MyLog", "prepareTesseract")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("MyLog", " ERROR:$e")
    }
    copyTessDataFiles(dataPath, tessData, context)
}

private fun copyTessDataFiles(
    dataPath:String,
    tessData: String,
    context: Context
) {
    try {
        val fileList = context.assets.list(tessData)

        for (fileName in fileList.orEmpty()) {
            // open file within the assets folder
            // if it is not already there copy it to the sdcard
            val pathToDataFile = "$dataPath$tessData/$fileName"
            if (!File(pathToDataFile).exists()) {

                context.assets.open("$tessData/$fileName").use { inStream ->
                    FileOutputStream(pathToDataFile).use { outStream ->

                        // Transfer bytes from in to out
                        val buf = ByteArray(1024)
                        var len: Int

                        while (inStream.read(buf).also { len = it } > 0) {
                            outStream.write(buf, 0, len)
                        }
                    }
                }

                Log.d("MyLog", "Copied $fileName to tessdata")
            }
        }
    } catch (e: IOException) {
        Log.e("MyLog", "Unable to copy files to tessdata ${e.toString()}")
    }
}


private fun prepareDirectory(path: String) {
    val dir = File(path)
    if (!dir.exists()) {
        if (!dir.mkdirs()) {
            Log.d(
                "MyLog",
                "ERROR: Creation of directory $path failed, check does Android Manifest have permission to write to external storage."
            )
        }
    } else {
        Log.d("MyLog", "Created directory $path")
    }
}


fun recognizeTextFromImage(image: Bitmap): String {
    val DATA_PATH: String  = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
    val tessBaseApi = TessBaseAPI()
    tessBaseApi.init(DATA_PATH, "eng")

    tessBaseApi.setImage(image)
    val recognizedText = tessBaseApi.utF8Text

    tessBaseApi.end()

    return recognizedText
}

