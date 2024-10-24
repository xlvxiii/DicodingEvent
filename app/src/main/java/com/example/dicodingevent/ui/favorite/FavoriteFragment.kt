package com.example.dicodingevent.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.repositories.Result
import com.example.dicodingevent.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteViewModel by viewModels {
            factory
        }

        val eventAdapter = FavoriteEventAdapter { event ->
            if (event.isFavorite) {
                viewModel.setFavoriteEvent(event, false)
            } else {
                viewModel.setFavoriteEvent(event, true)
            }
        }

        viewModel.getEvents(-1).observe(viewLifecycleOwner) { favoriteEvents ->
            if (favoriteEvents != null) {
                when (favoriteEvents) {
                    is Result.Loading -> {
                        // Handle loading state
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        // Handle success state with the list of favorite events
                        binding?.progressBar?.visibility = View.GONE
                        // Update UI with the list of favorite events
                        val eventData = favoriteEvents.data
                        eventAdapter.submitList(eventData)
                    }
                    is Result.Error -> {
                        // Handle error state
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + favoriteEvents.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewModel.getFavoriteEvents().observe(viewLifecycleOwner) { favoriteEvents ->
            binding?.progressBar?.visibility = View.GONE
            eventAdapter.submitList(favoriteEvents)
        }

        binding?.rvFavoriteEvent?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }
}