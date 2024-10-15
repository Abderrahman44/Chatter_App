package com.example.chatter.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatter.feature.auth.AuthViewModel

@Composable
fun HomeScreen(navController: NavController,authViewModel: AuthViewModel) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channels.collectAsState()
    
    Scaffold {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            LazyColumn {
                items(channels.value) {channel ->
                    Column {
                        Text(text = channel.name,
                            modifier = Modifier.fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray)
                                .clickable{
                                    navController.navigate("chat/${channel.id}")
                                }
                                .padding(16.dp)
                                )
                        Spacer(modifier = Modifier.padding(15.dp))
                        TextButton(onClick = {
                        authViewModel.signOut()
                        navController.navigate("login")
                            }) {
                                Text(text = "Sign out")
                            }
                    }
                }
            }
        }
    }

}



//        Column(
//            modifier = Modifier
//                .padding(it)
//                .fillMaxSize()
//        ) {
//            Text(text = "Home Page", fontSize = 32.sp)
//
//            TextButton(onClick = {
//                authViewModel.signOut()
//                navController.navigate("login")
//            }) {
//                Text(text = "Sign out")
//            }
//        }
