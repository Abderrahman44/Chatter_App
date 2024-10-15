package com.example.chatter.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatter.feature.auth.AuthState.Unauthenticated
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>(Unauthenticated)
    val authState :LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus(){
        if (auth.currentUser == null){
            _authState.value = Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email:String, pass:String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    return@addOnCompleteListener
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?: "Login Failed")

                }
            }
    }
    fun signUp(name:String,email: String,pass: String){
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    return@addOnCompleteListener
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"sign up failed")
                }
            }
    }
    fun signOut(){
        auth.signOut()
        _authState.value = Unauthenticated
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}