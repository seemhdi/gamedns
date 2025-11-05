package com.gamedns.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gamedns.data.model.GameCategory
import com.gamedns.data.model.GameProfile
import com.gamedns.ui.theme.*

@Composable
fun GameCard(
    game: GameProfile,
    recommendedDnsNames: List<String>,
    onConnectClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Game Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = game.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    if (game.description.isNotEmpty()) {
                        Text(
                            text = game.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Category Badge
                GameCategoryBadge(category = game.category)
            }
            
            // Recommended DNS Section
            if (recommendedDnsNames.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Recommended DNS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // DNS Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recommendedDnsNames.take(3).forEachIndexed { index, dnsName ->
                        DnsChip(
                            dnsName = dnsName,
                            isTop = index == 0,
                            onClick = { 
                                game.recommendedDns.getOrNull(index)?.let { dnsId ->
                                    onConnectClick(dnsId)
                                }
                            }
                        )
                    }
                }
            }
            
            // Install Status
            if (game.isInstalled) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Installed",
                        tint = SuccessGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Installed",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun GameCategoryBadge(category: GameCategory) {
    val (text, color) = when (category) {
        GameCategory.FPS -> "FPS" to ErrorRed
        GameCategory.MOBA -> "MOBA" to GamingPurple
        GameCategory.BATTLE_ROYALE -> "Battle Royale" to GamingBlue
        GameCategory.RACING -> "Racing" to WarningYellow
        GameCategory.SPORTS -> "Sports" to GamingGreen
        GameCategory.RPG -> "RPG" to GamingPurpleLight
        GameCategory.STRATEGY -> "Strategy" to InfoBlue
        GameCategory.OTHER -> "Other" to Color.Gray
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DnsChip(
    dnsName: String,
    isTop: Boolean,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isTop) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Top",
                        modifier = Modifier.size(14.dp),
                        tint = WarningYellow
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = dnsName,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isTop) 
                WarningYellow.copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    )
}
