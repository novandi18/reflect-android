package com.novandiramadhan.reflect.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.novandiramadhan.reflect.presentation.screen.SignInScreen
import com.novandiramadhan.reflect.presentation.screen.SignUpScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable<Destinations.SignIn> {
        SignInScreen(
            navigateToSignUp = {
                navController.navigate(Destinations.SignUp)
            },
            login = {
                navController.navigate(Destinations.Home) {
                    popUpTo(Destinations.SignIn) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<Destinations.SignUp> {
        SignUpScreen(
            back = {
                navController.popBackStack()
            },
            navigateToHome = {
                navController.navigate(Destinations.Home) {
                    popUpTo(Destinations.SignUp) {
                        inclusive = true
                    }
                }
            }
        )
    }
}