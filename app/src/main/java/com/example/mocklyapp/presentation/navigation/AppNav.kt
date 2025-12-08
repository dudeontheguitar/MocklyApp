package com.example.mocklyapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mocklyapp.ApiClient
import com.example.mocklyapp.R
import com.example.mocklyapp.data.auth.AuthRepositoryImpl
import com.example.mocklyapp.data.auth.local.AuthLocalDataSource
import com.example.mocklyapp.data.auth.remote.AuthApi
import com.example.mocklyapp.data.session.SessionRepositoryImpl
import com.example.mocklyapp.data.session.remote.SessionApi
import com.example.mocklyapp.data.user.UserRepositoryImpl
import com.example.mocklyapp.data.user.remote.UserApi
import com.example.mocklyapp.domain.session.SessionRepository
import com.example.mocklyapp.presentation.auth.login.LoginScreen
import com.example.mocklyapp.presentation.auth.login.LoginViewModel
import com.example.mocklyapp.presentation.auth.onboarding.OnboardingScreen1
import com.example.mocklyapp.presentation.auth.onboarding.OnboardingScreen2
import com.example.mocklyapp.presentation.auth.onboarding.OnboardingScreen3
import com.example.mocklyapp.presentation.auth.register.RegisterScreen
import com.example.mocklyapp.presentation.auth.register.RegisterViewModel
import com.example.mocklyapp.presentation.interview.InterviewRegisterViewModel
import com.example.mocklyapp.presentation.interview.InterviewViewModel
import com.example.mocklyapp.presentation.interview.InterviewerSessionsScreen
import com.example.mocklyapp.presentation.screens.DiscoverScreen
import com.example.mocklyapp.presentation.screens.InterviewRegisterScreen
import com.example.mocklyapp.presentation.screens.InterviewScreen
import com.example.mocklyapp.presentation.screens.MessageScreen
import com.example.mocklyapp.presentation.session.MockInterviewScreen
import com.example.mocklyapp.presentation.session.SessionDetailsScreen
//import com.example.mocklyapp.presentation.session.SessionDetailsScreen
import com.example.mocklyapp.presentation.sessiondetails.SessionDetailsViewModel
import com.example.mocklyapp.presentation.settings.SettingsScreen
import com.example.mocklyapp.presentation.settings.SettingsViewModel
import com.example.mocklyapp.presentation.settings.change_password.ChangePasswordScreen
import com.example.mocklyapp.presentation.settings.change_password.ChangePasswordViewModel
import com.example.mocklyapp.presentation.settings.edit_profile.EditProfileScreen
import com.example.mocklyapp.presentation.settings.edit_profile.EditProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNav(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current

    val authApi = remember { ApiClient.retrofit.create(AuthApi::class.java) }
    val authLocal = remember { AuthLocalDataSource(context) }
    val authRepo = remember { AuthRepositoryImpl(authApi, authLocal) }

    val isOnboardingCompleted = remember { authLocal.isOnboardingCompleted() }
    val isLoggedIn = remember { authLocal.getAccessToken() != null }

    // если пользователь залогинен — стартуем с RoleEntryRoute
    val startDestination = when {
        isLoggedIn -> RoleEntryRoute
        isOnboardingCompleted -> Login
        else -> Onboarding1
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Onboarding1> {
            OnboardingScreen1(
                onSkipClick = {
                    authLocal.setOnboardingCompleted()
                    navController.navigate(Login) {
                        popUpTo(Onboarding1) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNextClick = {
                    navController.navigate(Onboarding2) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Onboarding2> {
            OnboardingScreen2(
                onSkipClick = {
                    authLocal.setOnboardingCompleted()
                    navController.navigate(Login) {
                        popUpTo(Onboarding1) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNextClick = {
                    navController.navigate(Onboarding3) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Onboarding3> {
            OnboardingScreen3(
                onSkipClick = {
                    authLocal.setOnboardingCompleted()
                    navController.navigate(Login) {
                        popUpTo(Onboarding1) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNextClick = {
                    authLocal.setOnboardingCompleted()
                    navController.navigate(Login) {
                        popUpTo(Onboarding1) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Login> {
            val loginViewModel = remember { LoginViewModel(authRepo) }

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(RoleEntryRoute) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNoAccountClick = {
                    navController.navigate(Register)
                }
            )
        }

        composable<Register> {
            val registerViewModel = remember { RegisterViewModel(authRepo) }
            RegisterScreen(
                viewModel = registerViewModel,
                onSignUpSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<RoleEntryRoute> {
            val contextLocal = LocalContext.current
            val authLocalInner = remember { AuthLocalDataSource(contextLocal) }

            val authedRetrofit = remember {
                ApiClient.authedRetrofit(authLocalInner)
            }
            val userApi = remember {
                authedRetrofit.create(UserApi::class.java)
            }
            val userRepo = remember {
                UserRepositoryImpl(userApi)
            }

            val authApiInner = remember {
                ApiClient.retrofit.create(AuthApi::class.java)
            }
            val authRepoInner = remember {
                AuthRepositoryImpl(authApiInner, authLocalInner)
            }

            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }
            var role by remember { mutableStateOf<String?>(null) }

            // общий scope для корутин в этом composable
            val coroutineScope = rememberCoroutineScope()

            // функция загрузки роли
            fun loadRole() {
                isLoading = true
                error = null
                role = null

                coroutineScope.launch {
                    try {
                        val user = userRepo.getCurrentUser()
                        role = user.role            // "CANDIDATE" или "INTERVIEWER"
                        isLoading = false
                    } catch (e: Exception) {
                        error = e.message ?: "Failed to load user role."
                        isLoading = false
                    }
                }
            }

            // первый запуск – грузим роль
            LaunchedEffect(Unit) {
                loadRole()
            }

            // когда роль загрузилась — редиректим
            LaunchedEffect(role) {
                when (role?.uppercase()) {
                    "CANDIDATE" -> {
                        navController.navigate(DiscoverRoute) {
                            popUpTo(RoleEntryRoute) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    "INTERVIEWER" -> {
                        navController.navigate(InterviewerRootRoute) {
                            popUpTo(RoleEntryRoute) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }

            RoleEntryScreen(
                isLoading = isLoading,
                error = error,
                onRetry = {
                    // просто ещё раз грузим роль
                    loadRole()
                },
                onLogout = {
                    // Logout в корутине, БЕЗ LaunchedEffect
                    coroutineScope.launch {
                        try {
                            authRepoInner.logout()
                        } catch (_: Exception) {
                        }
                        navController.navigate(Login) {
                            popUpTo(RoleEntryRoute) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }



        /** root кандидата */
        composable<DiscoverRoute> {
            BottomNav(
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(DiscoverRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        /** root интервьюера */
        composable<InterviewerRootRoute> {
            InterviewerBottomNav(
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(InterviewerRootRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

/** простой экран-состояние для RoleEntryRoute */
@Composable
private fun RoleEntryScreen(
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.onBackground
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(32.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                error != null -> {
                    Column {
                        Text(
                            text = "Failed to load user info.",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                        TextButton(onClick = onRetry) {
                            Text("Retry")
                        }
                        TextButton(onClick = onLogout) {
                            Text("Logout")
                        }
                    }
                }

                else -> {
                    Text(
                        text = "Preparing your workspace...",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                      BOTTOM NAV — CANDIDATE                                */
/* -------------------------------------------------------------------------- */

@Composable
fun BottomNav(
    onLogout: () -> Unit
) {
    val nav = rememberNavController()
    val tabs = listOf(DiscoverRoute, InterviewRoute, MessageRoute, SettingsRoute)
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in tabs.map { it::class.qualifiedName }

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomBar(nav, tabs, currentRoute)
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = DiscoverRoute,
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            composable<DiscoverRoute> { DiscoverScreen() }

            composable<InterviewRoute> {
                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val sessionApi = remember {
                    authedRetrofit.create(SessionApi::class.java)
                }
                val sessionRepo = remember {
                    SessionRepositoryImpl(sessionApi)
                }

                val userApi = remember {
                    authedRetrofit.create(UserApi::class.java)
                }
                val userRepo = remember {
                    UserRepositoryImpl(userApi)
                }

                val vm = remember {
                    InterviewViewModel(
                        sessionRepo = sessionRepo,
                        userRepo = userRepo
                    )
                }

                InterviewScreen(
                    navController = nav,
                    viewModel = vm
                )
            }

            composable<MessageRoute> { MessageScreen() }

            composable<SettingsRoute> {

                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authApi = remember {
                    ApiClient.retrofit.create(AuthApi::class.java)
                }
                val authRepo = remember {
                    AuthRepositoryImpl(authApi, authLocal)
                }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val userApi = remember {
                    authedRetrofit.create(UserApi::class.java)
                }
                val userRepo = remember {
                    UserRepositoryImpl(userApi)
                }

                val settingsViewModel = remember {
                    SettingsViewModel(
                        userRepo = userRepo,
                        authRepo = authRepo
                    )
                }

                SettingsScreen(
                    viewModel = settingsViewModel,
                    onEditProfileClick = {
                        nav.navigate(EditProfileRoute)
                    },
                    onChangePasswordClick = {
                        nav.navigate(ChangePasswordRoute)
                    },
                    onLogoutClick = {
                        onLogout()
                    }
                )
            }

            composable<EditProfileRoute> {
                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val userApi = remember {
                    authedRetrofit.create(UserApi::class.java)
                }
                val userRepo = remember {
                    UserRepositoryImpl(userApi)
                }

                val vm = remember {
                    EditProfileViewModel(userRepo)
                }

                EditProfileScreen(
                    viewModel = vm,
                    onBack = { nav.popBackStack() }
                )
            }

            composable<ChangePasswordRoute> {
                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val authApi = remember {
                    authedRetrofit.create(AuthApi::class.java)
                }
                val authRepo = remember {
                    AuthRepositoryImpl(authApi, authLocal)
                }

                val vm = remember {
                    ChangePasswordViewModel(authRepo)
                }

                ChangePasswordScreen(
                    viewModel = vm,
                    onBack = { nav.popBackStack() }
                )
            }

            /** экран регистрации на интервью (как у тебя было) */
            composable<InterviewRegister> { backStackEntry ->
                val args = backStackEntry.toRoute<InterviewRegister>()

                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val sessionApi = remember {
                    authedRetrofit.create(SessionApi::class.java)
                }
                val sessionRepo: SessionRepository = remember {
                    SessionRepositoryImpl(sessionApi)
                }

                val vm = remember {
                    InterviewRegisterViewModel(
                        sessionRepo = sessionRepo,
                        interviewerId = args.interviewerId,
                        interviewerName = args.interviewerName,
                        jobTitle = args.jobTitle,
                        company = args.company
                    )
                }

                InterviewRegisterScreen(
                    viewModel = vm,
                    jobTitle = args.jobTitle,
                    company = args.company,
                    interviewerName = args.interviewerName,
                    interviewerId = args.interviewerId,
                    onBack = { nav.popBackStack() },
                    onSuccessOK = {
                        nav.navigate(DiscoverRoute) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            /** НОВОЕ: детали сессии */
            composable<SessionDetailsRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<SessionDetailsRoute>()

                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val sessionApi = remember {
                    authedRetrofit.create(SessionApi::class.java)
                }
                val sessionRepo = remember {
                    SessionRepositoryImpl(sessionApi)
                }

                val vm = remember {
                    SessionDetailsViewModel(
                        sessionRepo = sessionRepo,
                        sessionId = args.sessionId
                    )
                }

                SessionDetailsScreen(
                    viewModel = vm,
                    onBack = { nav.popBackStack() },
                    onStartClick = {
                        nav.navigate(MockInterviewRoute(args.sessionId))
                    }
                )
            }

            /** НОВОЕ: экран мок-интервью */
            composable<MockInterviewRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<MockInterviewRoute>()

                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }

                // 💡 новый API + repo
                val artifactApi = remember {
                    authedRetrofit.create(com.example.mocklyapp.data.artifact.remote.ArtifactApi::class.java)
                }
                val artifactRepo = remember {
                    com.example.mocklyapp.data.artifact.ArtifactRepositoryImpl(artifactApi)
                }

                MockInterviewScreen(
                    sessionId = args.sessionId,
                    onBack = { nav.popBackStack() },
                    onEndInterview = {
                        nav.popBackStack()
                    },
                    artifactRepository = artifactRepo
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
                        is DiscoverRoute -> if (selected) R.drawable.discover else R.drawable.discover2
                        is InterviewRoute -> if (selected) R.drawable.interview else R.drawable.interview2
                        is MessageRoute -> if (selected) R.drawable.message else R.drawable.message2
                        is SettingsRoute -> if (selected) R.drawable.settings else R.drawable.settings2
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
                            is DiscoverRoute -> "Discover"
                            is InterviewRoute -> "Interview"
                            is MessageRoute -> "Message"
                            is SettingsRoute -> "Settings"
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

/* -------------------------------------------------------------------------- */
/*                         BOTTOM NAV — INTERVIEWER                          */
/* -------------------------------------------------------------------------- */

@Composable
fun InterviewerBottomNav(
    onLogout: () -> Unit
) {
    val nav = rememberNavController()
    val tabs = listOf(InterviewerSessionsRoute, MessageRoute, SettingsRoute)
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in tabs.map { it::class.qualifiedName }

    Scaffold(
        bottomBar = {
            if (showBottomBar) InterviewerBottomBar(nav, tabs, currentRoute)
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = InterviewerSessionsRoute,
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            composable<InterviewerSessionsRoute> {
                // пока просто список сессий интервьюера
                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val sessionApi = remember {
                    authedRetrofit.create(SessionApi::class.java)
                }
                val sessionRepo = remember {
                    SessionRepositoryImpl(sessionApi)
                }

                val userApi = remember {
                    authedRetrofit.create(UserApi::class.java)
                }
                val userRepo = remember {
                    UserRepositoryImpl(userApi)
                }

                val vm = remember {
                    InterviewViewModel(
                        sessionRepo = sessionRepo,
                        userRepo = userRepo
                    )
                }

                InterviewerSessionsScreen(
                    viewModel = vm
                )
            }

            composable<MessageRoute> {
                MessageScreen()
            }

            composable<SettingsRoute> {
                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authApi = remember {
                    ApiClient.retrofit.create(AuthApi::class.java)
                }
                val authRepo = remember {
                    AuthRepositoryImpl(authApi, authLocal)
                }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val userApi = remember {
                    authedRetrofit.create(UserApi::class.java)
                }
                val userRepo = remember {
                    UserRepositoryImpl(userApi)
                }

                val settingsViewModel = remember {
                    SettingsViewModel(
                        userRepo = userRepo,
                        authRepo = authRepo
                    )
                }

                SettingsScreen(
                    viewModel = settingsViewModel,
                    onEditProfileClick = {
                        nav.navigate(EditProfileRoute)
                    },
                    onChangePasswordClick = {
                        nav.navigate(ChangePasswordRoute)
                    },
                    onLogoutClick = {
                        onLogout()
                    }
                )
            }

            composable<EditProfileRoute> {
                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val userApi = remember {
                    authedRetrofit.create(UserApi::class.java)
                }
                val userRepo = remember {
                    UserRepositoryImpl(userApi)
                }

                val vm = remember {
                    EditProfileViewModel(userRepo)
                }

                EditProfileScreen(
                    viewModel = vm,
                    onBack = { nav.popBackStack() }
                )
            }

            composable<ChangePasswordRoute> {
                val context = LocalContext.current
                val authLocal = remember { AuthLocalDataSource(context) }

                val authedRetrofit = remember {
                    ApiClient.authedRetrofit(authLocal)
                }
                val authApi = remember {
                    authedRetrofit.create(AuthApi::class.java)
                }
                val authRepo = remember {
                    AuthRepositoryImpl(authApi, authLocal)
                }

                val vm = remember {
                    ChangePasswordViewModel(authRepo)
                }

                ChangePasswordScreen(
                    viewModel = vm,
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun InterviewerBottomBar(
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
                        is InterviewerSessionsRoute -> if (selected) R.drawable.interview else R.drawable.interview2
                        is MessageRoute -> if (selected) R.drawable.message else R.drawable.message2
                        is SettingsRoute -> if (selected) R.drawable.settings else R.drawable.settings2
                        else -> R.drawable.interview
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
                            is InterviewerSessionsRoute -> "Sessions"
                            is MessageRoute -> "Message"
                            is SettingsRoute -> "Settings"
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
