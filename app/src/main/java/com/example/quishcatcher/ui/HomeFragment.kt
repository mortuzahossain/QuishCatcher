package com.example.quishcatcher.ui

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.quishcatcher.R

class HomeFragment : Fragment(R.layout.fragment_home) {

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

    }
}