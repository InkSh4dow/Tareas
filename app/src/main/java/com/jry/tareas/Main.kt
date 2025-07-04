package com.jry.tareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF228B22), onPrimary = Color.White, secondary = Color(0xFF228B22),
    onSecondary = Color.White, surface = Color(0xFF181A20), onSurface = Color.White, background = Color(0xFF121212)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF66BB6A), onPrimary = Color.Black, secondary = Color(0xFF66BB6A),
    onSecondary = Color.White, surface = Color.White, onSurface = Color.Black, background = Color(0xFFF2F2F2)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppNavigation() }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val darkMode by remember { Datos(context).darkMode }.collectAsStateWithLifecycle(false)
    val colorScheme = if (darkMode) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TipografiaApp
    ) {
        val navController = rememberNavController()
        AppNavGraph(navController = navController)
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = "home",
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) + fadeOut() }
    ) {
        composable("home") { Home(navController) }
        composable("addTask") { AddTaskScreen(navController) }
        composable("search") { SearchScreen(navController) }
        composable("addImage") { PlaceholderScreen("Añadir Imagen", navController) }
        composable("addTable") { PlaceholderScreen("Añadir Tabla", navController) }
        composable(
            route = "taskDetail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("taskId")?.let { taskId ->
                TaskDetailScreen(navController, taskId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(title: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    modifier = Modifier
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Pantalla para '$title'")
        }
    }
}
