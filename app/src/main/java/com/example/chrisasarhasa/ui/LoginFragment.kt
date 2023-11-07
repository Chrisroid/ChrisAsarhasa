package com.example.chrisasarhasa.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.navigation.fragment.findNavController
import com.example.chrisasarhasa.R
import com.example.chrisasarhasa.SharedPreferencesHelper
import com.example.chrisasarhasa.databinding.FragmentLoginBinding
import com.example.chrisasarhasa.model.LoginData
import com.example.chrisasarhasa.model.LoginUser
import com.example.chrisasarhasa.service.ApiService
import com.example.chrisasarhasa.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var apiService: ApiService
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        apiService = RetrofitClient.apiService
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        val savedUsername = sharedPreferencesHelper.getSavedUsername()
        val savedPassword = sharedPreferencesHelper.getSavedPassword()

        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            // Populate the EditText fields with the saved credentials
            binding.textInputEditTextUsername.setText(savedUsername)
            binding.textInputEditTextPassword.setText(savedPassword)
            binding.checkBoxRememberMe.isChecked = true
        }

        navigateToProfileFragment()
        // Inflate the layout for this fragment
        return binding.root
    }



    private fun navigateToProfileFragment() {
        binding.loginButton.setOnClickListener {
            val username = binding.textInputEditTextUsername.text.toString()
            val password = binding.textInputEditTextPassword.text.toString()
            val loginUser = LoginUser(username, password)
            if (binding.checkBoxRememberMe.isChecked) {
                // Save credentials in SharedPreferences if "Remember Me" is checked
                sharedPreferencesHelper.saveLoginCredentials(username, password)
            }
            if (loginUser.username == "" || loginUser.password == "") {
                makeText(requireContext(), "Fields Cannot be Empty", Toast.LENGTH_SHORT).show()
            } else {
            // Make the API call to authenticate the user
            apiService.login(loginUser).enqueue(object : Callback<LoginData> {
                override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                    if (response.isSuccessful) {
                        val loginData = response.body()
                        if (loginData != null) {
                            sharedPreferencesHelper.saveUserDetails(loginData.firstName, loginData.lastName, loginData.email, loginData.image)
                        }
                        // Handle a successful login response, e.g., save the token, navigate to the profile fragment, etc.
                        findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                    } else {
                        // Handle login failure (e.g., incorrect credentials)
                        makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginData>, t: Throwable) {
                    // Handle network or request failure
                    makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
        }
    }

}