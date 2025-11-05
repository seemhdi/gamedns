package com.gamedns.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameProfile(
    val id: Int,
    val name: String,
    val icon: String = "",
    val packageName: String = "",           // Android package name for detection
    val recommendedDns: List<Int>,          // DNS Server IDs
    val category: GameCategory,
    val description: String = "",
    val isInstalled: Boolean = false
) : Parcelable

@Parcelize
enum class GameCategory : Parcelable {
    FPS,                // First-Person Shooter
    MOBA,               // Multiplayer Online Battle Arena
    BATTLE_ROYALE,      // Battle Royale games
    RACING,             // Racing games
    SPORTS,             // Sports games
    RPG,                // Role-Playing Games
    STRATEGY,           // Strategy games
    OTHER               // Other categories
}

object PredefinedGames {
    
    val allGames = listOf(
        // FPS Games
        GameProfile(
            id = 1,
            name = "PUBG Mobile",
            icon = "pubg",
            packageName = "com.tencent.ig",
            recommendedDns = listOf(2, 1, 10),  // Cloudflare, Google, Cloudflare Gaming
            category = GameCategory.BATTLE_ROYALE,
            description = "Battle Royale game requiring low latency"
        ),
        GameProfile(
            id = 2,
            name = "Call of Duty Mobile",
            icon = "cod",
            packageName = "com.activision.callofduty.shooter",
            recommendedDns = listOf(2, 4, 1),  // Cloudflare, Quad9, Google
            category = GameCategory.FPS,
            description = "Fast-paced FPS game"
        ),
        GameProfile(
            id = 3,
            name = "Free Fire",
            icon = "freefire",
            packageName = "com.dts.freefireth",
            recommendedDns = listOf(1, 3, 2),  // Google, OpenDNS, Cloudflare
            category = GameCategory.BATTLE_ROYALE,
            description = "Popular battle royale game"
        ),
        
        // MOBA Games
        GameProfile(
            id = 4,
            name = "Mobile Legends",
            icon = "ml",
            packageName = "com.mobile.legends",
            recommendedDns = listOf(2, 1, 10),  // Cloudflare, Google, Cloudflare Gaming
            category = GameCategory.MOBA,
            description = "5v5 MOBA game requiring stable connection"
        ),
        GameProfile(
            id = 5,
            name = "Arena of Valor",
            icon = "aov",
            packageName = "com.garena.game.kgvn",
            recommendedDns = listOf(1, 4, 2),  // Google, Quad9, Cloudflare
            category = GameCategory.MOBA,
            description = "Competitive MOBA game"
        ),
        
        // Battle Royale
        GameProfile(
            id = 6,
            name = "Fortnite",
            icon = "fortnite",
            packageName = "com.epicgames.fortnite",
            recommendedDns = listOf(2, 1, 10),  // Cloudflare, Google, Cloudflare Gaming
            category = GameCategory.BATTLE_ROYALE,
            description = "Popular battle royale game"
        ),
        GameProfile(
            id = 7,
            name = "Apex Legends Mobile",
            icon = "apex",
            packageName = "com.ea.gp.apexlegendsmobilefps",
            recommendedDns = listOf(2, 4, 1),  // Cloudflare, Quad9, Google
            category = GameCategory.BATTLE_ROYALE,
            description = "Fast-paced battle royale"
        ),
        
        // Racing Games
        GameProfile(
            id = 8,
            name = "Asphalt 9",
            icon = "asphalt",
            packageName = "com.gameloft.android.ANMP.GloftA9HM",
            recommendedDns = listOf(1, 2, 3),  // Google, Cloudflare, OpenDNS
            category = GameCategory.RACING,
            description = "High-speed racing game"
        ),
        GameProfile(
            id = 9,
            name = "Real Racing 3",
            icon = "realracing",
            packageName = "com.ea.games.r3_row",
            recommendedDns = listOf(1, 3, 2),  // Google, OpenDNS, Cloudflare
            category = GameCategory.RACING,
            description = "Realistic racing simulation"
        ),
        
        // Sports Games
        GameProfile(
            id = 10,
            name = "FIFA Mobile",
            icon = "fifa",
            packageName = "com.ea.gp.fifamobile",
            recommendedDns = listOf(2, 1, 4),  // Cloudflare, Google, Quad9
            category = GameCategory.SPORTS,
            description = "Football simulation game"
        ),
        GameProfile(
            id = 11,
            name = "eFootball",
            icon = "efootball",
            packageName = "jp.konami.pesam",
            recommendedDns = listOf(1, 2, 3),  // Google, Cloudflare, OpenDNS
            category = GameCategory.SPORTS,
            description = "Football game by Konami"
        ),
        
        // RPG Games
        GameProfile(
            id = 12,
            name = "Genshin Impact",
            icon = "genshin",
            packageName = "com.miHoYo.GenshinImpact",
            recommendedDns = listOf(2, 1, 10),  // Cloudflare, Google, Cloudflare Gaming
            category = GameCategory.RPG,
            description = "Open-world action RPG"
        ),
        
        // Strategy Games
        GameProfile(
            id = 13,
            name = "Clash of Clans",
            icon = "coc",
            packageName = "com.supercell.clashofclans",
            recommendedDns = listOf(1, 2, 3),  // Google, Cloudflare, OpenDNS
            category = GameCategory.STRATEGY,
            description = "Strategy game with online battles"
        ),
        GameProfile(
            id = 14,
            name = "Clash Royale",
            icon = "clashroyale",
            packageName = "com.supercell.clashroyale",
            recommendedDns = listOf(2, 1, 4),  // Cloudflare, Google, Quad9
            category = GameCategory.STRATEGY,
            description = "Real-time strategy card game"
        )
    )
    
    fun getByCategory(category: GameCategory): List<GameProfile> {
        return allGames.filter { it.category == category }
    }
    
    fun getById(id: Int): GameProfile? {
        return allGames.find { it.id == id }
    }
    
    fun getByPackageName(packageName: String): GameProfile? {
        return allGames.find { it.packageName == packageName }
    }
}
