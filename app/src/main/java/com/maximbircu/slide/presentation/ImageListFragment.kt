package com.maximbircu.slide.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maximbircu.slide.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImageListFragment : Fragment(R.layout.fragment_image_list) {
    private val viewModel: ImageListViewModel by viewModels()
    private lateinit var adapter: ImageListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val invalidateCacheButton = view.findViewById<Button>(R.id.invalidateCacheButton)
        
        adapter = ImageListAdapter(emptyList()) { image ->
            val intent = ImageDetailActivity.createIntent(requireContext(), image)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        invalidateCacheButton.setOnClickListener {
            viewModel.onEvent(ImageListEvent.InvalidateCacheAndReload)
        }

        viewModel.onEvent(ImageListEvent.LoadImages)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is ImageListUiState.Loading -> {
                        // Show loading indicator
                    }
                    is ImageListUiState.Success -> {
                        adapter.updateImages(state.images)
                    }
                    is ImageListUiState.Error -> {
                        // Show error message
                    }
                }
            }
        }
    }
}
