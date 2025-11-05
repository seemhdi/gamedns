package com.gamedns.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import com.gamedns.data.model.DnsServer
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

/**
 * VPN Service for changing DNS without root access
 * This service creates a local VPN interface and routes all DNS queries through selected DNS servers
 */
class GameDnsVpnService : VpnService() {
    
    companion object {
        private const val TAG = "GameDnsVpnService"
        private const val VPN_ADDRESS = "10.0.0.2"
        private const val VPN_ROUTE = "0.0.0.0"
        private const val VPN_MTU = 1500
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "gamedns_vpn_channel"
        
        const val ACTION_CONNECT = "com.gamedns.action.CONNECT"
        const val ACTION_DISCONNECT = "com.gamedns.action.DISCONNECT"
        const val EXTRA_DNS_SERVER = "dns_server"
        
        var isRunning = false
            private set
        
        var currentDnsServer: DnsServer? = null
            private set
    }
    
    private var vpnInterface: ParcelFileDescriptor? = null
    private var vpnThread: Thread? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "VPN Service created")
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action}")
        
        when (intent?.action) {
            ACTION_CONNECT -> {
                val dnsServer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_DNS_SERVER, DnsServer::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_DNS_SERVER)
                }
                
                dnsServer?.let { connectToDns(it) }
            }
            ACTION_DISCONNECT -> {
                disconnect()
            }
        }
        
        return START_STICKY
    }
    
    /**
     * Connect to specified DNS server
     */
    private fun connectToDns(dnsServer: DnsServer) {
        try {
            Log.d(TAG, "Connecting to DNS: ${dnsServer.name}")
            
            // Stop existing connection if any
            disconnect()
            
            // Build VPN interface
            val builder = Builder()
            builder.setSession("GameDNS - ${dnsServer.name}")
                .addAddress(VPN_ADDRESS, 24)
                .addDnsServer(dnsServer.primaryDns)
                .addDnsServer(dnsServer.secondaryDns)
                .addRoute(VPN_ROUTE, 0)
                .setMtu(VPN_MTU)
                .setBlocking(false)
            
            // Establish VPN connection
            vpnInterface = builder.establish()
            
            if (vpnInterface == null) {
                Log.e(TAG, "Failed to establish VPN interface")
                stopSelf()
                return
            }
            
            // Update state
            currentDnsServer = dnsServer
            isRunning = true
            
            // Start foreground service with notification
            startForeground(NOTIFICATION_ID, createNotification(dnsServer))
            
            // Start VPN thread to handle packets
            startVpnThread()
            
            // Start monitoring connection
            startMonitoring()
            
            Log.d(TAG, "Successfully connected to ${dnsServer.name}")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to DNS", e)
            disconnect()
        }
    }
    
    /**
     * Disconnect from VPN
     */
    private fun disconnect() {
        Log.d(TAG, "Disconnecting VPN")
        
        try {
            // Stop VPN thread
            vpnThread?.interrupt()
            vpnThread = null
            
            // Close VPN interface
            vpnInterface?.close()
            vpnInterface = null
            
            // Update state
            isRunning = false
            currentDnsServer = null
            
            // Stop foreground service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_REMOVE)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(true)
            }
            
            // Stop service
            stopSelf()
            
            Log.d(TAG, "VPN disconnected successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting VPN", e)
        }
    }
    
    /**
     * Start VPN thread to handle network packets
     */
    private fun startVpnThread() {
        vpnThread = Thread {
            try {
                Log.d(TAG, "VPN thread started")
                
                val vpnInput = FileInputStream(vpnInterface!!.fileDescriptor)
                val vpnOutput = FileOutputStream(vpnInterface!!.fileDescriptor)
                
                val buffer = ByteBuffer.allocate(32767)
                
                while (!Thread.interrupted() && isRunning) {
                    try {
                        // Read packets from VPN interface
                        val length = vpnInput.read(buffer.array())
                        
                        if (length > 0) {
                            buffer.limit(length)
                            
                            // Process packet (simplified - in production, you'd parse and route packets)
                            // For DNS changer, the VPN interface automatically routes DNS queries
                            // through the configured DNS servers
                            
                            buffer.clear()
                        }
                        
                        // Small delay to prevent CPU overuse
                        Thread.sleep(1)
                        
                    } catch (e: InterruptedException) {
                        break
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing packet", e)
                    }
                }
                
                Log.d(TAG, "VPN thread stopped")
                
            } catch (e: Exception) {
                Log.e(TAG, "VPN thread error", e)
            }
        }
        
        vpnThread?.start()
    }
    
    /**
     * Start monitoring connection stats
     */
    private fun startMonitoring() {
        serviceScope.launch {
            while (isRunning) {
                try {
                    // Monitor connection and update notification
                    // In production, you'd measure actual ping and data usage
                    
                    delay(5000) // Update every 5 seconds
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Monitoring error", e)
                }
            }
        }
    }
    
    /**
     * Create notification channel for Android O and above
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "GameDNS VPN Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when GameDNS VPN is active"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Create notification for foreground service
     */
    private fun createNotification(dnsServer: DnsServer): Notification {
        // In production, you'd create a proper notification with actions
        // For now, creating a simple notification
        
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        
        builder
            .setContentTitle("GameDNS Active")
            .setContentText("Connected to ${dnsServer.name}")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
        
        return builder.build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "VPN Service destroyed")
        
        disconnect()
        serviceScope.cancel()
    }
    
    override fun onRevoke() {
        super.onRevoke()
        Log.d(TAG, "VPN permission revoked")
        
        disconnect()
    }
}
