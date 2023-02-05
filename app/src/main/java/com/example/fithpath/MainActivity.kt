package com.example.fithpath

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val file = File(filesDir, "user.txt")
        val content = ByteArray(file.length().toInt())
        if (file.isFile) {
            try {
                //obtem o ficheiro de dados sobre os utilizadores
                val fi = FileInputStream(file)
                fi.read(content)
                fi.close()

                //guarda o content numa string
                var s = String(content)
                //retira os []
                s = s.substring(1, s.length - 1)
                //separa os valores
                val values = s.split(", ")
                //guarda-os num array
                val userList = ArrayList(values)
                val token = userList[1]

                if(token.isNotEmpty()){
                    startActivity(Intent(this, Maps::class.java))
                }else{
                    startActivity(Intent(this, Login::class.java))
                }

            } catch (e: FileNotFoundException) {

            }
        }else{
            startActivity(Intent(this, Login::class.java))
        }
    }
}