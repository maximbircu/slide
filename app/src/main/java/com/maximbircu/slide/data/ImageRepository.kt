package com.maximbircu.slide.data

import com.maximbircu.slide.domain.Image
import com.maximbircu.slide.domain.ImageGateway

class ImageRepository(
    private val service: ImagesService = ImagesService()
) : ImageGateway {
    override suspend fun getImages(): List<Image> {
        return service.getImages()
            .map {
                Image(
                    id = it.id,
                    title = it.imageUrl.substringAfterLast("/"),
                    url =  it.imageUrl,
                )
            }
    }
}
