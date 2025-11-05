package com.gamedns.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gamedns.data.model.DnsServer
import com.gamedns.data.model.DnsTestResult
import com.gamedns.data.model.DnsQuality
import com.gamedns.ui.theme.*

@Composable
fun DnsCard(
    dnsServer: DnsServer,
    testResult: DnsTestResult? = null,
    isSelected: Boolean = false,
    isTesting: Boolean = false,
    onCardClick: () -> Unit,
    onTestClick: () -> Unit,
    onConnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // DNS Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dnsServer.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = dnsServer.primaryDns,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (dnsServer.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dnsServer.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Category Badge
                CategoryBadge(category = dnsServer.category)
            }
            
            // Test Result Section
            if (testResult != null || isTesting) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))
                
                if (isTesting) {
                    TestingIndicator()
                } else if (testResult != null) {
                    TestResultDisplay(testResult = testResult)
                }
            }
            
            // Action Buttons
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Test Speed Button
                OutlinedButton(
                    onClick = onTestClick,
                    modifier = Modifier.weight(1f),
                    enabled = !isTesting
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Test Speed",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Test")
                }
                
                // Connect Button
                Button(
                    onClick = onConnectClick,
                    modifier = Modifier.weight(1f),
                    enabled = !isTesting
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                        contentDescription = "Connect",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isSelected) "Connected" else "Connect")
                }
            }
        }
    }
}

@Composable
private fun CategoryBadge(category: com.gamedns.data.model.DnsCategory) {
    val (text, color) = when (category) {
        com.gamedns.data.model.DnsCategory.GLOBAL -> "Global" to GamingBlue
        com.gamedns.data.model.DnsCategory.IRAN -> "Iran" to GamingPurple
        com.gamedns.data.model.DnsCategory.GAMING -> "Gaming" to GamingGreen
        com.gamedns.data.model.DnsCategory.CUSTOM -> "Custom" to Color.Gray
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
private fun TestingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Testing speed...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TestResultDisplay(testResult: DnsTestResult) {
    val quality = testResult.getQualityRating()
    val qualityColor = when (quality) {
        DnsQuality.EXCELLENT -> ExcellentColor
        DnsQuality.GOOD -> GoodColor
        DnsQuality.FAIR -> FairColor
        DnsQuality.POOR -> PoorColor
        DnsQuality.VERY_POOR -> VeryPoorColor
        DnsQuality.FAILED -> FailedColor
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ping Display
        Column {
            Text(
                text = "Ping",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = if (testResult.isSuccess) "${testResult.avgPing}" else "—",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = qualityColor
                )
                if (testResult.isSuccess) {
                    Text(
                        text = "ms",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                }
            }
        }
        
        // Quality Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(qualityColor.copy(alpha = 0.2f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = quality.name,
                style = MaterialTheme.typography.labelMedium,
                color = qualityColor,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Min/Max Ping
        if (testResult.isSuccess) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Min: ${testResult.minPing}ms",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Max: ${testResult.maxPing}ms",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
