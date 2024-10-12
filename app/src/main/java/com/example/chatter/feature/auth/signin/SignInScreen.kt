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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatter.R
import androidx.compose.material3.Text as Text


@Composable
fun SignInScreen(navController: NavController) {
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

            EmailField()
            ShowHidePasswordTextField()

            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Sign in")
            }

        }

    }
}




@Composable
private fun EmailField() {

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {},
        value = "",
        //state = rememberTextFieldState(email),
        placeholder = { Text(text ="Enter email" )}
    )
}
@Composable
fun ShowHidePasswordTextField() {

   // var password by remember { mutableStateOf(value = "") }
    var showPassword by remember { mutableStateOf(value = false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = "",
        onValueChange = {
        },
        placeholder = { Text(text = "Type password here") },
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