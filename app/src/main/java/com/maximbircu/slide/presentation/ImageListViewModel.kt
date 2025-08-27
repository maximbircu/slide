package com.maximbircu.slide.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximbircu.imageloader.api.Slide
import com.maximbircu.slide.data.ImageRepository
import com.maximbircu.slide.domain.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ImageListUiState {
    object Loading : ImageListUiState()
    data class Success(val images: List<Image>) : ImageListUiState()
    data class Error(val message: String) : ImageListUiState()
}

sealed class ImageListEvent {
    object LoadImages : ImageListEvent()
    object InvalidateCacheAndReload : ImageListEvent()
}

class ImageListViewModel(
    private val repository: ImageRepository = ImageRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<ImageListUiState>(ImageListUiState.Loading)
    val uiState: StateFlow<ImageListUiState> = _uiState

    fun onEvent(event: ImageListEvent) {
        when (event) {
            is ImageListEvent.LoadImages -> loadImages()
            is ImageListEvent.InvalidateCacheAndReload -> invalidateCacheAndReload()
        }
    }

    private fun loadImages() {
        viewModelScope.launch {
            _uiState.value = ImageListUiState.Loading
            try {
                val images = repository.getImages()
                _uiState.value = ImageListUiState.Success(images)
            } catch (e: Exception) {
                _uiState.value = ImageListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    private fun invalidateCacheAndReload() {
        viewModelScope.launch {
            _uiState.value = ImageListUiState.Loading
            try {
                // Invalidate the image cache
                Slide.invalidateCache()
                // Reload the images from the server
                val images = repository.getImages()
                _uiState.value = ImageListUiState.Success(images)
            } catch (e: Exception) {
                _uiState.value = ImageListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
