package com.example.dicodingevent.ui.finished_event

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
import com.example.dicodingevent.databinding.FragmentFinishedEventBinding

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        // Inflate the layout for this fragment
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        val finishedEventViewModel: FinishedEventViewModel by viewModels {
            factory
        }

        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())

        finishedEventViewModel.getFinishedEvents().observe(viewLifecycleOwner) { finishedEventList ->
            if (finishedEventList != null) {
                when (finishedEventList) {
                    Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setFinishedEventData(finishedEventList.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFinishedEventData(eventList: List<ListEventsItem?>?) {
        val adapter = FinishedEventAdapter()
        adapter.submitList(eventList)
        binding.rvFinishedEvent.adapter = adapter

        adapter.setOnItemClickCallback(object : FinishedEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEventItem(data)
            }
        })
    }

    private fun showSelectedEventItem(event: ListEventsItem) {
        val toEventDetailActivity = FinishedEventFragmentDirections.actionNavigationFinishedEventToEventDetailActivity(event.id!!)
        findNavController().navigate(toEventDetailActivity)
    }
}