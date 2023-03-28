package com.example.quishcatcher.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.quishcatcher.R
import com.example.quishcatcher.SessionManager
import com.example.quishcatcher.SessionManager.set
import com.example.quishcatcher.api.*
import com.example.quishcatcher.databinding.FragmentRegistrationBinding
import com.example.quishcatcher.hideKeyboard
import com.example.quishcatcher.isValidEmail
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding get() = _binding!!

    lateinit var pref: SharedPreferences
    val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistrationBinding.bind(view)
        pref = SessionManager.sessionPrefs(requireContext())
        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            hideKeyboard()

            if (name.isEmpty()) {
                binding.etName.error = "Name required"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.etEmail.error = "Email address required"
                return@setOnClickListener
            }

            if (!email.isValidEmail()) {
                binding.etEmail.error = "Provide valid email address"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password required"
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Confirm password required"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Confirm password does not match"
                return@setOnClickListener
            }

            performApiCall(RegistrationRequest(name, email, password))
        }

        binding.ivClose.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.txtSignUp.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
    }


    private fun performApiCall(payload: RegistrationRequest) {
        val api = RetrofitHelper.getInstance().create(APIInterface::class.java)
        LoadingDialog.showProgress(requireActivity())
        api.register(payload).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                LoadingDialog.dismissProgress()

                if (response.isSuccessful) {
                    pref[Constants.SharedPref.TOKEN] = response.body()?.token
                    pref[Constants.SharedPref.IS_LOGIN] = true
                    pref[Constants.SharedPref.USER_PROFILE] = gson.toJson(response.body()?.user)
                    pref[Constants.SharedPref.USER_NAME] = gson.toJson(response.body()?.user?.name?: "")
                    Navigation.findNavController(binding.root)
                        .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment())
                    Toasty.success(
                        requireContext(),
                        "Login successful",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                } else {
                    Toasty.error(
                        requireContext(),
                        "Login Failed. Please try again.",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}