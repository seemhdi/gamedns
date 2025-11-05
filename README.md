# GameDNS - DNS Changer for Gaming

<div align="center">
  <h3>⚡ Fast & Reliable DNS Changer for Android Gaming ⚡</h3>
  <p>Change your DNS without root access using VPN mode</p>
</div>

## 📱 Features

### Core Features
- ✅ **VPN-based DNS Changer** - Change DNS without root access
- ✅ **DNS Speed Testing** - Test ping and performance of DNS servers
- ✅ **Auto-Find Best DNS** - Automatically find the fastest DNS for your network
- ✅ **Pre-configured DNS Servers** - Popular DNS servers ready to use
- ✅ **Custom DNS Support** - Add your own DNS servers

### Advanced Features
- ✅ **Real-time Connection Stats** - Monitor ping, duration, and data usage
- ✅ **Gaming Profiles** - Recommended DNS for popular games
- ✅ **Material Design 3** - Modern and beautiful UI
- ✅ **Dark/Light Theme** - Automatic theme switching
- ✅ **Performance Monitoring** - Track connection quality
- ✅ **Foreground Service** - Persistent VPN connection

## 🎮 Supported Games

The app includes optimized DNS recommendations for:

- **Battle Royale**: PUBG Mobile, Free Fire, Fortnite, Apex Legends
- **MOBA**: Mobile Legends, Arena of Valor
- **FPS**: Call of Duty Mobile
- **Racing**: Asphalt 9, Real Racing 3
- **Sports**: FIFA Mobile, eFootball
- **RPG**: Genshin Impact
- **Strategy**: Clash of Clans, Clash Royale

## 🌐 Pre-configured DNS Servers

### Global DNS
- **Google DNS** (8.8.8.8, 8.8.4.4)
- **Cloudflare DNS** (1.1.1.1, 1.0.0.1)
- **OpenDNS** (208.67.222.222, 208.67.220.220)
- **Quad9** (9.9.9.9, 149.112.112.112)
- **AdGuard DNS** (94.140.14.14, 94.140.15.15)

### Iran DNS
- **Shecan** (178.22.122.100, 185.51.200.2)
- **403 Online** (10.202.10.202, 10.202.10.102)
- **Electro** (78.157.42.100, 78.157.42.101)
- **Radar Game** (10.202.10.10, 10.202.10.11)

### Gaming DNS
- **Cloudflare Gaming** (1.1.1.1, 1.0.0.1)
- **Google Gaming** (8.8.8.8, 8.8.4.4)

## 🏗️ Architecture

The app follows **Clean Architecture** principles with MVVM pattern:

```
GameDNS/
├── data/           # Data models and repositories
├── domain/         # Business logic and use cases
├── service/        # VPN and DNS testing services
└── ui/             # Jetpack Compose UI
    ├── theme/      # Material Design 3 theme
    ├── components/ # Reusable UI components
    └── screens/    # App screens
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Async**: Kotlin Coroutines + Flow
- **Database**: Room (for future features)
- **Preferences**: DataStore
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## 📦 Build Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/GameDNS.git
cd GameDNS
```

2. **Open in Android Studio**
- Open Android Studio
- Select "Open an Existing Project"
- Navigate to the GameDNS folder

3. **Build the project**
```bash
./gradlew build
```

4. **Run on device/emulator**
- Connect your Android device or start an emulator
- Click "Run" in Android Studio or use:
```bash
./gradlew installDebug
```

## 🔧 How It Works

### VPN Service
GameDNS uses Android's `VpnService` API to create a local VPN interface. This allows the app to:
1. Intercept all DNS queries
2. Route them through selected DNS servers
3. Work without root access

### DNS Testing
The app tests DNS performance by:
1. Performing multiple DNS lookups
2. Measuring response time
3. Calculating average, min, and max ping
4. Detecting packet loss

### Connection Flow
```
User Selects DNS → VPN Permission Check → Create VPN Interface
→ Configure DNS Servers → Establish Connection → Monitor Stats
```

## 📱 Usage

1. **Launch the app**
2. **Select a DNS server** from the list
3. **Test speed** (optional) to check performance
4. **Tap Connect** to activate the DNS
5. **Monitor stats** in real-time
6. **Tap Disconnect** when done

### Auto-Find Best DNS
1. Tap the **"Find Best DNS"** button
2. The app will test all DNS servers
3. Automatically select the fastest one
4. Review results and connect

### Gaming Mode
1. Navigate to **"Popular Games"**
2. Select your game
3. Tap recommended DNS to connect
4. Enjoy optimized gaming experience

## 🎨 UI Screenshots

The app features:
- **Modern Material Design 3** interface
- **Dynamic color** support (Android 12+)
- **Dark/Light theme** with smooth transitions
- **Gaming-inspired colors** (Green, Blue, Purple)
- **Smooth animations** and transitions

## 🔐 Privacy & Security

- ✅ **No logging** - We don't log your DNS queries or browsing activity
- ✅ **Local processing** - All operations happen on your device
- ✅ **No ads** - Clean, ad-free experience
- ✅ **Open source ready** - Transparent code for community review

## ⚡ Performance

- **Lightweight** - Minimal battery and data usage
- **Fast connection** - Quick DNS switching
- **Stable** - Reliable VPN service
- **Optimized** - Efficient background processing

## 🐛 Known Issues

- DNS testing doesn't force specific DNS server (limitation of Android API)
- Some DNS servers may not work on certain networks
- VPN permission required on first use

## 🔮 Future Features

- [ ] DNS over HTTPS (DoH) support
- [ ] DNS over TLS (DoT) support
- [ ] Connection history
- [ ] Export/Import settings
- [ ] Widget support
- [ ] Tasker integration
- [ ] Split tunneling
- [ ] Per-app DNS settings

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

If you encounter any issues or have questions:
- Open an issue on GitHub
- Contact: support@gamedns.com

## 🙏 Acknowledgments

- Material Design 3 by Google
- Android VpnService API
- Jetpack Compose community
- All DNS providers

## ⭐ Show Your Support

If you find this app useful, please give it a star ⭐ on GitHub!

---

<div align="center">
  Made with ❤️ for gamers
</div>
