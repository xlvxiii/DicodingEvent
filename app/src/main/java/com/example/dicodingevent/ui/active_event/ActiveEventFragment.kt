package com.example.dicodingevent.ui.active_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentActiveEventBinding

class ActiveEventFragment : Fragment() {

    private var _binding: FragmentActiveEventBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by viewModels<EventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentActiveEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity())

        eventViewModel.listEvent.observe(viewLifecycleOwner) { eventList ->
            setEventData(eventList)
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) {
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

    private fun setEventData(eventList: List<ListEventsItem?>?) {
        val adapter = EventAdapter()
        adapter.submitList(eventList)
        binding.rvEvent.adapter = adapter
    }

}