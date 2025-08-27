package com.maximbircu.slide.domain

interface ImageGateway {
    suspend fun getImages(): List<Image>
}
