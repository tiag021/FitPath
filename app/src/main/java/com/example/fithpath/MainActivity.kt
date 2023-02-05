package com.example.fithpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val file: File = File(filesDir, "user.txt")
        val content = ByteArray(file.length().toInt())
        if (file.isFile) {
            try {
                //obtem o ficheiro de dados sobre as corridas
                val fi: FileInputStream = FileInputStream(file)
                fi.read(content)
                fi.close()

                //guardar o content numa string
                var s: String = String(content)
                //retirar os []
                s = s.substring(1, s.length - 1)
                //separar os valores
                val values = s.split(", ")
                //guarda-los num array
                val statsList = ArrayList(values)

                if(statsList[1].isNotEmpty()){
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