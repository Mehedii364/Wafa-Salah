package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.dua.DuaItem
import com.example.data.dua.DuaRepository
import com.example.data.local.PersonalDua
import com.example.ui.components.ShareCardDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = remember { WafaApplication.instance.database }
    val personalDuas by db.personalDuaDao().getAllDuas().collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var isPersonalTabSelected by remember { mutableStateOf(false) }

    var showAddPersonalDialog by remember { mutableStateOf(false) }
    var shareCardData by remember { mutableStateOf<Triple<String, String, String>?>(null) }

    // Dialog state
    var newTitle by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }
    var newNotes by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Personal") }

    val filteredDuas = remember(selectedCategory, searchQuery) {
        DuaRepository.ALL_DUAS.filter { dua ->
            val matchCategory = selectedCategory == "All" || dua.category.equals(selectedCategory, ignoreCase = true)
            val matchSearch = searchQuery.isBlank() ||
                    dua.titleBn.contains(searchQuery, ignoreCase = true) ||
                    dua.titleEn.contains(searchQuery, ignoreCase = true) ||
                    dua.banglaMeaning.contains(searchQuery, ignoreCase = true) ||
                    dua.banglaPronunciation.contains(searchQuery, ignoreCase = true)
            matchCategory && matchSearch
        }
    }

    Scaffold(
        floatingActionButton = {
            if (isPersonalTabSelected) {
                ExtendedFloatingActionButton(
                    onClick = { showAddPersonalDialog = true },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                    text = { Text("নতুন দোয়া লিখুন") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Main Top Tabs: "মাসনূন দোয়া" vs "আমার দোয়া বই"
            TabRow(
                selectedTabIndex = if (isPersonalTabSelected) 1 else 0,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = !isPersonalTabSelected,
                    onClick = { isPersonalTabSelected = false },
                    text = { Text("মাসনূন দোয়া") }
                )
                Tab(
                    selected = isPersonalTabSelected,
                    onClick = { isPersonalTabSelected = true },
                    text = { Text("ব্যক্তিগত দোয়া (${personalDuas.size})") }
                )
            }

            if (!isPersonalTabSelected) {
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("দোয়া খুঁজুন...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                // Category Chips Scroll Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == "All",
                            onClick = { selectedCategory = "All" },
                            label = { Text("সবগুলো") }
                        )
                    }
                    items(DuaRepository.CATEGORIES) { (nameBn, nameEn) ->
                        FilterChip(
                            selected = selectedCategory.equals(nameEn, ignoreCase = true) ||
                                    (nameEn == "Morning & Evening" && (selectedCategory == "Morning" || selectedCategory == "Evening")),
                            onClick = {
                                selectedCategory = if (nameEn == "Morning & Evening") "Morning" else nameEn
                            },
                            label = { Text(nameBn) }
                        )
                    }
                }

                // Dua list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredDuas, key = { it.id }) { dua ->
                        DuaCard(
                            dua = dua,
                            onShare = {
                                shareCardData = Triple(
                                    dua.titleBn,
                                    dua.arabic,
                                    dua.banglaMeaning
                                )
                            }
                        )
                    }
                }
            } else {
                // Personal Dua Book
                if (personalDuas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "আপনার কোনো ব্যক্তিগত দোয়া নেই",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = "নিচের বাটনে ট্যাপ করে নতুন মনের দোয়া যোগ করুন",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(personalDuas, key = { it.id }) { pDua ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        ) {
                                            Text(
                                                text = pDua.category,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    db.personalDuaDao().deleteDua(pDua)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.DeleteOutline,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = pDua.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = pDua.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    if (pDua.notes.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "নোট: ${pDua.notes}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Personal Dua Dialog
    if (showAddPersonalDialog) {
        AlertDialog(
            onDismissRequest = { showAddPersonalDialog = false },
            title = { Text("নতুন ব্যক্তিগত দোয়া যোগ করুন") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("শিরোনাম") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newText,
                        onValueChange = { newText = it },
                        label = { Text("দোয়ার কথা/টেক্সট") },
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newNotes,
                        onValueChange = { newNotes = it },
                        label = { Text("বিশেষ নোট (ঐচ্ছিক)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Category selector chips
                    val categories = listOf("Personal", "Family", "Study", "Travel", "Other")
                    Text("ক্যাটেগরি:", style = MaterialTheme.typography.labelSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = newCategory == cat,
                                onClick = { newCategory = cat },
                                label = { Text(cat, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newText.isNotBlank()) {
                            coroutineScope.launch {
                                db.personalDuaDao().insertDua(
                                    PersonalDua(
                                        title = newTitle,
                                        text = newText,
                                        notes = newNotes,
                                        category = newCategory
                                    )
                                )
                                newTitle = ""
                                newText = ""
                                newNotes = ""
                                showAddPersonalDialog = false
                            }
                        }
                    }
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPersonalDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // Share Card Dialog
    shareCardData?.let { (title, arabic, meaning) ->
        ShareCardDialog(
            title = title,
            arabicText = arabic,
            banglaText = meaning,
            reference = "দোয়া ও মুনাজাত • Wafa Salah Tracker",
            onDismiss = { shareCardData = null }
        )
    }
}

@Composable
fun DuaCard(
    dua: DuaItem,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = dua.titleBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = dua.titleEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic text
            Text(
                text = dua.arabic,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, lineHeight = 32.sp),
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Bangla Pronunciation
            Text(
                text = "উচ্চারণ: ${dua.banglaPronunciation}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Bangla Meaning
            Text(
                text = "অর্থ: ${dua.banglaMeaning}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Reference
            Text(
                text = "সূত্র: ${dua.reference}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
