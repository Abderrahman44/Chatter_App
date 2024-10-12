package com.example.chatter.feature.auth.signin


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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatter.R
import java.lang.Error
import androidx.compose.material3.Text as Text


@Composable
fun SignInScreen(navController: NavController) {
    var email = remember {
        mutableStateOf("")
    }
    var password = remember {
        mutableStateOf("")
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
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign in")
            }
            TextButton(onClick = { navController.navigate("signup") }) {
                Text(text = "Don't have account? Sign up")
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