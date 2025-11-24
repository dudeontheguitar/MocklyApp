package com.example.mocklyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mocklyapp.presentation.screens.*

@Composable
fun AppNav(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Onboarding1
    ) {
        composable<Onboarding1> { OnboardingScreen1(
            onSkipClick = {navController.navigate(Login){
                popUpTo(Onboarding1) { inclusive = true }
                launchSingleTop = true
            } },
            onNextClick = {navController.navigate(Onboarding2){

                launchSingleTop = true
            } }
        ) }
        composable<Onboarding2> { OnboardingScreen2(
            onSkipClick = {navController.navigate(Login){
                popUpTo(Onboarding1) { inclusive = true }
                launchSingleTop = true
            } },
            onNextClick = {navController.navigate(Onboarding3){
                launchSingleTop = true
            } }
        ) }
        composable<Onboarding3> { OnboardingScreen3(
            onSkipClick = {navController.navigate(Login){
                popUpTo(Onboarding1) { inclusive = true }
                launchSingleTop = true
            } },
            onNextClick = {navController.navigate(Login){
                popUpTo(Onboarding1) { inclusive = true }
                launchSingleTop = true
            } }
        ) }
        composable<Login> { LoginScreen(
            onSignInClick = {
                navController.navigate(DiscoverRoute){
                    popUpTo(Login) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNoAccountClick = {
                navController.navigate(Register){
                    launchSingleTop = true
                }
            }
        )}
        composable<Register> { RegisterScreen(
            onSignUpClick = {
                navController.navigate(DiscoverRoute){
                    popUpTo(Register){
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        ) }
        composable<DiscoverRoute> { BottomNav() }


    }
}

@Composable
fun BottomNav(){
    val nav = rememberNavController()
    val tabs = listOf(DiscoverRoute, InterviewRoute, MessageRoute, SettingsRoute)
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in tabs.map { it::class.qualifiedName }

    Scaffold (
        bottomBar = {
            if (showBottomBar) BottomBar(nav, tabs, currentRoute)
        }
    ){ innerPadding ->
        NavHost(
            navController = nav,
            startDestination = DiscoverRoute,
            modifier = Modifier.padding(innerPadding).background(MaterialTheme.colorScheme.onBackground)
        ){
            composable<DiscoverRoute> { DiscoverScreen() }
            composable<InterviewRoute> { InterviewScreen(nav) }
            composable<MessageRoute> { MessageScreen() }
            composable<SettingsRoute> { SettingsScreen() }
            composable<InterviewRegister> {
                InterviewRegisterScreen(
                    onBack = {
                        nav.popBackStack()
                    },
                    onSuccessOK = {
                        nav.navigate(DiscoverRoute) {
                            popUpTo(nav.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }


        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    tabs: List<Any>,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab::class.qualifiedName

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(tab) {
                        popUpTo(navController.graph.id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val iconRes = when (tab) {
                        is DiscoverRoute  -> if (selected) R.drawable.discover else R.drawable.discover2
                        is InterviewRoute -> if (selected) R.drawable.interview else R.drawable.interview2
                        is MessageRoute   -> if (selected) R.drawable.message else R.drawable.message2
                        is SettingsRoute  -> if (selected) R.drawable.settings else R.drawable.settings2
                        else -> R.drawable.discover
                    }

                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                },
                label = {
                    Text(
                        text = when (tab) {
                            is DiscoverRoute  -> "Discover"
                            is InterviewRoute -> "Interview"
                            is MessageRoute   -> "Message"
                            is SettingsRoute  -> "Settings"
                            else -> ""
                        },
                        fontSize = 14.sp,
                        color = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
