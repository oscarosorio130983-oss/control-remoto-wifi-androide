package com.oscar.universalremote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val remoteClient = WifiRemoteClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val device by remember {
                mutableStateOf(
                    RemoteDevice(
                        id = "1",
                        name = "oscar",
                        ip = "192.168.1.50",
                        port = 8060,
                        isConnected = true
                    )
                )
            }
            var volume by remember { mutableIntStateOf(37) }
            var statusMessage by remember { mutableStateOf("Listo por Wi-Fi") }

            fun trigger(cmd: RemoteCommand) {
                scope.launch {
                    val result = remoteClient.sendCommand(device, cmd)
                    statusMessage = result.getOrElse { "Error: ${it.message}" }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0E0F12))
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (device.isConnected) "● ${device.name}" else "○ ${device.name}",
                        color = if (device.isConnected) Color(0xFF2EE59D) else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1A1C22))
                            .clickable { trigger(RemoteCommand.POWER) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⏻", color = Color(0xFF4ADE80), fontSize = 20.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppShortcutButton("Netflix", Color(0xFFB91C1C), "N") {
                        trigger(RemoteCommand.APP_NETFLIX)
                    }
                    AppShortcutButton("YouTube", Color(0xFFDC2626), "▶") {
                        trigger(RemoteCommand.APP_YOUTUBE)
                    }
                    AppShortcutButton("Prime", Color(0xFF2563EB), "P") {
                        trigger(RemoteCommand.APP_PRIME)
                    }
                    AppShortcutButton("Disney+", Color(0xFF1D4ED8), "D+") {
                        trigger(RemoteCommand.APP_DISNEY)
                    }
                    AppShortcutButton("XuperTV", Color(0xFF7C3AED), "X") {
                        trigger(RemoteCommand.APP_XUPERTV)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF17191F))
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF8B5CF6), Color(0xFF38BDF8))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "⌃",
                        color = Color.White,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 18.dp)
                            .clickable { trigger(RemoteCommand.UP) }
                    )
                    Text(
                        "⌄",
                        color = Color.White,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 18.dp)
                            .clickable { trigger(RemoteCommand.DOWN) }
                    )
                    Text(
                        "‹",
                        color = Color.White,
                        fontSize = 28.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 22.dp)
                            .clickable { trigger(RemoteCommand.LEFT) }
                    )
                    Text(
                        "›",
                        color = Color.White,
                        fontSize = 28.sp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 22.dp)
                            .clickable { trigger(RemoteCommand.RIGHT) }
                    )
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF222630))
                            .border(1.5.dp, Color(0xFF7C3AED), CircleShape)
                            .clickable { trigger(RemoteCommand.SELECT) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFF1B263B))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("+", color = Color.White, fontSize = 22.sp, modifier = Modifier.clickable {
                            volume += 1
                            trigger(RemoteCommand.VOL_UP)
                        })
                        Text("VOL\n$volume", color = Color(0xFF93C5FD), fontSize = 12.sp)
                        Text("−", color = Color.White, fontSize = 22.sp, modifier = Modifier.clickable {
                            volume = (volume - 1).coerceAtLeast(0)
                            trigger(RemoteCommand.VOL_DOWN)
                        })
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            MiniActionBtn("⌨") { trigger(RemoteCommand.HOME) }
                            MiniActionBtn("⌂") { trigger(RemoteCommand.HOME) }
                            MiniActionBtn("⠿") { trigger(RemoteCommand.SELECT) }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            MiniActionBtn("🔇") { trigger(RemoteCommand.MUTE) }
                            MiniActionBtn("🎙") { trigger(RemoteCommand.SELECT) }
                            MiniActionBtn("↩") { trigger(RemoteCommand.BACK) }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFF1A1C22))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("⌃", color = Color.White, fontSize = 20.sp, modifier = Modifier.clickable {
                            trigger(RemoteCommand.CH_UP)
                        })
                        Text("CH", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("⌄", color = Color.White, fontSize = 20.sp, modifier = Modifier.clickable {
                            trigger(RemoteCommand.CH_DOWN)
                        })
                    }
                }

                Text(text = statusMessage, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun AppShortcutButton(
    label: String,
    bgColor: Color,
    badgeText: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = badgeText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun MiniActionBtn(symbol: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color(0xFF1A1C22))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(symbol, color = Color.White, fontSize = 18.sp)
    }
}
