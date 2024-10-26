package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.repositories.Result
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        hide action bar
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            factory
        }

        binding.rvEvent.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireActivity())

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)

                    viewModel.getSearchResult(searchView.text.toString()).observe(viewLifecycleOwner) { eventList ->
                        binding.progressBar3.visibility = View.GONE
                        if (eventList != null) {
                            when (eventList) {
                                is Result.Loading -> {
                                    binding.progressBar3.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.progressBar3.visibility = View.GONE
                                    setSearchResultData(eventList.data)
                                    if (eventList.data?.isEmpty() == true) {
                                        binding.tvTextEmptyResult.text = getString(R.string.empty_search_result)
                                        binding.tvTextResult.text = ""
                                    } else {
                                        binding.tvTextResult.text = getString(R.string.text_result)
                                        binding.tvTextEmptyResult.text = ""
                                    }
                                }

                                is Result.Error -> {
                                    binding.progressBar3.visibility = View.GONE
                                    Toast.makeText(
                                        context,
                                        "No internet connection",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    binding.rvSearchResult.addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager(requireActivity()).orientation))

                    searchBar.setText("")
                    false
                }
        }

        viewModel.getActiveEvents(1, 5).observe(viewLifecycleOwner) { eventList ->
            binding.progressBar.visibility = View.GONE
            if (eventList != null) {
                when (eventList) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setEventData(eventList.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "No internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewModel.getFinishedEvents(0).observe(viewLifecycleOwner) { eventList ->
            binding.progressBar2.visibility = View.GONE
            if (eventList != null) {
                when (eventList) {
                    is Result.Loading -> {
                        binding.progressBar2.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar2.visibility = View.GONE
                        setFinishedEventData(eventList.data)
                    }
                    is Result.Error -> {
                        binding.progressBar2.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "No internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
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

    private fun setSearchResultData(eventList: List<ListEventsItem?>?) {
        val adapter = SearchResultAdapter()
        adapter.submitList(eventList)
        binding.rvSearchResult.adapter = adapter

        adapter.setOnItemClickCallback(object : SearchResultAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSearchEventItem(data)
            }
        })
    }

    private fun showSelectedEventItem(event: ListEventsItem) {
        val toEventDetailActivity = HomeFragmentDirections.actionNavigationHomeToEventDetailActivity(event.id!!)
        findNavController().navigate(toEventDetailActivity)
    }

    private fun showSearchEventItem(event: ListEventsItem) {
        val toEventDetailActivity = HomeFragmentDirections.actionNavigationHomeToEventDetailActivity(event.id!!)
        findNavController().navigate(toEventDetailActivity)
    }
}