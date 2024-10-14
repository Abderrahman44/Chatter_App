package com.example.chatter.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatter.feature.auth.AuthViewModel

@Composable
fun HomeScreen(navController: NavController,authViewModel: AuthViewModel) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(text = "Home Page", fontSize = 32.sp)

            TextButton(onClick = {
                authViewModel.signOut()
                navController.navigate("login")
            }) {
                Text(text = "Sign out")
            }
        }
    }
}