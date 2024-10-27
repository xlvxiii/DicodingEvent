package com.example.dicodingevent.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

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
            // Delete event from table when favorite button clicked
            viewModel.deleteFavoriteEvent(event.id)
        }

        viewModel.getFavoriteEvents().observe(viewLifecycleOwner) { favoriteEvents ->
            eventAdapter.submitList(favoriteEvents)
            binding?.progressBar?.visibility = View.GONE
        }

        binding?.rvFavoriteEvent?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        eventAdapter.setOnItemClickCallback(object : FavoriteEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EventEntity) {
                showSelectedEventItem(data)
            }
        })
    }

    private fun showSelectedEventItem(event: EventEntity) {
        val toEventDetailActivity = FavoriteFragmentDirections.actionNavigationFavoriteToEventDetailActivity(event.id)
        findNavController().navigate(toEventDetailActivity)
    }
}