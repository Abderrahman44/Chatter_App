package com.example.chatter.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatter.MainActivity
import com.example.chatter.feature.auth.AuthViewModel
import com.example.chatter.model.Channel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {
//    val context = LocalContext.current as MainActivity
//
//    LaunchedEffect(Unit) {
//
//    }
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
                Text(text = "New Channel")
            }
        }, contentColor = Color.Black
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                LazyColumn {
                    item {
                        Text(
                            text = "Messages",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }


                    item() {
                        val onActiveSearch = remember {
                            mutableStateOf(false)
                        }
                        DockedSearchBar(

                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(top = 8.dp)
                                .clip(
                                    RoundedCornerShape(40.dp)
                                ),

                            query = "",
                            onQueryChange = {},
                            onSearch = { onActiveSearch.value = false },
                            active = onActiveSearch.value,
                            onActiveChange = { onActiveSearch.value = it },
                            placeholder = { Text(text = "Search ...") },
                            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                            trailingIcon = {
                                Icon(
                                    Icons.Rounded.MoreVert,
                                    contentDescription = null
                                )
                            },

                            ) {}
                    }
                    items(channels.value) { channel ->
                        Column {
                            ChannelItem(
                                channelName = channel.name,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                                onClick = {
                                    navController.navigate("chat/${channel.id}&${channel.name}")
                                }
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
fun ChannelItem(
    channelName: String,
    //shouldShowCallButtons: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,

    ) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))

    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(70.dp)
                    .clip(CircleShape)

            ) {
                Text(
                    text = channelName[0].uppercase(),
                    color = Color.White,
                    style = TextStyle(fontSize = 35.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            Text(text = channelName, modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.inversePrimary)
        }
//        if (shouldShowCallButtons) {
//            Row(
//                modifier = Modifier.align(Alignment.CenterEnd)
//            ) {
//
//            }
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

@Preview
@Composable
fun PreviewItem() {
    ChannelItem(
        channelName = "Test Channel", Modifier, {})
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {

    HomeScreen(rememberNavController(), authViewModel = AuthViewModel())
}