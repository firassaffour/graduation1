package com.example.graduation1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduation1.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {


    fun createAccount(email : String, password : String){
        viewModelScope.launch {
            try {

            }
            catch (e: Exception){
                Log.e("AuthViewModel", "createAccount: ${e.message}")
            }
        }
    }

    fun login(email : String, password : String){
        viewModelScope.launch {
            try {

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
}