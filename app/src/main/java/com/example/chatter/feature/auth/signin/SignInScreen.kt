package com.example.chatter.feature.auth.signin


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatter.R
import androidx.compose.material3.Text as Text


@Composable
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value ) {
        when(uiState.value) {
            is SignInState.Success -> navController.navigate("home")
            is SignInState.Error -> {
                Toast.makeText(context,"Sign in failed",Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.White)
            )

            SimpleField("Enter Email", email)
            Spacer(modifier = Modifier.size(6.dp))
            ShowHidePasswordTextField("Enter Password", password)

            Spacer(modifier = Modifier.size(16.dp))
            if (uiState.value == SignInState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.signIn(email.value, password.value) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.value.isNotEmpty() && password.value.isNotEmpty() &&
                            (uiState.value == SignInState.Nothing || uiState.value == SignInState.Error)
                ) {
                    Text(text = "Sign in")
                }
            }
            TextButton(onClick = { navController.navigate("signup") }) {
                Text(text = "Don't have account? Sign Up")
            }

        }

    }
}


@Composable
fun SimpleField(message: String, content: MutableState<String>, isError: Boolean = false) {

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = content.value,
        onValueChange = { content.value = it },
        placeholder = { Text(text = message) },
        isError = isError

    )
}

@Composable
fun ShowHidePasswordTextField(
    message: String, password: MutableState<String>, isError: Boolean = false
) {

    // var password by remember { mutableStateOf(value = "") }
    var showPassword by remember { mutableStateOf(value = false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = password.value,
        onValueChange = {
            password.value = it
        },
        isError = isError,
        placeholder = { Text(text = message) },
        visualTransformation = if (showPassword) {

            VisualTransformation.None

        } else {

            PasswordVisualTransformation()

        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "hide_password"
                    )
                }
            } else {
                IconButton(
                    onClick = { showPassword = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "hide_password"
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewSignInScreen() {
    SignInScreen(navController = rememberNavController())
}