package com.example.p10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.p10.ui.theme.P10Theme


class MainActivity2 : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P10Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserGreeting(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun UserGreeting(mainViewModel: MainViewModel) {
    val users = mainViewModel.users.collectAsState(initial = emptyList()).value
    LaunchedEffect(key1 = true) {
        mainViewModel.fetchAllUsers()
    }
    // Assuming you want to display something from the users
    if (users.isNotEmpty()) {
        Text(text = "First user: ${users.first().name}")
    } else {
        Text(text = "No users found")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    P10Theme {
//        UserGreeting()
    }
}
