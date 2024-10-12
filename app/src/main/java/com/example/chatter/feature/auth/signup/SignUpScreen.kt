package com.example.chatter.feature.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Button

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatter.R
import com.example.chatter.feature.auth.signin.ShowHidePasswordTextField
import com.example.chatter.feature.auth.signin.SimpleField


@Composable
fun SignUpScreen(navController: NavController) {
    var name = remember {
        mutableStateOf("")
    }
    var email = remember {
        mutableStateOf("")
    }
    var password = remember {
        mutableStateOf("")
    }
    var confirmPass = remember {
        mutableStateOf("")
    }
    var trueInputPassword = remember {
        mutableStateOf(Boolean)
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

            SimpleField("Full Name", name)
            Spacer(modifier = Modifier.size(6.dp))
            SimpleField("Enter Email", email)
            Spacer(modifier = Modifier.size(6.dp))
            ShowHidePasswordTextField("Enter Password", password)

            Spacer(modifier = Modifier.size(6.dp))

            ShowHidePasswordTextField(
                "Confirm Password", confirmPass,

                isError = password.value.isNotEmpty() &&
                        confirmPass.value.isNotEmpty() &&
                        password.value != confirmPass.value
            )

            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
                enabled = password.value.isNotEmpty() &&
                        confirmPass.value.isNotEmpty() &&
                        password.value == confirmPass.value &&
                        email.value.isNotEmpty()
            ) {
                Text(text = "Sign Un")
            }
            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = "Already have account? Sign In")
            }


        }

    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewSignInScreen() {
    SignUpScreen(navController = rememberNavController())
}