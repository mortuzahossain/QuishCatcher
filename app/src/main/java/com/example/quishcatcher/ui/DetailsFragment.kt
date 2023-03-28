package com.example.quishcatcher.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quishcatcher.R
import com.example.quishcatcher.SessionManager
import com.example.quishcatcher.SessionManager.get
import com.example.quishcatcher.api.*
import com.example.quishcatcher.databinding.FragmentDetailsBinding
import com.example.quishcatcher.makeToken
import com.example.quishcatcher.showAlertDialog
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment : Fragment(R.layout.fragment_details) {
    lateinit var binding: FragmentDetailsBinding
    lateinit var pref: SharedPreferences
    val gson = Gson()
    lateinit var token: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        pref = SessionManager.sessionPrefs(requireContext())
        token = pref[Constants.SharedPref.TOKEN, ""].toString()

        binding.tvContent.text = "QR Content: ${Constants.browsableUrl}"
        binding.btnCheck.setOnClickListener {

            if (URLUtil.isValidUrl(Constants.browsableUrl)) {
                performApiCall()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Do not contain valid url",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }


    private fun performApiCall() {
        val api = RetrofitHelper.getInstance().create(APIInterface::class.java)
        LoadingDialog.showProgress(requireActivity())
        api.scanUrl(ScanUrlRequest(Constants.browsableUrl), token.makeToken())
            .enqueue(object : Callback<ScanResponse> {
                override fun onResponse(
                    call: Call<ScanResponse>, response: Response<ScanResponse>
                ) {
                    LoadingDialog.dismissProgress()

                    if (response.isSuccessful) {
                        if (response.body()?.isSafe == true) {
                            showAlertDialog(requireContext(), "Safe", "${response.body()?.message}", true)
                        } else {
                            showAlertDialog(requireContext(), "Unsafe", "${response.body()?.message}", false)
                        }
                    } else {
                        Toasty.error(
                            requireContext(),
                            "Scan Failed. Please try again.",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ScanResponse>, t: Throwable) {
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