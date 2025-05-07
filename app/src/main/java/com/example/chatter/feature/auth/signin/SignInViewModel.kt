package com.example.chatter.feature.auth.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state = _state.asStateFlow()

    fun signIn(email:String, password:String){
        val auth : FirebaseAuth = FirebaseAuth.getInstance()
        val signIn = auth.signInWithEmailAndPassword(email,password)
        _state.value = SignInState.Loading
        //firebase signIn
        signIn
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let {
                        _state.value = SignInState.Success
                        return@addOnCompleteListener
                    }

                } else{
                    _state.value = SignInState.Error
                }
            }
    }

}


