package com.example.quishcatcher.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.quishcatcher.R
import com.example.quishcatcher.SessionManager
import com.example.quishcatcher.SessionManager.get
import com.example.quishcatcher.SessionManager.set
import com.example.quishcatcher.api.*
import com.example.quishcatcher.databinding.FragmentHomeBinding
import com.example.quishcatcher.getGreetingMessage
import com.example.quishcatcher.makeToken
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var binding: FragmentHomeBinding

    lateinit var pref: SharedPreferences
    val gson = Gson()
    lateinit var token: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        pref = SessionManager.sessionPrefs(requireContext())

        val name = pref[Constants.SharedPref.USER_NAME, ""].toString().replace("\"", "")
        token = pref[Constants.SharedPref.TOKEN, ""].toString()
        binding.tvName.text = "${getGreetingMessage()}\n$name"

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
            performApiCall()
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


    private fun performApiCall() {
        val api = RetrofitHelper.getInstance().create(APIInterface::class.java)
        LoadingDialog.showProgress(requireActivity())
        api.logout(token.makeToken()).enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>, response: Response<LogoutResponse>
            ) {
                LoadingDialog.dismissProgress()

                if (response.isSuccessful) {
                    Toasty.success(
                        requireContext(),
                        "${response.body()?.message}",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                    performLogout()
                } else {
                    Toasty.error(
                        requireContext(),
                        "Logout Failed. Please try again.",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                LoadingDialog.dismissProgress()
                Toasty.error(
                    requireContext(),
                    "Something went wrong. Please try again",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }

        })
    }
}