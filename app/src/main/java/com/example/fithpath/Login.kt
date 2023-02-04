package com.example.fithpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fithpath.client.ApiClient
import com.example.fithpath.databinding.ActivityLoginBinding
import com.example.fithpath.models.request.LoginRequest
import com.example.fithpath.models.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        initData()
    }

    private fun initData(){
        clickListener()
    }

    private fun clickListener(){
        binding.tvRegister.setOnClickListener{
            moveToRegister()
        }
        binding.btnLogin.setOnClickListener{
            getInputs()
        }
    }

    private fun moveToRegister(){
        startActivity(Intent(this, Register::class.java))
    }

    private fun getInputs(){
        val email = binding.lgEmail.text.toString()
        val password = binding.lgPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            loginUser(email, password)
        }else{
            showMessage("")
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun loginUser(email: String, password: String){
        val loginRequest = LoginRequest(email, password)

        val apiCall = ApiClient.getApiService().loginUser(loginRequest)
        apiCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    moveToMainActivity()
                }else{
                    showMessage("Não foi possivel iniciar sessão. Verifique a sua password")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

}