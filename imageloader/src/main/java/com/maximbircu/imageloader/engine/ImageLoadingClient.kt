package com.maximbircu.imageloader.engine

import com.maximbircu.imageloader.data.ImageRepository
import com.maximbircu.imageloader.engine.renderer.ImageRenderer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageLoadingClient(
    private val repository: ImageRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    private val loadingJobs = mutableMapOf<ImageKey, Job>()

    fun load(
        key: ImageKey,
        target: ImageRenderer
    ) {
        if (loadingJobs[key] != null) {
            loadingJobs[key]?.cancel()
        }
        loadingJobs[key] = coroutineScope.launch {
            withContext(Dispatchers.Main) {
                target.onLoading()
            }
            try {
                repository.loadImage(key)
                    .collect { bitmap ->
                        withContext(Dispatchers.Main) {
                            target.onLoaded(bitmap)
                        }
                    }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    target.onError()
                }
            }
        }.apply {
            invokeOnCompletion { loadingJobs.remove(key) }
        }
    }

    fun invalidateCache() {
        repository.invalidateCache()
    }
}
