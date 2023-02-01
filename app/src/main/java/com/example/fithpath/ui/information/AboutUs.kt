package com.example.fithpath.ui.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fithpath.R
import com.example.fithpath.databinding.FragmentAboutUsBinding

class AboutUs : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}