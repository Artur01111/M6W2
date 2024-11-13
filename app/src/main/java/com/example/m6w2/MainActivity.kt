package com.example.m6w2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Registration : Screen("registration_screen")
    object Confirmation : Screen("confirmation_screen/{username}") {
        fun createRoute(username: String) = "confirmation_screen/$username"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() as NavHostController
            OnboardingNavHost(navController = navController)
        }
    }
}

@Composable
fun OnboardingNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(onNextClick = { navController.navigate(Screen.Registration.route) })
        }
        composable(route = Screen.Registration.route) {
            RegistrationScreen(onRegisterClick = { username ->
                navController.navigate(Screen.Confirmation.createRoute(username))
            })
        }
        composable(
            route = Screen.Confirmation.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            ConfirmationScreen(username = username ?: "")
        }
    }
}

@Composable
fun WelcomeScreen(onNextClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Welcome to the App")
        Button(onClick = onNextClick) {
            Text("Next")
        }
    }
}

@Composable
fun RegistrationScreen(onRegisterClick: (String) -> Unit) {
    var username by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Button(onClick = { onRegisterClick(username) }) {
            Text("Register")
        }
    }
}

@Composable
fun ConfirmationScreen(username: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Welcome, $username!")
        Text(text = "Registration successful.")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController() as NavHostController
    OnboardingNavHost(navController = navController)
}