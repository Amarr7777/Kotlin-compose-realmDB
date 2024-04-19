@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.p10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.p10.ui.theme.Mgreen
import com.example.p10.ui.theme.P10Theme

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P10Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.log),
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserInfoForm(mainViewModel)
        }
    }
}

@Composable
fun UserInfoForm(mainViewModel: MainViewModel) {
    var name by remember { mutableStateOf("") }

    Column {
        Text(text = "Name", color = Color.White)
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            UserInputField(label = "Name", value = name, onValueChange = { name = it })
            IconButton(onClick = {
                mainViewModel.createUser(name)
                name = "" // Clear input after adding user
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add", tint = Mgreen)
            }
        }
        Results(mainViewModel)
    }
}


@Composable
fun UserInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .padding(5.dp, 10.dp)
                .background(Color.Black)
                .border(width = 1.dp, color = Mgreen, shape = CircleShape) // Set the border color and width
        ){
            TextField(value = value, onValueChange = onValueChange, label = { Text(label) },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Black, unfocusedTextColor = Color.White,focusedTextColor = Color.White,focusedIndicatorColor = Color.Transparent, // Remove the focused indicator
                    unfocusedIndicatorColor = Color.Transparent))
        }

    }
}


@Composable
fun Results(mainViewModel: MainViewModel) {
    val users by mainViewModel.users.collectAsState(initial = emptyList())
    mainViewModel.fetchAllUsers()
    if (users.isNotEmpty()) {
        Text("Results", color = Mgreen, fontSize = 30.sp)
        LazyColumn {
            items(users) { user ->
                UserItem(user, mainViewModel)
            }
        }


    } else {
        Text("No users found", color = Color.White)
    }
}

@Composable
fun UserItem(user: User, mainViewModel: MainViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                var newName by remember { mutableStateOf(user.name) }
                Box(
                    modifier = Modifier
                        .padding(5.dp, 10.dp)
                        .background(Color.Black)
                        .border(width = 1.dp, color = Mgreen, shape = CircleShape) // Set the border color and width
                ){
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Update name") },
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Black, unfocusedTextColor = Color.White,focusedTextColor = Color.White,focusedIndicatorColor = Color.Transparent, // Remove the focused indicator
                        unfocusedIndicatorColor = Color.Transparent)
                )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    mainViewModel.updateUser(user.name, newName)
                    mainViewModel.fetchAllUsers()
                    showDialog = false
                },
                    colors = ButtonDefaults.buttonColors(Mgreen)) {
                    Text("Update", color = Color.Black)
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Name: ${user.name}", color = Color.White)
        Row {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Mgreen)
            }
            IconButton(onClick = { mainViewModel.deleteUser(user.name)
                mainViewModel.fetchAllUsers()}) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Mgreen)
            }
        }
    }
}
