package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.content.HadithRepository
import com.example.data.content.NamesOfAllahRepository
import com.example.data.dua.DuaRepository
import com.example.data.quran.QuranRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(
    onBack: () -> Unit,
    onSurahSelected: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val matchedSurahs = remember(query) {
        if (query.isBlank()) emptyList()
        else QuranRepository.ALL_114_SURAHS.filter {
            it.nameBangla.contains(query, ignoreCase = true) ||
            it.nameEnglish.contains(query, ignoreCase = true) ||
            it.banglaMeaning.contains(query, ignoreCase = true)
        }
    }

    val matchedDuas = remember(query) {
        if (query.isBlank()) emptyList()
        else DuaRepository.ALL_DUAS.filter {
            it.titleBn.contains(query, ignoreCase = true) ||
            it.banglaMeaning.contains(query, ignoreCase = true) ||
            it.banglaPronunciation.contains(query, ignoreCase = true)
        }
    }

    val matchedNames = remember(query) {
        if (query.isBlank()) emptyList()
        else NamesOfAllahRepository.NAMES.filter {
            it.transliteration.contains(query, ignoreCase = true) ||
            it.banglaMeaning.contains(query, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("সার্চ করুন", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("কুরআন, দোয়া বা আল্লাহর নাম খুঁজুন...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (matchedSurahs.isNotEmpty()) {
                    item {
                        Text(
                            text = "কুরআনের সূরাসমূহ (${matchedSurahs.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(matchedSurahs) { s ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSurahSelected(s.number) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "${s.number}. সূরা ${s.nameBangla} (${s.nameArabic})", fontWeight = FontWeight.Bold)
                                Text(text = "অর্থ: ${s.banglaMeaning}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                if (matchedDuas.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "দোয়াসমূহ (${matchedDuas.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(matchedDuas) { d ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = d.titleBn, fontWeight = FontWeight.Bold)
                                Text(text = d.banglaMeaning, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                if (matchedNames.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "আল্লাহর গুণবাচক নামসমূহ (${matchedNames.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(matchedNames) { n ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = n.transliteration, fontWeight = FontWeight.Bold)
                                    Text(text = n.banglaMeaning, style = MaterialTheme.typography.bodySmall)
                                }
                                Text(text = n.arabic, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
