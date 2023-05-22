package com.example.thesisapp.Fragments_MainMenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thesisapp.databinding.FragmentFaccountBinding

class Faccount : Fragment() {

    private lateinit var binding: FragmentFaccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaccountBinding.inflate(inflater,container,false)


        return binding.root
    }
}