package com.gamedns.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gamedns.data.model.ConnectionState
import com.gamedns.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToGames: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "GameDNS",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToGames) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = "Games"
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!uiState.isFindingBest) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.findBestDns() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Find Best"
                        )
                    },
                    text = { Text("Find Best DNS") }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Connection Button Section
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ConnectButton(
                        isConnected = uiState.connectionState is ConnectionState.Connected,
                        isConnecting = uiState.connectionState is ConnectionState.Connecting,
                        onClick = { viewModel.onConnectClick() }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Current DNS Card
                    CurrentDnsCard(
                        dnsServerName = when (val state = uiState.connectionState) {
                            is ConnectionState.Connected -> state.stats.connectedDns?.name
                            else -> uiState.selectedDns?.name
                        }
                    )
                }
            }
            
            // Connection Stats
            if (uiState.connectionState is ConnectionState.Connected) {
                item {
                    StatsCard(
                        connectionStats = (uiState.connectionState as ConnectionState.Connected).stats
                    )
                }
            }
            
            // Error Message
            if (uiState.connectionState is ConnectionState.Error) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = (uiState.connectionState as ConnectionState.Error).message,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
            
            // DNS Servers List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available DNS Servers",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (uiState.isFindingBest) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
            
            // DNS Servers List
            items(
                items = uiState.dnsServers,
                key = { it.id }
            ) { dnsServer ->
                DnsCard(
                    dnsServer = dnsServer,
                    testResult = dnsServer.lastTestResult,
                    isSelected = uiState.selectedDns?.id == dnsServer.id,
                    isTesting = uiState.testingDnsId == dnsServer.id,
                    onCardClick = { viewModel.selectDns(dnsServer) },
                    onTestClick = { viewModel.testDnsSpeed(dnsServer) },
                    onConnectClick = {
                        viewModel.selectDns(dnsServer)
                        viewModel.onConnectClick()
                    }
                )
            }
            
            // Bottom Spacing for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
