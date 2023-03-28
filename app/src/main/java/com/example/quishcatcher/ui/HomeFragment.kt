package com.example.quishcatcher.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.quishcatcher.R
import com.example.quishcatcher.SessionManager
import com.example.quishcatcher.SessionManager.get
import com.example.quishcatcher.SessionManager.set
import com.example.quishcatcher.api.Constants
import com.example.quishcatcher.databinding.FragmentHomeBinding
import com.example.quishcatcher.getGreetingMessage
import com.google.gson.Gson

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var binding: FragmentHomeBinding

    lateinit var pref: SharedPreferences
    val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        pref = SessionManager.sessionPrefs(requireContext())

        val name = pref[Constants.SharedPref.USER_NAME, ""]
        binding.tvName.text = "${getGreetingMessage()}\n${name?.replace("\"","")}"

        binding.imgLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Warning!!")
        builder.setMessage("Are you sure want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.cancel()
            performLogout()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun performLogout() {
        pref[Constants.SharedPref.IS_LOGIN] = false
        pref[Constants.SharedPref.USER_NAME] = null
        pref[Constants.SharedPref.USER_PROFILE] = null
        requireActivity().finish()
    }
}