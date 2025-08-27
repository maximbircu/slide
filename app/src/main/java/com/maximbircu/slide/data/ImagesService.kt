package com.maximbircu.slide.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ImagesService {
    private val baseUrl: String =
        "https://zipoapps-storage-test.nyc3.digitaloceanspaces.com/image_list.json"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getImages(): List<ImageApiModel> {
        val response: HttpResponse = client.get(baseUrl)
        return response.body()
    }
}

@Serializable
data class ImageApiModel(
    val id: Int,
    val imageUrl: String
)
