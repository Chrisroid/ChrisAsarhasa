package com.example.chrisasarhasa.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.chrisasarhasa.R
import com.example.chrisasarhasa.SharedPreferencesHelper
import com.example.chrisasarhasa.databinding.FragmentProfileBinding
import com.github.drjacky.imagepicker.ImagePicker


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        val savedFullName = sharedPreferencesHelper.getSavedFullName()
        val savedEmail = sharedPreferencesHelper.getSavedEmail()
        val savedImageUrl = sharedPreferencesHelper.getSavedImageUrl()

        if (!savedFullName.isNullOrEmpty() && !savedEmail.isNullOrEmpty() && !savedImageUrl.isNullOrEmpty()) {
            // Populate the EditText fields with the saved credentials
            binding.textInputEditTextFullName.setText(savedFullName)
            binding.textInputEditTextEmail.setText(savedEmail)
            Glide.with(this)
                .load(savedImageUrl)
                .centerInside()
                .into(binding.profileImage)
        }

        binding.editImage.setOnClickListener {
            ImagePicker.with(requireActivity())
                .createIntentFromDialog {
                    launcher.launch(it)
                }
        }
        logoutUser()

        // for fragment: requireActivity().onBackPressedDispatcher
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing

            }
        })
        return binding.root
    }


    private fun logoutUser() {
        binding.logoutUser.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                // Only if you are not using crop feature:
                // Grant persistable URI permission

                Glide.with(this)
                    .load(uri)
                    .into(binding.profileImage)
            }
        }

}