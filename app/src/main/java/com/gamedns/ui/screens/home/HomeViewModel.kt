package com.gamedns.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamedns.data.model.*
import com.gamedns.domain.usecase.ConnectDnsUseCase
import com.gamedns.domain.usecase.TestDnsSpeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectDnsUseCase: ConnectDnsUseCase,
    private val testDnsSpeedUseCase: TestDnsSpeedUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadDnsServers()
        startConnectionMonitoring()
    }
    
    private fun loadDnsServers() {
        _uiState.value = _uiState.value.copy(
            dnsServers = PredefinedDnsServers.allServers
        )
    }
    
    fun onConnectClick() {
        val currentState = _uiState.value
        
        if (currentState.connectionState is ConnectionState.Connected) {
            disconnect()
        } else {
            currentState.selectedDns?.let { connect(it) }
        }
    }
    
    fun selectDns(dnsServer: DnsServer) {
        _uiState.value = _uiState.value.copy(selectedDns = dnsServer)
    }
    
    private fun connect(dnsServer: DnsServer) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    connectionState = ConnectionState.Connecting
                )
                
                // Simulate connection delay
                delay(1000)
                
                connectDnsUseCase.connect(dnsServer)
                
                _uiState.value = _uiState.value.copy(
                    connectionState = ConnectionState.Connected(
                        ConnectionStats(
                            connectedDns = dnsServer,
                            connectionStartTime = System.currentTimeMillis(),
                            isConnected = true
                        )
                    )
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    connectionState = ConnectionState.Error(
                        e.message ?: "Connection failed"
                    )
                )
            }
        }
    }
    
    private fun disconnect() {
        viewModelScope.launch {
            connectDnsUseCase.disconnect()
            
            _uiState.value = _uiState.value.copy(
                connectionState = ConnectionState.Disconnected
            )
        }
    }
    
    fun testDnsSpeed(dnsServer: DnsServer) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    testingDnsId = dnsServer.id
                )
                
                val result = testDnsSpeedUseCase.testSingle(dnsServer)
                
                // Update DNS server with test result
                val updatedServers = _uiState.value.dnsServers.map { dns ->
                    if (dns.id == dnsServer.id) {
                        dns.copy(lastTestResult = result)
                    } else {
                        dns
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    dnsServers = updatedServers,
                    testingDnsId = null
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    testingDnsId = null
                )
            }
        }
    }
    
    fun findBestDns() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isFindingBest = true
                )
                
                // Test all DNS servers
                val servers = _uiState.value.dnsServers
                val results = mutableListOf<Pair<DnsServer, DnsTestResult>>()
                
                servers.forEach { dns ->
                    _uiState.value = _uiState.value.copy(
                        testingDnsId = dns.id
                    )
                    
                    val result = testDnsSpeedUseCase.testSingle(dns)
                    results.add(dns to result)
                    
                    // Update UI with result
                    val updatedServers = _uiState.value.dnsServers.map { server ->
                        if (server.id == dns.id) {
                            server.copy(lastTestResult = result)
                        } else {
                            server
                        }
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        dnsServers = updatedServers
                    )
                }
                
                // Find best result
                val bestResult = results
                    .filter { it.second.isSuccess }
                    .minByOrNull { it.second.avgPing }
                
                bestResult?.let { (bestDns, _) ->
                    _uiState.value = _uiState.value.copy(
                        selectedDns = bestDns
                    )
                }
                
                _uiState.value = _uiState.value.copy(
                    testingDnsId = null,
                    isFindingBest = false
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    testingDnsId = null,
                    isFindingBest = false
                )
            }
        }
    }
    
    private fun startConnectionMonitoring() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                
                val currentState = _uiState.value.connectionState
                if (currentState is ConnectionState.Connected) {
                    val stats = currentState.stats
                    val duration = System.currentTimeMillis() - stats.connectionStartTime
                    
                    // Simulate ping monitoring (in production, you'd measure actual ping)
                    val currentPing = (10..50).random().toLong()
                    
                    _uiState.value = _uiState.value.copy(
                        connectionState = ConnectionState.Connected(
                            stats.copy(
                                connectionDuration = duration,
                                currentPing = currentPing
                            )
                        )
                    )
                }
            }
        }
    }
}

data class HomeUiState(
    val dnsServers: List<DnsServer> = emptyList(),
    val selectedDns: DnsServer? = null,
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val testingDnsId: Int? = null,
    val isFindingBest: Boolean = false
)
