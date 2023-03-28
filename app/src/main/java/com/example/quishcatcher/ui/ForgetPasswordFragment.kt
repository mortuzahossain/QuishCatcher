package com.example.quishcatcher.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.quishcatcher.R
import com.example.quishcatcher.api.*
import com.example.quishcatcher.databinding.FragmentForgetPasswordBinding
import com.example.quishcatcher.hideKeyboard
import com.example.quishcatcher.isValidEmail
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {
    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding: FragmentForgetPasswordBinding get() = _binding!!
    val gson = Gson()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentForgetPasswordBinding.bind(view)


        binding.ivClose.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()

            hideKeyboard()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email address required"
                return@setOnClickListener
            }
            if (!email.isValidEmail()) {
                binding.etEmail.error = "Provide valid email address"
                return@setOnClickListener
            }
            performApiCall(ForgetPasswordRequest(email))

        }

    }


    private fun performApiCall(payload: ForgetPasswordRequest) {
        val api = RetrofitHelper.getInstance().create(APIInterface::class.java)
        LoadingDialog.showProgress(requireActivity())
        api.forgetPassword(payload)
            .enqueue(object : Callback<ForgetPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgetPasswordResponse>,
                    response: Response<ForgetPasswordResponse>
                ) {
                    LoadingDialog.dismissProgress()

                    if (response.isSuccessful) {
                        Toasty.success(
                            requireContext(),
                            "${response.body()?.message}",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    } else {
                        Toasty.error(
                            requireContext(),
                            "Forget to process. Please try again.",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
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