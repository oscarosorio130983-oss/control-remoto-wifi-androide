package com.oscar.universalremote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class WifiRemoteClient {

    suspend fun sendCommand(device: RemoteDevice, command: RemoteCommand): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val endpoint = URL("http://${device.ip}:${device.port}/keypress/${command.code}")
                val connection = (endpoint.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 2500
                    readTimeout = 2500
                    doOutput = true
                }
                val responseCode = connection.responseCode
                connection.disconnect()

                if (responseCode in 200..299) {
                    Result.success("Comando ${command.code} enviado a ${device.name}")
                } else {
                    Result.failure(Exception("Respuesta HTTP $responseCode"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
