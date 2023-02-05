package com.example.fithpath.ui.exit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fithpath.Login
import com.example.fithpath.Maps
import com.example.fithpath.R
import com.example.fithpath.databinding.FragmentExitBinding
import com.example.fithpath.ui.map.MapFragment
import java.io.File
import kotlin.math.log

class ExitFragment : Fragment() {

    private var _binding: FragmentExitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Botões
    private lateinit var logout: AppCompatButton
    private lateinit var cancel: AppCompatButton
    private lateinit var fragLogout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExitBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //inicialização de variáveis
        logout = root.findViewById(R.id.btnLogout)
        cancel = root.findViewById(R.id.btnCancel)
        fragLogout = root.findViewById(R.id.fragLogout)

        logout.setOnClickListener {
            //pede permissões de localização
            logout()
        }

        cancel.setOnClickListener {
            //não guarda a corrida
            fragLogout.isVisible = false
            startActivity(Intent(activity, MapFragment::class.java))

        }

        return root
    }

    fun logout(){
        val file: File = File(activity!!.filesDir, "user.txt")
        if(file.isFile){
            file.delete()
        }

        startActivity(Intent(activity, Login::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}