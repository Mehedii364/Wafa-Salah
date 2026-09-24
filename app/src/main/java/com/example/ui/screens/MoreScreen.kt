package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MoreScreen(
    onNavigateToNamazGuide: () -> Unit,
    onNavigateTo99Names: () -> Unit,
    onNavigateToRamadan: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToHadith: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToSalahTracker: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "ইসলামিক ফিচারসমূহ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.AutoStories,
                title = "নামাজ ও ওযু শিক্ষা (সহীহ পদ্ধতি)",
                subtitle = "পুরুষ ও মহিলাদের নামাজের পূর্ণাঙ্গ নিয়ম ও দোয়াসমূহ",
                onClick = onNavigateToNamazGuide
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.FactCheck,
                title = "সালাত ট্র্যাকার ও পরিসংখ্যান",
                subtitle = "দৈনিক ৫ ওয়াক্ত সালাত রেকর্ড ও সাপ্তাহিক রিপোর্ট",
                onClick = onNavigateToSalahTracker
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.Fingerprint,
                title = "ডিজিটাল তাসবিহ গণক",
                subtitle = "হ্যাপটিক ভাইব্রেশন ও কাস্টম যিকির লক্ষ্যমাত্রা",
                onClick = onNavigateToTasbih
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.Stars,
                title = "আসমাউল হুসনা (আল্লাহর ৯৯ নাম)",
                subtitle = "আরবি, বাংলা ও ইংরেজি অর্থসহ ফজিলত",
                onClick = onNavigateTo99Names
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.Nightlight,
                title = "রমজান ড্যাশবোর্ড",
                subtitle = "সেহরি ও ইফতারের সময়সূচি, রোজা ট্র্যাকার ও দোয়া",
                onClick = onNavigateToRamadan
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.CalendarMonth,
                title = "হিজরি ইসলামিক ক্যালেন্ডার",
                subtitle = "গ্রেগরিয়ান ও হিজরি তারিখ এবং প্রধান ইসলামিক দিবস",
                onClick = onNavigateToCalendar
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.LibraryBooks,
                title = "সহীহ হাদিস ভাণ্ডার",
                subtitle = "দৈনন্দিন জীবনের সহীহ বুখারী ও মুসলিমের নির্বাচিত হাদিস",
                onClick = onNavigateToHadith
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.Search,
                title = "গ্লোবাল সার্চ",
                subtitle = "কুরআন সূরা, আয়াত, দোয়া ও হাদিস দ্রুত খুঁজুন",
                onClick = onNavigateToSearch
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "অ্যাপ্লিকেশন সেটিংস ও পরিচিতি",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.Settings,
                title = "সেটিংস",
                subtitle = "ভাষা, থিম, হিসাব পদ্ধতি, আজান ও নোটিফিকেশন",
                onClick = onNavigateToSettings
            )
        }

        item {
            MoreMenuItem(
                icon = Icons.Filled.Info,
                title = "Wafa Salah Tracker পরিচিতি",
                subtitle = "Wafa Zone by Mehedi364 • সংস্করণ ১.০",
                onClick = onNavigateToAbout
            )
        }
    }
}

@Composable
fun MoreMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}
