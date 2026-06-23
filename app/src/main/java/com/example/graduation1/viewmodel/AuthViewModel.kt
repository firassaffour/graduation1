package com.example.graduation1.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.domain.models.requets_response.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    fun createAccount(userName : String, email : String, password : String, onSuccess : () -> Unit, onFailure : () -> Unit){
        viewModelScope.launch {
            try {

                val user = RegisterRequest(
                    userName,
                    "",
                    email,
                    password,
                    "User"
                )
                val response = authRepository.createAccount(user)

                onSuccess()

                Log.e("AuthViewModel", "createAccount: account created successfully, $response")
            }
            catch (e: Exception){
                onFailure()
                Log.e("REGISTER_EXCEPTION", e.toString())
            }
        }
    }

    fun login(context: Context, email : String, password : String, onSuccess : () -> Unit, onFailure : () -> Unit){
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)

                val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                prefs.edit() { putString("token", response.token) }

                onSuccess()

                Log.e("AuthViewModel", response.toString())
                Log.e("AuthViewModel TOKEN", response.token)
                Log.d("token preference", prefs.getString("token", "").toString())
            }
            catch (e: Exception){
                onFailure()
                Log.e("AuthViewModel", "login: ${e.message}")
            }
        }
    }

    fun logout(context: Context){
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit() { remove("token") }
    }

    fun deleteAccount(email : String){
        viewModelScope.launch {
            try {

            }
            catch (e: Exception){
                Log.e("AuthViewModel", "deleteAccount: ${e.message}")
            }
        }
    }

    fun updateEmail(email : String){
        _email.value = email
    }
}