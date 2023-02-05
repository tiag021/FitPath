package com.example.fithpath

import  android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fithpath.client.ApiClient
import com.example.fithpath.databinding.ActivityRegisterBinding
import com.example.fithpath.models.request.RegisterRequest
import com.example.fithpath.models.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        initData()
    }

    private fun initData(){
        clickListener()
    }

    private fun clickListener(){
        binding.tvLogin.setOnClickListener{
            moveToLogin()
        }
        binding.btnRegister.setOnClickListener{
            getInputs()
        }
    }

    private fun moveToLogin(){
        startActivity(Intent(this, Login::class.java))
    }

    private fun getInputs(){
        val name = binding.rgName.text.toString()
        val email = binding.rgEmail.text.toString()
        val password = binding.rgPassword.text.toString()
        val confirmPassword = binding.rgConfirmPassword.text.toString()

        if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
            if(password == confirmPassword){
                //Registo
                registerUser(name, email, password, confirmPassword)
            }else{
                showMessage("As passwords não combinam")
            }
        }else{
            showMessage("Preencher todos os campos")
        }
    }

    private fun registerUser(name: String, email: String, password: String, confirmPassword: String){
        val registerRequest = RegisterRequest(name, email, password, confirmPassword)

        val apiCall = ApiClient.getApiService().registerUser(registerRequest)
        apiCall.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful){
                    moveToLogin()
                }else{
                    showMessage("Ocorreu um erro ao registar")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showMessage("Ocorreu um erro " + t.localizedMessage)
            }

        })
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}