package com.example.codsofttodo.ui.get_started

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.codsofttodo.R
import com.example.codsofttodo.databinding.FragmentGetStartedBinding

class GetStartedFragment : Fragment() {
    private lateinit var binding: FragmentGetStartedBinding
    private lateinit var sharedPref:SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_get_started, container, false)

        sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (sharedPref.getString("isFirstTime","no") == "yes"){
            findNavController().navigate(R.id.action_getStartedFragment_to_homeFragment)
        }
        
        binding.getStartBtn.setOnClickListener {
            editor.putString("isFirstTime","yes").apply()
            Navigation.findNavController(it).navigate(R.id.action_getStartedFragment_to_homeFragment)
        }

        return binding.root
    }
}