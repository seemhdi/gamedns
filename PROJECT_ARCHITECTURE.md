# معماری برنامه Game DNS

## نام پروژه: GameDNS - DNS Changer for Gaming

## ساختار کلی پروژه

```
GameDNS/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gamedns/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── DnsServer.kt
│   │   │   │   │   │   ├── DnsTestResult.kt
│   │   │   │   │   │   ├── ConnectionStats.kt
│   │   │   │   │   │   └── GameProfile.kt
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── DnsRepository.kt
│   │   │   │   │   │   └── StatsRepository.kt
│   │   │   │   │   └── local/
│   │   │   │   │       ├── DnsDatabase.kt
│   │   │   │   │       ├── DnsDao.kt
│   │   │   │   │       └── PreferencesManager.kt
│   │   │   │   ├── domain/
│   │   │   │   │   ├── usecase/
│   │   │   │   │   │   ├── ConnectDnsUseCase.kt
│   │   │   │   │   │   ├── TestDnsSpeedUseCase.kt
│   │   │   │   │   │   ├── GetBestDnsUseCase.kt
│   │   │   │   │   │   └── GetStatsUseCase.kt
│   │   │   │   │   └── util/
│   │   │   │   │       ├── DnsHelper.kt
│   │   │   │   │       └── NetworkUtils.kt
│   │   │   │   ├── service/
│   │   │   │   │   ├── GameDnsVpnService.kt
│   │   │   │   │   └── DnsTestService.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── DnsCard.kt
│   │   │   │   │   │   ├── ConnectButton.kt
│   │   │   │   │   │   ├── StatsCard.kt
│   │   │   │   │   │   └── GameCard.kt
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── home/
│   │   │   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   │   │   └── HomeViewModel.kt
│   │   │   │   │   │   ├── dnslist/
│   │   │   │   │   │   │   ├── DnsListScreen.kt
│   │   │   │   │   │   │   └── DnsListViewModel.kt
│   │   │   │   │   │   ├── stats/
│   │   │   │   │   │   │   ├── StatsScreen.kt
│   │   │   │   │   │   │   └── StatsViewModel.kt
│   │   │   │   │   │   ├── games/
│   │   │   │   │   │   │   ├── GamesScreen.kt
│   │   │   │   │   │   │   └── GamesViewModel.kt
│   │   │   │   │   │   └── settings/
│   │   │   │   │   │       ├── SettingsScreen.kt
│   │   │   │   │   │       └── SettingsViewModel.kt
│   │   │   │   │   └── navigation/
│   │   │   │   │       └── NavGraph.kt
│   │   │   │   ├── di/
│   │   │   │   │   ├── AppModule.kt
│   │   │   │   │   ├── DatabaseModule.kt
│   │   │   │   │   └── RepositoryModule.kt
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   ├── drawable/
│   │   │   │   └── mipmap/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## معماری Clean Architecture

### 1. Data Layer
**مسئولیت**: مدیریت داده‌ها و منابع خارجی

**کامپوننت‌ها**:
- **Models**: کلاس‌های داده (DnsServer, DnsTestResult, ConnectionStats, GameProfile)
- **Repository**: واسط دسترسی به داده
- **Local Storage**: Room Database + DataStore

**مدل‌های داده**:

```kotlin
// DnsServer.kt
data class DnsServer(
    val id: Int,
    val name: String,
    val primaryDns: String,
    val secondaryDns: String,
    val icon: Int,
    val category: DnsCategory,
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false
)

enum class DnsCategory {
    GLOBAL,      // Google, Cloudflare, etc.
    IRAN,        // Shecan, 403, Electro
    GAMING,      // Gaming-optimized DNS
    CUSTOM       // User-added
}

// DnsTestResult.kt
data class DnsTestResult(
    val dnsServerId: Int,
    val avgPing: Long,      // milliseconds
    val minPing: Long,
    val maxPing: Long,
    val packetLoss: Float,  // percentage
    val timestamp: Long,
    val isSuccess: Boolean
)

// ConnectionStats.kt
data class ConnectionStats(
    val connectedDns: DnsServer?,
    val connectionTime: Long,
    val currentPing: Long,
    val dataUsed: Long,
    val isConnected: Boolean
)

// GameProfile.kt
data class GameProfile(
    val id: Int,
    val name: String,
    val icon: Int,
    val recommendedDns: List<Int>,  // DNS Server IDs
    val category: GameCategory
)

enum class GameCategory {
    FPS,        // PUBG, COD, etc.
    MOBA,       // Mobile Legends, etc.
    BATTLE_ROYALE,
    RACING,
    SPORTS,
    OTHER
}
```

### 2. Domain Layer
**مسئولیت**: منطق کسب‌وکار

**Use Cases**:
- `ConnectDnsUseCase`: اتصال به DNS انتخابی
- `TestDnsSpeedUseCase`: تست سرعت DNS
- `GetBestDnsUseCase`: یافتن بهترین DNS
- `GetStatsUseCase`: دریافت آمار اتصال

### 3. Presentation Layer (UI)
**مسئولیت**: نمایش داده و تعامل با کاربر

**صفحات اصلی**:

#### Home Screen
- دکمه بزرگ Connect/Disconnect
- نمایش DNS فعلی
- نمایش پینگ real-time
- دسترسی سریع به تنظیمات

#### DNS List Screen
- لیست تمام DNSهای موجود
- دسته‌بندی (Global, Iran, Gaming, Custom)
- دکمه Test Speed برای هر DNS
- نمایش نتایج تست (پینگ، وضعیت)
- امکان افزودن DNS سفارشی
- مرتب‌سازی بر اساس سرعت

#### Stats Screen
- نمودار مصرف داده
- نمودار پینگ در طول زمان
- زمان اتصال
- تاریخچه اتصالات

#### Games Screen
- لیست بازی‌های محبوب
- DNS پیشنهادی برای هر بازی
- امکان اتصال مستقیم

#### Settings Screen
- تم (Light/Dark/Auto)
- زبان
- نوتیفیکیشن‌ها
- اتصال خودکار
- درباره برنامه

### 4. Service Layer
**مسئولیت**: سرویس‌های پس‌زمینه

**GameDnsVpnService**:
```kotlin
class GameDnsVpnService : VpnService() {
    
    private var vpnInterface: ParcelFileDescriptor? = null
    private var isRunning = false
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dnsServer = intent?.getParcelableExtra<DnsServer>("dns_server")
        dnsServer?.let { connectToDns(it) }
        return START_STICKY
    }
    
    private fun connectToDns(dnsServer: DnsServer) {
        val builder = Builder()
        builder.setSession("GameDNS")
            .addAddress("10.0.0.2", 24)
            .addDnsServer(dnsServer.primaryDns)
            .addDnsServer(dnsServer.secondaryDns)
            .addRoute("0.0.0.0", 0)
            .setMtu(1500)
            .setBlocking(false)
        
        vpnInterface = builder.establish()
        isRunning = true
        
        // Start foreground notification
        startForeground(NOTIFICATION_ID, createNotification(dnsServer))
        
        // Start monitoring
        startMonitoring()
    }
    
    private fun startMonitoring() {
        // Monitor connection stats
        // Update notification with current ping
    }
}
```

**DnsTestService**:
```kotlin
class DnsTestService {
    
    suspend fun testDnsSpeed(dnsServer: DnsServer): DnsTestResult {
        val results = mutableListOf<Long>()
        var successCount = 0
        
        repeat(5) {
            val startTime = System.currentTimeMillis()
            val isSuccess = performDnsQuery(dnsServer.primaryDns)
            val endTime = System.currentTimeMillis()
            
            if (isSuccess) {
                results.add(endTime - startTime)
                successCount++
            }
        }
        
        return if (results.isNotEmpty()) {
            DnsTestResult(
                dnsServerId = dnsServer.id,
                avgPing = results.average().toLong(),
                minPing = results.minOrNull() ?: 0,
                maxPing = results.maxOrNull() ?: 0,
                packetLoss = ((5 - successCount) / 5f) * 100,
                timestamp = System.currentTimeMillis(),
                isSuccess = true
            )
        } else {
            DnsTestResult(
                dnsServerId = dnsServer.id,
                avgPing = 0,
                minPing = 0,
                maxPing = 0,
                packetLoss = 100f,
                timestamp = System.currentTimeMillis(),
                isSuccess = false
            )
        }
    }
    
    private suspend fun performDnsQuery(dnsServer: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val address = InetAddress.getByName("www.google.com")
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}
```

## تکنولوژی‌های استفاده شده

### Core
- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### UI
- **Jetpack Compose**: Modern UI toolkit
- **Material Design 3**: Latest design system
- **Compose Navigation**: Navigation between screens
- **Accompanist**: Additional Compose utilities

### Architecture
- **MVVM**: Model-View-ViewModel pattern
- **Clean Architecture**: Separation of concerns
- **Hilt**: Dependency injection
- **Kotlin Coroutines**: Asynchronous programming
- **Flow**: Reactive streams

### Data
- **Room**: Local database
- **DataStore**: Preferences storage
- **Gson**: JSON serialization

### Network
- **OkHttp**: HTTP client
- **Retrofit**: REST client (if needed)

### Testing
- **JUnit**: Unit testing
- **Mockk**: Mocking framework
- **Compose UI Test**: UI testing

## لیست DNS پیش‌فرض

### Global DNS Servers
1. **Google Public DNS**
   - Primary: 8.8.8.8
   - Secondary: 8.8.4.4

2. **Cloudflare DNS**
   - Primary: 1.1.1.1
   - Secondary: 1.0.0.1

3. **OpenDNS**
   - Primary: 208.67.222.222
   - Secondary: 208.67.220.220

4. **Quad9**
   - Primary: 9.9.9.9
   - Secondary: 149.112.112.112

5. **AdGuard DNS**
   - Primary: 94.140.14.14
   - Secondary: 94.140.15.15

### Iran DNS Servers
1. **Shecan**
   - Primary: 178.22.122.100
   - Secondary: 185.51.200.2

2. **403 Online**
   - Primary: 10.202.10.202
   - Secondary: 10.202.10.102

3. **Electro**
   - Primary: 78.157.42.100
   - Secondary: 78.157.42.101

4. **Radar Game**
   - Primary: 10.202.10.10
   - Secondary: 10.202.10.11

### Gaming DNS (Optimized)
1. **Cloudflare Gaming**
   - Primary: 1.1.1.1
   - Secondary: 1.0.0.1

2. **Google Gaming**
   - Primary: 8.8.8.8
   - Secondary: 8.8.4.4

## لیست بازی‌های محبوب

### FPS Games
- PUBG Mobile → Recommended: Cloudflare, Google
- Call of Duty Mobile → Recommended: Cloudflare, Quad9
- Free Fire → Recommended: Google, OpenDNS

### MOBA Games
- Mobile Legends → Recommended: Cloudflare, Google
- Arena of Valor → Recommended: Google, Quad9

### Battle Royale
- Fortnite → Recommended: Cloudflare, Google
- Apex Legends Mobile → Recommended: Cloudflare, Quad9

### Racing
- Asphalt 9 → Recommended: Google, Cloudflare
- Real Racing 3 → Recommended: Google, OpenDNS

### Sports
- FIFA Mobile → Recommended: Cloudflare, Google
- eFootball → Recommended: Google, Cloudflare

## طراحی رنگ (Material You)

### Light Theme
- **Primary**: #00C853 (Green)
- **Secondary**: #00B0FF (Blue)
- **Tertiary**: #7C4DFF (Purple)
- **Background**: #FFFFFF
- **Surface**: #F5F5F5

### Dark Theme
- **Primary**: #00E676 (Light Green)
- **Secondary**: #40C4FF (Light Blue)
- **Tertiary**: #B388FF (Light Purple)
- **Background**: #121212
- **Surface**: #1E1E1E

### Gaming Accent Colors
- **Success**: #00E676
- **Warning**: #FFD600
- **Error**: #FF1744
- **Info**: #00B0FF

## فلوچارت اتصال

```
[User Opens App]
    ↓
[Check VPN Permission] → [Request Permission]
    ↓                         ↓
[Show DNS List]          [Permission Granted?]
    ↓                         ↓
[User Selects DNS]       [Yes] → [Continue]
    ↓                    [No] → [Show Error]
[Optional: Test Speed]
    ↓
[User Clicks Connect]
    ↓
[Start VPN Service]
    ↓
[Configure VPN Builder]
    ↓
[Establish VPN Interface]
    ↓
[Show Connected Status]
    ↓
[Monitor Connection]
    ↓
[Update Stats Real-time]
    ↓
[User Clicks Disconnect]
    ↓
[Stop VPN Service]
    ↓
[Show Disconnected Status]
```

## نوتیفیکیشن‌ها

### Foreground Service Notification
- عنوان: "GameDNS Active"
- متن: "Connected to [DNS Name]"
- آیکون: لوگوی برنامه
- اکشن‌ها: Disconnect, Show Stats

### Status Updates
- اتصال موفق
- قطع اتصال
- خطا در اتصال
- تست سرعت کامل شد

## مجوزهای لازم

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

## ویژگی‌های امنیتی

1. **No Logging**: هیچ لاگی از فعالیت کاربر ذخیره نمی‌شود
2. **Local Processing**: تمام پردازش‌ها روی دستگاه انجام می‌شود
3. **No Ads**: بدون تبلیغات
4. **Open Source Ready**: آماده برای انتشار به صورت متن‌باز

## Performance Optimization

1. **Lazy Loading**: بارگذاری تنها داده‌های مورد نیاز
2. **Caching**: کش کردن نتایج تست
3. **Background Processing**: استفاده از Coroutines
4. **Memory Management**: مدیریت صحیح حافظه
5. **Battery Optimization**: بهینه‌سازی مصرف باتری
