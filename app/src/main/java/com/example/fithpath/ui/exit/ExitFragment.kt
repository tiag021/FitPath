package com.example.fithpath.ui.exit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.fithpath.Login
import com.example.fithpath.R
import com.example.fithpath.ui.map.MapFragment
import java.io.File

class ExitFragment : Fragment() {

    //Botões
    private lateinit var logout: AppCompatButton
    private lateinit var cancel: AppCompatButton
    //Fragmento
    private lateinit var fragLogout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_exit, container, false)

        //inicialização de variáveis
        logout = rootView.findViewById(R.id.btnLogout)
        cancel = rootView.findViewById(R.id.btnCancel)
        fragLogout = rootView.findViewById(R.id.fragLogout)

        logout.setOnClickListener {
            //pede permissões de localização
            logout()
        }

        cancel.setOnClickListener {
            //não guarda a corrida
            fragLogout.isVisible = false
            startActivity(Intent(activity, MapFragment::class.java))

        }

        return rootView
    }

    private fun logout(){
        val file = File(activity!!.filesDir, "user.txt")
        if(file.isFile){
            file.delete()
        }

        startActivity(Intent(activity, Login::class.java))
    }
}