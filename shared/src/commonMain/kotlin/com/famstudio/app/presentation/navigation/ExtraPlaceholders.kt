package com.famstudio.app.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.famstudio.app.presentation.theme.FamColors

@Composable
fun SearchPlaceholder(navController: NavHostController) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
        Text("🔍  Search coming soon", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = FamColors.TextMuted)
    }
}

@Composable
fun CartPlaceholder(navController: NavHostController) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
        Text("🛒  Cart coming soon", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = FamColors.TextMuted)
    }
}

@Composable
fun ProfilePlaceholder(navController: NavHostController) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
        Text("👤  Profile coming soon", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = FamColors.TextMuted)
    }
}