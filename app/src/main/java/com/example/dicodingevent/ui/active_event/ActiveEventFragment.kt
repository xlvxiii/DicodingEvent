package com.example.dicodingevent.ui.active_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.repositories.Result
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentActiveEventBinding

class ActiveEventFragment : Fragment() {

    private var _binding: FragmentActiveEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentActiveEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity())

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels {
            factory
        }

        viewModel.getEvents(1).observe(viewLifecycleOwner) { eventList ->
            binding.progressBar.visibility = View.GONE
            if (eventList != null) {
                when(eventList) {
                    Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setEventData(eventList.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
//                        FailDialogFragment().show(childFragmentManager, "FailDialogFragment")
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEventData(eventList: List<ListEventsItem?>?) {
        val adapter = EventAdapter()
        adapter.submitList(eventList)
        binding.rvEvent.adapter = adapter

        adapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEventItem(data)
            }
        })
    }

    private fun showSelectedEventItem(event: ListEventsItem) {
        val toEventDetailActivity = ActiveEventFragmentDirections.actionNavigationActiveEventToEventDetailActivity(event.id!!)
        findNavController().navigate(toEventDetailActivity)
    }

}