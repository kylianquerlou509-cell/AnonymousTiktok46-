package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.math.cos

// Theme Enumeration for color scheme overrides
enum class ModTheme(
    val displayName: String,
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val glow: Color,
    val darkBg: Color
) {
    GALAXY("Galaxy Purple", Color(0xFFBD00FF), Color(0xFFFF007F), Color(0xFFD5A6FF), Color(0xFFFF00D6), Color(0xFF090314)),
    GRAVITY("Gravity Gold", Color(0xFFFFD700), Color(0xFFFFA500), Color(0xFFFFF3A6), Color(0xFFFFEA00), Color(0xFF0C0A00)),
    MATRIX("Matrix Green", Color(0xFF00FF41), Color(0xFF008F11), Color(0xFF90FF90), Color(0xFF00FF00), Color(0xFF000501)),
    CYBER("Cyber Cyan", Color(0xFF00F0FF), Color(0xFF0072FF), Color(0xFFA6FAFF), Color(0xFF00E1FF), Color(0xFF000A14)),
    LAVA("Lava Red", Color(0xFFFF3333), Color(0xFF880000), Color(0xFFFF9999), Color(0xFFFF1A1A), Color(0xFF0D0000))
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    ModMenuSimulator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ModMenuSimulator(modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }
    var loadingText by remember { mutableStateOf("Connexion au serveur One State RP...") }
    val coroutineScope = rememberCoroutineScope()

    // Interactive mod configurations
    var selectedTheme by remember { mutableStateOf(ModTheme.GALAXY) }
    var selectedTab by remember { mutableStateOf("COMBAT") }
    var isMenuExpanded by remember { mutableStateOf(true) }

    // Toggle states
    var aimbotEnabled by remember { mutableStateOf(false) }
    var wallhackEnabled by remember { mutableStateOf(false) }
    var shootThroughWallsEnabled by remember { mutableStateOf(false) }
    var visibilityCheckEnabled by remember { mutableStateOf(false) }
    var aimLockEnabled by remember { mutableStateOf(false) }
    var autoFollowEnabled by remember { mutableStateOf(false) }
    var stickyCarEnabled by remember { mutableStateOf(false) }

    // Visuels toggles
    var espEnabled by remember { mutableStateOf(false) }
    var espLineEnabled by remember { mutableStateOf(false) }
    var espBoxEnabled by remember { mutableStateOf(false) }
    var espSkeletonEnabled by remember { mutableStateOf(false) }
    var showDistanceEnabled by remember { mutableStateOf(false) }
    var showNamesEnabled by remember { mutableStateOf(false) }
    var espThickness by remember { mutableStateOf(2f) }

    // Monde toggles
    var lunarGravity by remember { mutableStateOf(false) }
    var speedhack by remember { mutableStateOf(false) }
    var frogJump by remember { mutableStateOf(false) }
    var noClip by remember { mutableStateOf(false) }
    var timeMultiplier by remember { mutableStateOf(1.0f) }
    var selectedWeather by remember { mutableStateOf("SOLEIL") }

    // Spawn / Teleport states
    var activeLogs = remember { mutableStateListOf<String>() }
    var selectedVehicleToSpawn by remember { mutableStateOf("") }
    var selectedWeaponToGive by remember { mutableStateOf("") }

    // Loading Simulation
    LaunchedEffect(Unit) {
        val messages = listOf(
            "Connexion au serveur One State RP...",
            "Vérification de l'accès VIP pour AnonymousTikTok46...",
            "Contournement de la sécurité EasyAntiCheat...",
            "Injection des fichiers binaires du Mod Galaxy...",
            "Initialisation du Menu VIP AnonymousTikTok46..."
        )
        var msgIdx = 0
        while (progress < 1.0f) {
            delay(120)
            progress += 0.03f
            if (progress >= 1.0f) {
                progress = 1.0f
            }
            if (progress > (msgIdx + 1) * 0.2f && msgIdx < messages.size - 1) {
                msgIdx++
                loadingText = messages[msgIdx]
            }
        }
        delay(400)
        isLoading = false
        activeLogs.add("[CONSOLE] Injecté avec succès par AnonymousTikTok46 !")
        activeLogs.add("[LOG] Mod Menu Galaxy opérationnel.")
    }

    // Main layout
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Base textured cosmic game background (low opacity for depth)
        Image(
            painter = painterResource(id = R.drawable.img_galaxy_bg_1782491552233),
            contentDescription = "Galaxy base texture",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.25f
        )

        // Custom real-time dynamic animated galaxy particle background (slow stars + glowing nebulae)
        DynamicGalaxyBackground(
            modifier = Modifier.fillMaxSize(),
            theme = selectedTheme
        )

        // Loading Screen Overlay
        AnimatedVisibility(
            visible = isLoading,
            exit = fadeOut(animationSpec = tween(600))
        ) {
            LoadingScreen(
                progress = progress,
                statusText = loadingText,
                theme = selectedTheme
            )
        }

        // Active Game State (visible after load)
        if (!isLoading) {
            // Simulated Game Overlay (HUD)
            GameHUDOverlay(
                theme = selectedTheme,
                isMenuExpanded = isMenuExpanded,
                onToggleMenu = { isMenuExpanded = !isMenuExpanded }
            )

            // Mod Menu Modal Overlay
            AnimatedVisibility(
                visible = isMenuExpanded,
                enter = slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(600, easing = EaseOutCubic)
                ) + fadeIn(animationSpec = tween(600)),
                exit = slideOutVertically(
                    targetOffsetY = { it / 3 },
                    animationSpec = tween(500, easing = EaseInCubic)
                ) + fadeOut(animationSpec = tween(500)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                ModMenuCard(
                    theme = selectedTheme,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onClose = { isMenuExpanded = false },
                    aimbotEnabled = aimbotEnabled,
                    onAimbotToggle = {
                        aimbotEnabled = it
                        logAction(activeLogs, "Aimbot", it)
                    },
                    shootThroughWallsEnabled = shootThroughWallsEnabled,
                    onShootThroughWallsToggle = {
                        shootThroughWallsEnabled = it
                        logAction(activeLogs, "Tirer à travers murs", it)
                    },
                    visibilityCheckEnabled = visibilityCheckEnabled,
                    onVisibilityCheckToggle = {
                        visibilityCheckEnabled = it
                        logAction(activeLogs, "Vérif de visibilité", it)
                    },
                    aimLockEnabled = aimLockEnabled,
                    onAimLockToggle = {
                        aimLockEnabled = it
                        logAction(activeLogs, "AimLock (Caméra)", it)
                    },
                    autoFollowEnabled = autoFollowEnabled,
                    onAutoFollowToggle = {
                        autoFollowEnabled = it
                        logAction(activeLogs, "Suivre auto", it)
                    },
                    stickyCarEnabled = stickyCarEnabled,
                    onStickyCarToggle = {
                        stickyCarEnabled = it
                        logAction(activeLogs, "Coller voiture cible", it)
                    },
                    espEnabled = espEnabled,
                    onEspToggle = {
                        espEnabled = it
                        logAction(activeLogs, "ESP", it)
                    },
                    espLineEnabled = espLineEnabled,
                    onEspLineToggle = {
                        espLineEnabled = it
                        logAction(activeLogs, "ESP Lignes", it)
                    },
                    espBoxEnabled = espBoxEnabled,
                    onEspBoxToggle = {
                        espBoxEnabled = it
                        logAction(activeLogs, "ESP Boîtes 2D", it)
                    },
                    espSkeletonEnabled = espSkeletonEnabled,
                    onEspSkeletonToggle = {
                        espSkeletonEnabled = it
                        logAction(activeLogs, "ESP Squelettes", it)
                    },
                    showDistanceEnabled = showDistanceEnabled,
                    onShowDistanceToggle = {
                        showDistanceEnabled = it
                        logAction(activeLogs, "Affichage Distance", it)
                    },
                    showNamesEnabled = showNamesEnabled,
                    onShowNamesToggle = {
                        showNamesEnabled = it
                        logAction(activeLogs, "Affichage Noms", it)
                    },
                    espThickness = espThickness,
                    onEspThicknessChange = { espThickness = it },
                    lunarGravity = lunarGravity,
                    onLunarGravityToggle = {
                        lunarGravity = it
                        logAction(activeLogs, "Gravité Lunaire", it)
                    },
                    speedhack = speedhack,
                    onSpeedhackToggle = {
                        speedhack = it
                        logAction(activeLogs, "Speedhack", it)
                    },
                    frogJump = frogJump,
                    onFrogJumpToggle = {
                        frogJump = it
                        logAction(activeLogs, "Saut de Grenouille", it)
                    },
                    noClip = noClip,
                    onNoClipToggle = {
                        noClip = it
                        logAction(activeLogs, "NoClip (Passer à travers)", it)
                    },
                    timeMultiplier = timeMultiplier,
                    onTimeMultiplierChange = { timeMultiplier = it },
                    selectedWeather = selectedWeather,
                    onWeatherChange = {
                        selectedWeather = it
                        activeLogs.add("[WORLD] Météo changée en $it !")
                    },
                    selectedVehicle = selectedVehicleToSpawn,
                    onSpawnVehicle = {
                        selectedVehicleToSpawn = it
                        activeLogs.add("[SPAWN] $it généré près d'AnonymousTikTok46 !")
                    },
                    selectedWeapon = selectedWeaponToGive,
                    onGiveWeapon = {
                        selectedWeaponToGive = it
                        activeLogs.add("[ITEM] Arme $it ajoutée à l'inventaire !")
                    },
                    onTriggerAction = { action ->
                        activeLogs.add("[VIP] Action déclenchée: $action")
                    },
                    activeTheme = selectedTheme,
                    onThemeChange = { selectedTheme = it },
                    activeLogs = activeLogs
                )
            }
        }
    }
}

fun logAction(logs: MutableList<String>, name: String, enabled: Boolean) {
    logs.add("[OPTION] $name: ${if (enabled) "ACTIVÉ" else "DESACTIVÉ"}")
}

// ----------------- SUB-COMPOSABLES & DESIGN PIECES -----------------

@Composable
fun LoadingScreen(
    progress: Float,
    statusText: String,
    theme: ModTheme
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF06030B)),
        contentAlignment = Alignment.Center
    ) {
        // Cosmic Stars Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val random = java.util.Random(42)
            for (i in 0..100) {
                val x = random.nextFloat() * size.width
                val y = random.nextFloat() * size.height
                val radius = random.nextFloat() * 2.5f.dp.toPx()
                val opacity = random.nextFloat()
                drawCircle(
                    color = Color.White,
                    radius = radius,
                    center = Offset(x, y),
                    alpha = opacity
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Animated Glowing Logo
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.95f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.scale(scale)
            ) {
                // Outer Blur/Glow
                Text(
                    text = "ANONYMOUS VIP",
                    color = theme.glow.copy(alpha = 0.4f),
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp
                )
                // Inner Glowing text
                Text(
                    text = "ANONYMOUS VIP",
                    color = Color.White,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp,
                    modifier = Modifier.drawBehind {
                        // Drawing a futuristic sci-fi underline
                        val thickness = 4.dp.toPx()
                        val y = size.height + 8.dp.toPx()
                        drawLine(
                            brush = Brush.linearGradient(
                                colors = listOf(theme.primary, theme.secondary)
                            ),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = thickness
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Subtitle
            Text(
                text = "CONVERTED GALAXY MOD APK BY ANONYMOUSTIKTOK46",
                color = theme.accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Progress Bar (Matches the progress bar at the bottom of screenshot)
            Column(
                modifier = Modifier.width(420.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$statusText (${(progress * 100).toInt()}%)",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                // Beautiful custom track & thumb progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(theme.primary, theme.secondary)
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .drawBehind {
                                // Add a glowing dot at the end of the progress
                                if (progress > 0.02f) {
                                    drawCircle(
                                        color = Color.White,
                                        radius = 6.dp.toPx(),
                                        center = Offset(size.width, size.height / 2)
                                    )
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun GameHUDOverlay(
    theme: ModTheme,
    isMenuExpanded: Boolean,
    onToggleMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Top Center Widget: "ID AnonymousTikTok46 TP"
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(theme.primary, theme.secondary)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left pill indicator
                Text(
                    text = "ID",
                    color = theme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .background(theme.primary.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                // Center name
                Text(
                    text = "AnonymousTikTok46",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.drawBehind {
                        // Soft glow aura underneath name
                    }
                )

                // Right pill indicator
                Text(
                    text = "TP",
                    color = theme.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .background(theme.secondary.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Live Simulated Game Stats on Top Left
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text("FPS: 60", color = Color(0xFF00FF41), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            Text("PING: 14 ms", color = Color(0xFF00FF41), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            Text("VIP ACTIVE", color = theme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }

        // Floating trigger controller button to collapse/expand menu (Bottom Left)
        Button(
            onClick = onToggleMenu,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black.copy(alpha = 0.85f)
            ),
            shape = CircleShape,
            border = BorderStroke(
                2.dp,
                Brush.linearGradient(listOf(theme.primary, theme.secondary))
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(54.dp)
                .testTag("toggle_menu_button")
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Mini controller custom design or text
                Text(
                    text = if (isMenuExpanded) "CLOSE" else "OPEN",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ModMenuCard(
    theme: ModTheme,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onClose: () -> Unit,
    aimbotEnabled: Boolean,
    onAimbotToggle: (Boolean) -> Unit,
    shootThroughWallsEnabled: Boolean,
    onShootThroughWallsToggle: (Boolean) -> Unit,
    visibilityCheckEnabled: Boolean,
    onVisibilityCheckToggle: (Boolean) -> Unit,
    aimLockEnabled: Boolean,
    onAimLockToggle: (Boolean) -> Unit,
    autoFollowEnabled: Boolean,
    onAutoFollowToggle: (Boolean) -> Unit,
    stickyCarEnabled: Boolean,
    onStickyCarToggle: (Boolean) -> Unit,
    espEnabled: Boolean,
    onEspToggle: (Boolean) -> Unit,
    espLineEnabled: Boolean,
    onEspLineToggle: (Boolean) -> Unit,
    espBoxEnabled: Boolean,
    onEspBoxToggle: (Boolean) -> Unit,
    espSkeletonEnabled: Boolean,
    onEspSkeletonToggle: (Boolean) -> Unit,
    showDistanceEnabled: Boolean,
    onShowDistanceToggle: (Boolean) -> Unit,
    showNamesEnabled: Boolean,
    onShowNamesToggle: (Boolean) -> Unit,
    espThickness: Float,
    onEspThicknessChange: (Float) -> Unit,
    lunarGravity: Boolean,
    onLunarGravityToggle: (Boolean) -> Unit,
    speedhack: Boolean,
    onSpeedhackToggle: (Boolean) -> Unit,
    frogJump: Boolean,
    onFrogJumpToggle: (Boolean) -> Unit,
    noClip: Boolean,
    onNoClipToggle: (Boolean) -> Unit,
    timeMultiplier: Float,
    onTimeMultiplierChange: (Float) -> Unit,
    selectedWeather: String,
    onWeatherChange: (String) -> Unit,
    selectedVehicle: String,
    onSpawnVehicle: (String) -> Unit,
    selectedWeapon: String,
    onGiveWeapon: (String) -> Unit,
    onTriggerAction: (String) -> Unit,
    activeTheme: ModTheme,
    onThemeChange: (ModTheme) -> Unit,
    activeLogs: androidx.compose.runtime.snapshots.SnapshotStateList<String>
) {
    // Elegant translucent dark panel with glowing theme border
    Card(
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .fillMaxHeight(0.85f)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(theme.primary, theme.secondary)
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.88f)
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Card Header (Identical to Screenshots)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Brand block
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Custom Discord-like Controller Symbol via Canvas
                    Canvas(modifier = Modifier.size(24.dp)) {
                        drawCircle(color = theme.primary, radius = size.width / 2)
                        // Cut lines
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width * 0.3f, size.height * 0.5f),
                            end = Offset(size.width * 0.7f, size.height * 0.5f),
                            strokeWidth = 3.dp.toPx()
                        )
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width * 0.5f, size.height * 0.3f),
                            end = Offset(size.width * 0.5f, size.height * 0.7f),
                            strokeWidth = 3.dp.toPx()
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "ANONYMOUS MOD",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Middle Info Title block (Centered metadata)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ONE STATE VIP",
                        color = theme.accent,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Développé par AnonymousTikTok46",
                        color = theme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "v26.06.24.0107 - Expire: 30/06/2026",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Right tiktok/social block
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onTriggerAction("TikTok Visit") }
                ) {
                    // Musical note representation for TikTok
                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(size.width * 0.4f, size.height * 0.7f))
                        drawLine(color = Color.White, start = Offset(size.width * 0.55f, size.height * 0.2f), end = Offset(size.width * 0.55f, size.height * 0.7f), strokeWidth = 2.dp.toPx())
                        drawLine(color = Color.White, start = Offset(size.width * 0.55f, size.height * 0.2f), end = Offset(size.width * 0.85f, size.height * 0.1f), strokeWidth = 2.dp.toPx())
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "@AnonymousTikTok46",
                        color = theme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(theme.primary.copy(alpha = 0.2f)))

            // Dynamic Navigation Tabs Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("COMBAT", "VISUELS", "MONDE", "CATALOGUE", "TELEPORTS", "COULEURS").forEach { tab ->
                    val isActive = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .clickable { onTabSelected(tab) }
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .drawBehind {
                                if (isActive) {
                                    // Custom high fidelity tab background highlight (Pentagonal block / pill hybrid)
                                    drawRoundRect(
                                        color = theme.primary.copy(alpha = 0.15f),
                                        size = size,
                                        cornerRadius = CornerRadius(8.dp.toPx())
                                    )
                                    // Glow bottom line
                                    drawRect(
                                        color = theme.secondary,
                                        topLeft = Offset(0f, size.height - 3.dp.toPx()),
                                        size = Size(size.width, 3.dp.toPx())
                                    )
                                }
                            }
                    ) {
                        Text(
                            text = tab,
                            color = if (isActive) theme.accent else Color.White.copy(alpha = 0.6f),
                            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(theme.primary.copy(alpha = 0.2f)))

            // Content Panel (Dual-column layout)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Left Column: Scrollable controls
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.1f)
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (selectedTab) {
                        "COMBAT" -> {
                            ModToggleRow("Activer Aimbot", aimbotEnabled, onAimbotToggle, theme)
                            ModToggleRow("Tirer a travers les murs", shootThroughWallsEnabled, onShootThroughWallsToggle, theme)
                            ModToggleRow("Verification de visibilite", visibilityCheckEnabled, onVisibilityCheckToggle, theme)
                            ModToggleRow("AimLock (Camera)", aimLockEnabled, onAimLockToggle, theme)
                            ModToggleRow("Suivre Joueur / Voiture Auto", autoFollowEnabled, onAutoFollowToggle, theme)
                            ModToggleRow("Coller Voiture sur Cible (Sticky Car)", stickyCarEnabled, onStickyCarToggle, theme)
                        }
                        "VISUELS" -> {
                            ModToggleRow("Wallhack global (ESP)", espEnabled, onEspToggle, theme)
                            ModToggleRow("Tracer des lignes de visée", espLineEnabled, onEspLineToggle, theme)
                            ModToggleRow("Boîtes d'encadrement 2D", espBoxEnabled, onEspBoxToggle, theme)
                            ModToggleRow("Afficher Squelettes 3D", espSkeletonEnabled, onEspSkeletonToggle, theme)
                            ModToggleRow("Afficher la distance (mètres)", showDistanceEnabled, onShowDistanceToggle, theme)
                            ModToggleRow("Afficher le nom des joueurs", showNamesEnabled, onShowNamesToggle, theme)

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Épaisseur des lignes ESP: ${espThickness.toInt()}px",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Slider(
                                value = espThickness,
                                onValueChange = onEspThicknessChange,
                                valueRange = 1f..10f,
                                colors = SliderDefaults.colors(
                                    thumbColor = theme.primary,
                                    activeTrackColor = theme.secondary,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                                )
                            )
                        }
                        "MONDE" -> {
                            ModToggleRow("Gravité Lunaire (Sauts lents)", lunarGravity, onLunarGravityToggle, theme)
                            ModToggleRow("Super Vitesse (Speedhack x5)", speedhack, onSpeedhackToggle, theme)
                            ModToggleRow("Saut de Grenouille géant", frogJump, onFrogJumpToggle, theme)
                            ModToggleRow("NoClip (Traverser murs/décors)", noClip, onNoClipToggle, theme)

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Vitesse temporelle (Temps réel): ${"%.1f".format(timeMultiplier)}x",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Slider(
                                value = timeMultiplier,
                                onValueChange = onTimeMultiplierChange,
                                valueRange = 0.5f..5.0f,
                                colors = SliderDefaults.colors(
                                    thumbColor = theme.primary,
                                    activeTrackColor = theme.secondary,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Changer la météo locale:", color = theme.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("SOLEIL", "PLUIE", "TEMPETE", "GALAXY").forEach { weather ->
                                    val isSel = selectedWeather == weather
                                    Button(
                                        onClick = { onWeatherChange(weather) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSel) theme.primary else Color.White.copy(alpha = 0.05f)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(weather, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                        "CATALOGUE" -> {
                            Text("VIP Vehicle Spawner (Gratuit)", color = theme.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            val vehicles = listOf("Bugatti Chiron", "Lamborghini Aventador", "Ferrari LaFerrari", "Tesla Cybertruck", "Toyota Supra RZ", "Batmobile Concept")
                            vehicles.forEach { car ->
                                Button(
                                    onClick = { onSpawnVehicle(car) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedVehicle == car) theme.secondary else Color.White.copy(alpha = 0.05f)
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(34.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(car, color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                        Text(if (selectedVehicle == car) "SPAWNED!" else "SPAWN", color = theme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Générateur d'Armes VIP", color = theme.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            val weapons = listOf("M4A1 Galaxy Edition", "AK47 Neon Fire", "Sniper Barrett .50", "Desert Eagle Gold")
                            weapons.forEach { weapon ->
                                Button(
                                    onClick = { onGiveWeapon(weapon) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedWeapon == weapon) theme.secondary else Color.White.copy(alpha = 0.05f)
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(34.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(weapon, color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                        Text("OBTENIR", color = theme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        "TELEPORTS" -> {
                            Text("Points de Téléportation Rapide", color = theme.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            val points = listOf(
                                "Téléportation à l'Aéroport" to "Aéroport",
                                "Téléportation au Commissariat" to "Police",
                                "Téléportation au Concessionnaire VIP" to "Concessionnaire",
                                "Téléportation à la Banque Centrale" to "Banque",
                                "Téléportation à la Villa de luxe" to "Villa",
                                "Téléportation au Point GPS" to "PointGPS"
                            )
                            points.forEach { (label, act) ->
                                Button(
                                    onClick = { onTriggerAction("Teleport: $act") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(34.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(label, color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                        Text("TP INSTANT", color = theme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        "COULEURS" -> {
                            Text("Personnalisation de la Couleur du Mod", color = theme.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Choisissez votre thème néon préféré:", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, modifier = Modifier.padding(bottom = 6.dp))

                            ModTheme.values().forEach { mTheme ->
                                val isSelected = activeTheme == mTheme
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onThemeChange(mTheme) }
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) mTheme.primary else Color.White.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) mTheme.primary.copy(alpha = 0.1f) else Color.Black
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            // Tiny color dot preview
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .background(mTheme.primary, CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = mTheme.displayName,
                                                color = if (isSelected) mTheme.accent else Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        if (isSelected) {
                                            Text("ACTIF", color = mTheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Spacer separating left controls and middle visualization
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(theme.primary.copy(alpha = 0.2f))
                )

                // Middle/Right column: Interactive Visual Animation or Live Console logs
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.9f)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (selectedTab == "COMBAT" || selectedTab == "COULEURS") {
                        // Centered Branding Layout with Sine Waves (Exactly as requested by user!)
                        Spacer(modifier = Modifier.weight(0.1f))

                        // Sine Wave Animation 1 (Top Wave)
                        SineWaveWaveform(color = theme.primary, amplitude = 10f, speedMultiplier = 1.0f)

                        Spacer(modifier = Modifier.height(12.dp))

                        // Glowing Main Name Text
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val infiniteTransition = rememberInfiniteTransition(label = "text_glow")
                            val textGlowAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.5f,
                                targetValue = 1.0f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1500, easing = EaseInOutSine),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "textGlowAlpha"
                            )

                            Box(contentAlignment = Alignment.Center) {
                                // Double outer text shadows for glowing volumetric galaxy aura
                                Text(
                                    text = "ANONYMOUS",
                                    color = theme.glow.copy(alpha = 0.35f * textGlowAlpha),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 6.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.offset(y = (-1).dp)
                                )
                                Text(
                                    text = "ANONYMOUS",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 6.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = "TIKTOK 46",
                                    color = theme.primary.copy(alpha = 0.35f * textGlowAlpha),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 6.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.offset(y = 1.dp)
                                )
                                Text(
                                    text = "TIKTOK 46",
                                    color = theme.accent,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 6.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Sine Wave Animation 2 (Bottom Wave)
                        SineWaveWaveform(color = theme.secondary, amplitude = 10f, speedMultiplier = -1.2f)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "BOUTONS & TOGGLES",
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.weight(0.1f))

                        // Bottom action buttons matching screenshots
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onTriggerAction("Changer Cible") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                                border = BorderStroke(1.dp, theme.primary.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Changer Cible", color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }

                            Button(
                                onClick = { onTriggerAction("TP Cible Voiture") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                                border = BorderStroke(1.dp, theme.secondary.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("TP Cible Voiture", color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    } else if (selectedTab == "VISUELS") {
                        // Visual wireframe simulator to show live ESP toggles
                        Text("SIMULATEUR DE RENDU ESP (Live)", color = theme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Color(0xFF030206), RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                if (espEnabled) {
                                    // Target Mock Box (ESP)
                                    val left = size.width * 0.35f
                                    val top = size.height * 0.2f
                                    val boxWidth = size.width * 0.3f
                                    val boxHeight = size.height * 0.6f

                                    // Draw 2D Box
                                    if (espBoxEnabled) {
                                        drawRoundRect(
                                            color = theme.primary,
                                            topLeft = Offset(left, top),
                                            size = Size(boxWidth, boxHeight),
                                            style = Stroke(width = espThickness.dp.toPx()),
                                            cornerRadius = CornerRadius(4.dp.toPx())
                                        )
                                    }

                                    // Draw Skeletons
                                    if (espSkeletonEnabled) {
                                        val cX = left + boxWidth / 2
                                        val headY = top + boxHeight * 0.2f
                                        // Head
                                        drawCircle(color = theme.secondary, radius = 6.dp.toPx(), center = Offset(cX, headY))
                                        // Spine
                                        drawLine(color = theme.secondary, start = Offset(cX, headY + 6.dp.toPx()), end = Offset(cX, top + boxHeight * 0.7f), strokeWidth = espThickness.dp.toPx())
                                        // Arms
                                        drawLine(color = theme.secondary, start = Offset(cX - 15.dp.toPx(), headY + 12.dp.toPx()), end = Offset(cX + 15.dp.toPx(), headY + 12.dp.toPx()), strokeWidth = espThickness.dp.toPx())
                                        // Legs
                                        drawLine(color = theme.secondary, start = Offset(cX, top + boxHeight * 0.7f), end = Offset(cX - 12.dp.toPx(), top + boxHeight * 0.95f), strokeWidth = espThickness.dp.toPx())
                                        drawLine(color = theme.secondary, start = Offset(cX, top + boxHeight * 0.7f), end = Offset(cX + 12.dp.toPx(), top + boxHeight * 0.95f), strokeWidth = espThickness.dp.toPx())
                                    }

                                    // Draw line of sight ESP
                                    if (espLineEnabled) {
                                        drawLine(
                                            color = theme.accent,
                                            start = Offset(size.width / 2, size.height),
                                            end = Offset(left + boxWidth / 2, top + boxHeight / 2),
                                            strokeWidth = (espThickness * 0.75f).dp.toPx()
                                        )
                                    }
                                } else {
                                    // Empty state when ESP is off
                                    drawCircle(
                                        color = Color.White.copy(alpha = 0.05f),
                                        radius = 30.dp.toPx(),
                                        center = Offset(size.width / 2, size.height / 2)
                                    )
                                }
                            }

                            // Info Overlay Text
                            Column(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(6.dp)
                            ) {
                                if (espEnabled) {
                                    if (showNamesEnabled) Text("JOUEUR: Target_092", color = Color.White, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    if (showDistanceEnabled) Text("DISTANCE: 45m", color = theme.primary, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                } else {
                                    Text("RENDU DÉSACTIVÉ", color = Color.White.copy(alpha = 0.3f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Mini Logs section inside visualizer
                        Text("Live Console Logs", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        ConsoleLogsSection(activeLogs = activeLogs)
                    } else {
                        // General Live Console Output Section (Used for Spawns, Teleports, Monde)
                        Text("LIVESTREAM CONSOLE DU SIMULATEUR", color = theme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        ConsoleLogsSection(activeLogs = activeLogs)

                        Button(
                            onClick = { activeLogs.clear(); activeLogs.add("[CONSOLE] Logs réinitialisés.") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Effacer la Console", color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(theme.primary.copy(alpha = 0.2f)))

            // Card Bottom bar: "SUPPORT LIVE" and "LANG : FR"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SUPPORT LIVE",
                    color = theme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.clickable { onTriggerAction("Live Support opened") }
                )

                Text(
                    text = "EXPIRATION AUTOMATIQUE DU SIMULATEUR DANS 4 JOURS",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "LANG : FR",
                    color = theme.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.clickable { onTriggerAction("Language switch") }
                )
            }
        }
    }
}

@Composable
fun ConsoleLogsSection(activeLogs: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .background(Color.Black, RoundedCornerShape(6.dp))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
            .padding(6.dp)
    ) {
        val scrollState = rememberScrollState()
        // Auto scroll to bottom when new logs appear
        LaunchedEffect(activeLogs.size) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            activeLogs.forEach { log ->
                val color = when {
                    log.startsWith("[SYSTEM]") -> Color(0xFF00FF41)
                    log.startsWith("[OPTION]") -> Color(0xFF00F0FF)
                    log.startsWith("[SPAWN]") -> Color(0xFFFF9900)
                    log.startsWith("[ITEM]") -> Color(0xFFFFD700)
                    log.startsWith("[WORLD]") -> Color(0xFFBD00FF)
                    else -> Color.White.copy(alpha = 0.8f)
                }
                Text(
                    text = log,
                    color = color,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun ModToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    theme: ModTheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )

        // Custom Neon Switch with Glowing track
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = theme.primary,
                checkedBorderColor = theme.primary,
                uncheckedThumbColor = Color.White.copy(alpha = 0.5f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                uncheckedBorderColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.scale(0.85f)
        )
    }
}

@Composable
fun SineWaveWaveform(
    color: Color,
    amplitude: Float,
    speedMultiplier: Float
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sin_wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        val points = 80
        val path = Path()

        for (i in 0..points) {
            val progress = i.toFloat() / points
            val x = progress * width
            // Sine math formula with phase shift
            val wavePhase = progress * 2 * Math.PI.toFloat() * 1.5f + (phase * speedMultiplier)
            val y = centerY + sin(wavePhase) * amplitude.dp.toPx()

            if (i == 0) {
                path.moveTo(x, y.toFloat())
            } else {
                path.lineTo(x, y.toFloat())
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
            alpha = 0.85f
        )

        // Draw multiple glowing lines around the core wave for realism
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round),
            alpha = 0.25f
        )
    }
}

@Composable
fun DynamicGalaxyBackground(
    modifier: Modifier = Modifier,
    theme: ModTheme
) {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy_bg")
    
    // Animate phase for rotation/drift
    val driftPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "drift"
    )

    // Twinkle modulation
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    // Nebula pulsing scale
    val nebulaScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nebula_scale"
    )

    // Shooting star animation
    val shootingStarTransition = rememberInfiniteTransition(label = "shooting_star")
    val shootingStarOffset by shootingStarTransition.animateFloat(
        initialValue = -200f,
        targetValue = 3000f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 9000
                -200f at 0 with LinearEasing
                2500f at 1600 with LinearEasing
                2500f at 9000 with LinearEasing // Pause
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "shooting_star_offset"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerPoint = center
        
        // 1. Draw Glowing Nebulae Clouds with Radial Gradient Brushes matching Theme colors
        val nebulaColor1 = theme.primary.copy(alpha = 0.24f)
        val nebulaColor2 = theme.secondary.copy(alpha = 0.18f)
        val nebulaColorDark = Color.Transparent

        // Nebula 1: Top-Left
        val nebulaCenter1 = Offset(
            x = centerPoint.x * 0.4f + sin(driftPhase) * 40.dp.toPx(),
            y = centerPoint.y * 0.4f + cos(driftPhase) * 35.dp.toPx()
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(nebulaColor1, nebulaColorDark),
                center = nebulaCenter1,
                radius = 350.dp.toPx() * nebulaScale
            ),
            radius = 350.dp.toPx() * nebulaScale,
            center = nebulaCenter1
        )

        // Nebula 2: Bottom-Right
        val nebulaCenter2 = Offset(
            x = centerPoint.x * 1.6f + cos(driftPhase) * 55.dp.toPx(),
            y = centerPoint.y * 1.5f + sin(driftPhase) * 45.dp.toPx()
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(nebulaColor2, nebulaColorDark),
                center = nebulaCenter2,
                radius = 450.dp.toPx() * nebulaScale
            ),
            radius = 450.dp.toPx() * nebulaScale,
            center = nebulaCenter2
        )

        // 2. Tiny deep background stars (twinkling)
        val random = java.util.Random(1337)
        for (i in 0..85) {
            val rx = random.nextFloat() * size.width
            val ry = random.nextFloat() * size.height
            val radius = random.nextFloat() * 1.3f.dp.toPx()
            val individualTwinkle = sin(driftPhase * 4f + i) * 0.35f + 0.65f
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(rx, ry),
                alpha = twinkle * individualTwinkle * 0.65f
            )
        }

        // 3. Medium drifting stars
        for (i in 0..45) {
            val baseRx = random.nextFloat() * size.width
            val baseRy = random.nextFloat() * size.height
            
            val speed = 12.dp.toPx() + (random.nextFloat() * 16.dp.toPx())
            val dx = (baseRx + sin(driftPhase + i) * speed) % size.width
            val dy = (baseRy + cos(driftPhase + i) * speed) % size.height
            
            val radius = 1.8f.dp.toPx() + random.nextFloat() * 1.6f.dp.toPx()
            val starColor = if (random.nextBoolean()) Color.White else theme.accent.copy(alpha = 0.85f)
            val individualTwinkle = sin(driftPhase * 6f + i) * 0.4f + 0.6f

            drawCircle(
                color = starColor,
                radius = radius,
                center = Offset(dx, dy),
                alpha = individualTwinkle * 0.85f
            )
        }

        // 4. Large glowing stars with cross flares
        for (i in 0..7) {
            val baseRx = random.nextFloat() * size.width
            val baseRy = random.nextFloat() * size.height
            
            val speed = 6.dp.toPx() + (random.nextFloat() * 10.dp.toPx())
            val dx = (baseRx + sin(driftPhase * 0.5f + i) * speed) % size.width
            val dy = (baseRy + cos(driftPhase * 0.5f + i) * speed) % size.height
            
            val radius = 3.2f.dp.toPx() + random.nextFloat() * 2f.dp.toPx()
            val individualTwinkle = sin(driftPhase * 2.5f + i) * 0.3f + 0.7f
            
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(dx, dy),
                alpha = individualTwinkle
            )
            val flareLen = radius * 4.5f
            drawLine(
                color = theme.accent.copy(alpha = 0.5f * individualTwinkle),
                start = Offset(dx - flareLen, dy),
                end = Offset(dx + flareLen, dy),
                strokeWidth = 1.2f.dp.toPx()
            )
            drawLine(
                color = theme.accent.copy(alpha = 0.5f * individualTwinkle),
                start = Offset(dx, dy - flareLen),
                end = Offset(dx, dy + flareLen),
                strokeWidth = 1.2f.dp.toPx()
            )
        }

        // 5. Shooting Star Comet swipe
        if (shootingStarOffset > -150f && shootingStarOffset < size.width + 150f) {
            val startX = shootingStarOffset
            val startY = shootingStarOffset * 0.45f
            val length = 90.dp.toPx()
            val endX = startX - length
            val endY = startY - length * 0.45f
            
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(theme.accent.copy(alpha = 0.9f), Color.Transparent),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY)
                ),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 2.2f.dp.toPx()
            )
        }
    }
}


