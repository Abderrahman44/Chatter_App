package com.example.chatter.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatter.feature.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channels.collectAsState()
    val addChannel = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()


    Scaffold(
        floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = { addChannel.value = true },
        ) {
            Icon(Icons.Filled.Add, "Add New channel")
            Text(text = "Add")
        }
    }) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn {
                    items(channels.value) { channel ->
                        Column {
                            Text(text = channel.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.LightGray)
                                    .clickable {
                                        //  navController.navigate("chat/${channel.id}")
                                    }
                                    .padding(16.dp)
                            )

                        }
                    }
                }

            }
            Column {
                TextButton(onClick = {
                    authViewModel.signOut()
                    navController.navigate("login")
                }) {
                    Text(text = "Sign out")
                }

            }

        }
    }
    if (addChannel.value) {
        ModalBottomSheet(
            onDismissRequest = { addChannel.value = false },
            sheetState = sheetState
        ) {
            AddChannelDialog {
                viewModel.addChannel(it)
                addChannel.value = false
            }
        }
    }

}



@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember {
        mutableStateOf("")
    }

    Column(
        Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Channel")
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = channelName.value,
            onValueChange = {
                channelName.value = it
            },
            label = { Text(text = "Channel Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = { onAddChannel(channelName.value) },
            modifier = Modifier.fillMaxWidth(),
            enabled = channelName.value.isNotEmpty()
        ) {
            Text(text = "Add")
        }
    }
}




@Preview(showBackground = true)
@Composable

fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), authViewModel = AuthViewModel())
}