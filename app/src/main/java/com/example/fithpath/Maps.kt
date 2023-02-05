package com.example.fithpath
/*
Fontes:
alterar o texto no navheader: https://www.youtube.com/watch?v=1f4CvPvwvZ4
 */
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fithpath.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class Maps : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var notLoginFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (notLoginFlag) {
            moveToLogin()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_map, R.id.nav_runs, R.id.nav_exit, R.id.nav_about_us
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //manter o ecrã sempre ligado
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        updateNavHeader()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java))
    }

    private fun updateNavHeader() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(R.id.usrName) as TextView

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
                val statsList = ArrayList(values)

                val nome = statsList[0]

                navUsername.text = nome

            } catch (e: FileNotFoundException) {

            }
        }
    }
}