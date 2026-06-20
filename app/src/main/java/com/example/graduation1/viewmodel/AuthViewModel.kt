package com.example.graduation1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.AuthRepository
import com.example.graduation1.domain.models.RegisterRequest
import com.example.graduation1.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    fun createAccount(userName : String, email : String, password : String){
        viewModelScope.launch {
            try {

                val user = RegisterRequest(
                    1,
                    userName,
                    userName,
                    email,
                    password

                )
                val response = authRepository.createAccount(user)


                Log.e("AuthViewModel", "createAccount: account created successfully, $response")
            }
            catch (e: Exception){
                Log.e("AuthViewModel", "createAccount: ${e.message}")
            }
        }
    }

    fun login(email : String, password : String){
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)

                Log.e("AuthViewModel", response.toString())
            }
            catch (e: Exception){
                Log.e("AuthViewModel", "login: ${e.message}")
            }
        }
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