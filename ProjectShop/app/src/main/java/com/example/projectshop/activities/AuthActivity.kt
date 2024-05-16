package com.example.projectshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.projectshop.R
import com.example.projectshop.data.DbHelper
import com.google.android.material.snackbar.Snackbar

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val linkToReg: TextView = findViewById(R.id.auth_to_reg)
        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPassword: EditText = findViewById(R.id.user_password_auth)
        val button : Button = findViewById(R.id.button_reg_auth)

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(login == "" || password == "") {
                val snack = Snackbar.make(it, "Введите необходимые данные", Snackbar.LENGTH_LONG)
                snack.show()
            } else {
                val db = DbHelper(this, null)
                val isAuth = db.getUser(login, password)

                if(isAuth) {
                    val snack = Snackbar.make(it, "Успешно", Snackbar.LENGTH_LONG)
                    snack.show()
                    val intent = Intent(this, ItemsActivity :: class.java)
                    startActivity(intent)
                } else {
                    val snack = Snackbar.make(it, "Пользователь ${userLogin.text} не авторизован", Snackbar.LENGTH_LONG)
                    snack.show()
                }
            }
        }
        linkToReg.setOnClickListener{
            val intent = Intent(this, RegistrationActivity :: class.java)
            startActivity(intent)
        }
    }
}