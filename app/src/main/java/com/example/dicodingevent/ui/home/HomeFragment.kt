package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels<HomeViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())

        homeViewModel.listEvent.observe(viewLifecycleOwner) { eventList ->
            setEventData(eventList)
        }

        homeViewModel.listFinishedEvent.observe(viewLifecycleOwner) { finishedEventList ->
            setFinishedEventData(finishedEventList)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.isLoading2.observe(viewLifecycleOwner) {
            showLoading2(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showLoading2(isLoading2: Boolean) {
        if (isLoading2) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }

    private fun setEventData(eventList: List<ListEventsItem?>?) {
        val adapter = EventAdapter()
        adapter.submitList(eventList)
        binding.rvEvent.adapter = adapter
    }

    private fun setFinishedEventData(eventList: List<ListEventsItem?>?) {
        val adapter = FinishedEventAdapter()
        adapter.submitList(eventList)
        binding.rvFinishedEvent.adapter = adapter
    }
}