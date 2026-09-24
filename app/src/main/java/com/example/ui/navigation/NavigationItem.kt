package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val titleBn: String, val titleEn: String, val iconSelected: ImageVector, val iconUnselected: ImageVector) {
    object Home : Screen("home", "হোম", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Quran : Screen("quran", "কুরআন", "Quran", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
    object Dua : Screen("dua", "দোয়া", "Dua", Icons.Filled.VolunteerActivism, Icons.Outlined.VolunteerActivism)
    object Qibla : Screen("qibla", "কিবলা", "Qibla", Icons.Filled.Explore, Icons.Outlined.Explore)
    object More : Screen("more", "অন্যান্য", "More", Icons.Filled.GridView, Icons.Outlined.GridView)

    // Sub-screens
    object QuranReader : Screen("quran_reader/{surahNumber}", "কুরআন পাঠ", "Quran Reader", Icons.Filled.MenuBook, Icons.Outlined.MenuBook) {
        fun createRoute(surahNumber: Int) = "quran_reader/$surahNumber"
    }
    object SalahTracker : Screen("salah_tracker", "সালাত ট্র্যাকার", "Salah Tracker", Icons.Filled.FactCheck, Icons.Outlined.FactCheck)
    object Tasbih : Screen("tasbih", "তাসবিহ", "Tasbih", Icons.Filled.Fingerprint, Icons.Outlined.Fingerprint)
    object NamazGuide : Screen("namaz_guide", "নামাজ শিক্ষা", "Namaz Guide", Icons.Filled.AutoStories, Icons.Outlined.AutoStories)
    object NamesOfAllah : Screen("names_99", "৯৯ গুণবাচক নাম", "99 Names of Allah", Icons.Filled.Stars, Icons.Outlined.Stars)
    object Ramadan : Screen("ramadan", "রমজান মোড", "Ramadan Mode", Icons.Filled.Nightlight, Icons.Outlined.Nightlight)
    object CalendarScreen : Screen("calendar", "হিজরি ক্যালেন্ডার", "Islamic Calendar", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth)
    object HadithScreen : Screen("hadith", "হাদিস ভাণ্ডার", "Hadith Collection", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks)
    object GlobalSearch : Screen("search", "সার্চ", "Global Search", Icons.Filled.Search, Icons.Outlined.Search)
    object Settings : Screen("settings", "সেটিংস", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    object About : Screen("about", "অ্যাপ পরিচিতি", "About App", Icons.Filled.Info, Icons.Outlined.Info)
}

val BOTTOM_NAV_ITEMS = listOf(
    Screen.Home,
    Screen.Quran,
    Screen.Dua,
    Screen.Qibla,
    Screen.More
)
