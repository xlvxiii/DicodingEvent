package com.example.dicodingevent.ui.finished_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentFinishedEventBinding

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding!!
    private val finishedEventViewModel by viewModels<FinishedEventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())

        finishedEventViewModel.listEvent.observe(viewLifecycleOwner) { finishedEventList ->
            setFinishedEventData(finishedEventList)
        }

        finishedEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
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

    private fun setFinishedEventData(eventList: List<ListEventsItem?>?) {
        val adapter = FinishedEventAdapter()
        adapter.submitList(eventList)
        binding.rvFinishedEvent.adapter = adapter
    }
}