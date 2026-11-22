package com.oscar.universalremote

data class RemoteDevice(
    val id: String,
    val name: String,
    val ip: String,
    val port: Int = 8060,
    val protocol: String = "HTTP_POST",
    val isConnected: Boolean = false
)

enum class RemoteCommand(val code: String) {
    POWER("POWER"),
    UP("UP"),
    DOWN("DOWN"),
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    SELECT("SELECT"),
    VOL_UP("VOL_UP"),
    VOL_DOWN("VOL_DOWN"),
    MUTE("MUTE"),
    HOME("HOME"),
    BACK("BACK"),
    CH_UP("CH_UP"),
    CH_DOWN("CH_DOWN"),
    APP_NETFLIX("APP_NETFLIX"),
    APP_YOUTUBE("APP_YOUTUBE"),
    APP_PRIME("APP_PRIME"),
    APP_DISNEY("APP_DISNEY"),
    APP_XUPERTV("APP_XUPERTV")
}
