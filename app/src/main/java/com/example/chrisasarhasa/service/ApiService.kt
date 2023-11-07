package com.example.chrisasarhasa.service

import com.example.chrisasarhasa.model.LoginData
import com.example.chrisasarhasa.model.LoginUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginUser): Call<LoginData>
}