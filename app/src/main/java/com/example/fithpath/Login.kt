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
import java.io.File
import java.io.FileOutputStream

class Login : AppCompatActivity() {

    //Definir o binding da Atividade
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
        // Se o utlizador não tiver conta, tem a opção de mudar para a view de registo
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

    //Obter e validar os inputs introduzidos pelo utilizador
    private fun getInputs(){
        val email = binding.lgEmail.text.toString()
        val password = binding.lgPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            //Iniciar sessão com os valores obtidos na API
            loginUser(email, password)
        }else{
            showMessage("Ambos os campos têm que estar preenchidos")
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun loginUser(email: String, password: String){
        val loginRequest = LoginRequest(email, password)

        //Enviar o email e password para a API e obter resposta
        val apiCall = ApiClient.getApiService().loginUser(loginRequest)
        apiCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    //Guardar o nome e Token obtidos pela API em variável
                    val name = response.body()!!.data.name
                    val token = response.body()!!.data.token

                    val arraylist = ArrayList<String>()
                    arraylist.add(name)
                    arraylist.add(token)

                    //Salvar o nome e token em ficheiro user.txt
                    val directory: File = applicationContext.filesDir
                    val file = File(directory, "user.txt")
                    val fo = FileOutputStream(file, true)
                    fo.write(arraylist.toString().toByteArray())
                    fo.close()

                    moveToMainActivity()
                }else{
                    showMessage("Não foi possivel iniciar sessão. Verifique a sua password")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }

        })
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, Maps::class.java))
    }

}