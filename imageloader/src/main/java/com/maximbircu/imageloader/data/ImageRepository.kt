package com.maximbircu.imageloader.data

import android.graphics.Bitmap
import com.maximbircu.imageloader.data.sources.DiskImageSource
import com.maximbircu.imageloader.data.sources.MemoryImageSource
import com.maximbircu.imageloader.data.sources.NetworkImageSource
import com.maximbircu.imageloader.engine.ImageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ImageRepository(
    private val memorySource: MemoryImageSource,
    private val diskSource: DiskImageSource,
    private val networkSource: NetworkImageSource
) {
    fun loadImage(key: ImageKey): Flow<Bitmap> = flow {
        val memData = memorySource.get(key)
        if (memData != null) {
            emit(memData)
        } else if (diskSource.hasValid(key)) {
            val diskData = diskSource.get(key)
            if (diskData != null) {
                memorySource.put(key, diskData)
                emit(diskData)
            }
        } else {
            val netData = networkSource.get(key)
            memorySource.put(key, netData)
            diskSource.put(key, netData)
            emit(netData)
        }
    }

    fun invalidateCache() {
        memorySource.celar()
        diskSource.clear()
    }
}
