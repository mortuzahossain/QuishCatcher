package com.example.quishcatcher.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.quishcatcher.R
import com.example.quishcatcher.SessionManager
import com.example.quishcatcher.SessionManager.set
import com.example.quishcatcher.api.*
import com.example.quishcatcher.databinding.FragmentLoginBinding
import com.example.quishcatcher.hideKeyboard
import com.example.quishcatcher.isValidEmail
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    lateinit var pref: SharedPreferences
    val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        pref = SessionManager.sessionPrefs(requireContext())

        if (pref.getBoolean(Constants.SharedPref.IS_LOGIN, false)) {
            Navigation.findNavController(binding.root)
                .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }


        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            hideKeyboard()

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

            performApiCall(LoginRequest(email, password))

        }

        binding.txtSignUp.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

        binding.tvForgetPassword.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performApiCall(payload: LoginRequest) {
        val api = RetrofitHelper.getInstance().create(APIInterface::class.java)
        LoadingDialog.showProgress(requireActivity())
        api.login(payload).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                LoadingDialog.dismissProgress()

                if (response.isSuccessful) {
                    pref[Constants.SharedPref.TOKEN] = response.body()?.token
                    pref[Constants.SharedPref.IS_LOGIN] = true
                    pref[Constants.SharedPref.USER_PROFILE] = gson.toJson(response.body()?.user)
                    pref[Constants.SharedPref.USER_NAME] = gson.toJson(response.body()?.user?.name)
                    Navigation.findNavController(binding.root)
                        .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
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
}