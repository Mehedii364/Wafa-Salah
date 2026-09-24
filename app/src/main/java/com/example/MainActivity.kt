package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.prayer.CityData
import com.example.data.preferences.UserPreferences
import com.example.ui.components.AudioPlayerBottomBar
import com.example.ui.navigation.BOTTOM_NAV_ITEMS
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.WafaTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val prefsRepo = remember { WafaApplication.instance.preferencesRepository }
            val userPrefs by prefsRepo.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())

            val isDarkTheme = when (userPrefs.themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            WafaTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                // Notification permission launcher
                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { /* handle granted or denied */ }

                // Location permission launcher
                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    if (fineGranted || coarseGranted) {
                        try {
                            val fusedLocation = LocationServices.getFusedLocationProviderClient(this@MainActivity)
                            fusedLocation.lastLocation.addOnSuccessListener { loc ->
                                if (loc != null) {
                                    coroutineScope.launch {
                                        prefsRepo.updateLocation(
                                            "আমার অবস্থান (GPS)",
                                            loc.latitude,
                                            loc.longitude
                                        )
                                    }
                                }
                            }
                        } catch (e: SecurityException) {
                            e.printStackTrace()
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                    if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }

                val showBottomNav = currentRoute in BOTTOM_NAV_ITEMS.map { it.route }

                Scaffold(
                    bottomBar = {
                        Column {
                            // Persistent Media Player Bar
                            AudioPlayerBottomBar()

                            if (showBottomNav) {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 4.dp
                                ) {
                                    BOTTOM_NAV_ITEMS.forEach { screen ->
                                        val isSelected = currentRoute == screen.route
                                        NavigationBarItem(
                                            selected = isSelected,
                                            onClick = {
                                                if (currentRoute != screen.route) {
                                                    navController.navigate(screen.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = if (isSelected) screen.iconSelected else screen.iconUnselected,
                                                    contentDescription = screen.titleBn
                                                )
                                            },
                                            label = {
                                                Text(
                                                    text = screen.titleBn,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            colors = NavigationBarItemDefaults.colors(
                                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                                selectedTextColor = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Home
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onNavigateToQuran = { surahNum ->
                                    navController.navigate(Screen.QuranReader.createRoute(surahNum))
                                },
                                onNavigateToDua = { navController.navigate(Screen.Dua.route) },
                                onNavigateToQibla = { navController.navigate(Screen.Qibla.route) },
                                onNavigateToTasbih = { navController.navigate(Screen.Tasbih.route) },
                                onNavigateToSalahTracker = { navController.navigate(Screen.SalahTracker.route) }
                            )
                        }

                        // 2. Quran
                        composable(Screen.Quran.route) {
                            QuranListScreen(
                                onSurahSelected = { surahNum ->
                                    navController.navigate(Screen.QuranReader.createRoute(surahNum))
                                }
                            )
                        }

                        // 3. Dua
                        composable(Screen.Dua.route) {
                            DuaScreen()
                        }

                        // 4. Qibla
                        composable(Screen.Qibla.route) {
                            QiblaScreen()
                        }

                        // 5. More
                        composable(Screen.More.route) {
                            MoreScreen(
                                onNavigateToNamazGuide = { navController.navigate(Screen.NamazGuide.route) },
                                onNavigateTo99Names = { navController.navigate(Screen.NamesOfAllah.route) },
                                onNavigateToRamadan = { navController.navigate(Screen.Ramadan.route) },
                                onNavigateToCalendar = { navController.navigate(Screen.CalendarScreen.route) },
                                onNavigateToHadith = { navController.navigate(Screen.HadithScreen.route) },
                                onNavigateToSearch = { navController.navigate(Screen.GlobalSearch.route) },
                                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                                onNavigateToAbout = { navController.navigate(Screen.About.route) },
                                onNavigateToTasbih = { navController.navigate(Screen.Tasbih.route) },
                                onNavigateToSalahTracker = { navController.navigate(Screen.SalahTracker.route) }
                            )
                        }

                        // Sub-destinations
                        composable(
                            route = Screen.QuranReader.route,
                            arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
                            QuranReaderScreen(
                                surahNumber = surahNumber,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.SalahTracker.route) {
                            SalahTrackerScreen()
                        }

                        composable(Screen.Tasbih.route) {
                            TasbihScreen()
                        }

                        composable(Screen.NamazGuide.route) {
                            NamazGuideScreen(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.NamesOfAllah.route) {
                            NamesOfAllahScreen(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.Ramadan.route) {
                            RamadanScreen(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.CalendarScreen.route) {
                            CalendarScreen(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.HadithScreen.route) {
                            HadithScreen(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.GlobalSearch.route) {
                            GlobalSearchScreen(
                                onBack = { navController.popBackStack() },
                                onSurahSelected = { surahNum ->
                                    navController.navigate(Screen.QuranReader.createRoute(surahNum))
                                }
                            )
                        }

                        composable(Screen.Settings.route) {
                            SettingsScreen(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.About.route) {
                            AboutScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
