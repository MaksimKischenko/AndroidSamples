package com.example.projectshop.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projectshop.R


class ScrollListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_list)

        val userData: EditText = findViewById(R.id.user_data)
        val button : Button = findViewById(R.id.button)

        val todos : MutableList<String> = mutableListOf()
        val listView: ListView = findViewById(R.id.list_view)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todos)
        listView.adapter = adapter

        button.setOnClickListener {
            val text = userData.text.toString().trim()
            if(text != "") {
                adapter.insert(text, 0)
                userData.text = null
            }
        }

        listView.setOnItemClickListener{adapterView, view, i, l ->
            val text = listView.getItemAtPosition(i).toString()
            adapter.remove(text)
            Toast.makeText(this, "Мы удалили $text", Toast.LENGTH_LONG).show()
        }
    }
}