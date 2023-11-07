package com.example.chrisasarhasa

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveLoginCredentials(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    fun saveUserDetails(firstName: String, lastName: String, email: String, imageUrl: String) {
        val editor = sharedPreferences.edit()
        val fullName = "$firstName $lastName"
        editor.putString("fullName", fullName)
        editor.putString("email", email)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getSavedUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    fun getSavedPassword(): String? {
        return sharedPreferences.getString("password", null)
    }

    fun getSavedFullName(): String? {
        return sharedPreferences.getString("fullName", null)
    }

    fun getSavedEmail(): String? {
        return sharedPreferences.getString("email", null)
    }

    fun getSavedImageUrl(): String? {
        return sharedPreferences.getString("imageUrl", null)
    }
}
