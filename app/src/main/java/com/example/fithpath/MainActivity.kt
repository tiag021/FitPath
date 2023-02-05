package com.example.fithpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        startActivity(Intent(this, Login::class.java))
        Toast.makeText(this, "Passou pelo Main",Toast.LENGTH_SHORT).show()
    }
}