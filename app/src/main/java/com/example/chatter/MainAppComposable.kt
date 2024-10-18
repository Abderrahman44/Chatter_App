package com.example.chatter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatter.feature.auth.AuthViewModel
import com.example.chatter.feature.auth.signin.SignInScreen
import com.example.chatter.feature.auth.signup.SignUpScreen
import com.example.chatter.feature.chat.ChatScreen
import com.example.chatter.feature.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp(modifier: Modifier = Modifier,authViewModel: AuthViewModel) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()

        NavHost(navController = navController,"login", builder = {

            composable(paths.login.name) {
                SignInScreen(navController,authViewModel)
            }
            composable("signup") {
                SignUpScreen(navController,authViewModel)
            }
            composable("home") {
                HomeScreen(navController,authViewModel)
            }
            composable("chat/{channelId}", arguments = listOf(
                navArgument("channelId"){
                    type = NavType.StringType
                }
            )) {
                val channelId = it.arguments?.getString("channelId")?:""
                ChatScreen(navController = navController, channelId =channelId )
            }
        } )
    }
}

enum class paths{
    login,

}