package com.maximbircu.imageloader.data.sources

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.maximbircu.imageloader.engine.ImageKey
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import java.io.IOException

class NetworkImageSource {
    private val httpClient = HttpClient(Android) {
        engine {
            connectTimeout = 10_000
            socketTimeout = 15_000
        }
        followRedirects = true
    }

    suspend fun get(key: ImageKey): Bitmap {
        try {
            val response: HttpResponse = httpClient.get(key.url)
            if (response.status == HttpStatusCode.OK) {
                val bytes = response.readBytes()
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } else {
                throw IOException("HTTP error code: ${response.status} for URL: ${key.url}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
