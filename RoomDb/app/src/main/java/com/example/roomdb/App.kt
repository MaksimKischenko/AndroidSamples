package com.example.roomdb

import android.app.Application
import com.example.roomdb.data.MainDb

class App : Application() { //прописываем как инициализируем ДБ 1 раз и не пересоздвать
    val database by lazy { MainDb.createDataBase(this) }
}