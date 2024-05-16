package com.example.projectshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projectshop.R
import com.example.projectshop.data.DbHelper
import com.example.projectshop.models.User
import com.google.android.material.snackbar.Snackbar

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button : Button = findViewById(R.id.button_reg)

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(login == "" || password == "") {
                val snack = Snackbar.make(it, "Введите необходимые данные", Snackbar.LENGTH_LONG)
                snack.show()
            } else {
                val user = User(login, email, password)
                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()
                userLogin.text.clear()
                userEmail.text.clear()
                userPassword.text.clear()
            }
        }

        val linkToReg: TextView = findViewById(R.id.auth_to_reg_from_auth)
        linkToReg.setOnClickListener{
            val intent = Intent(this, AuthActivity :: class.java)
            startActivity(intent)
        }
    }
}