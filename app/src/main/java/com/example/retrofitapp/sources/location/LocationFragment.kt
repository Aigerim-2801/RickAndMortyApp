package com.example.retrofitapp.sources.location

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.LocationFragmentBinding
import com.example.retrofitapp.sources.location.viewModel.LocationViewModel


class LocationFragment : Fragment(R.layout.location_detail) {

    private var _binding: LocationFragmentBinding? = null
    private val binding get() = _binding!!

    private val locationAdapter = LocationAdapter()
    private val viewModel: LocationViewModel by viewModels()

    private var loading = false
    private var previousTotalItemCount = 0
    private val visibleThreshold = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationFragmentBinding.inflate(inflater,container,false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.locationRv.layoutManager = layoutManager
        binding.locationRv.adapter = locationAdapter

        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            locationAdapter.submit(locations)
        }

        binding.locationRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (loading && totalItemCount > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }
                if (!loading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
                    loadMore()
                    loading = true
                }
            }
        })

        locationAdapter.onLocationClick = {  navigateToDetail(it.id) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadMore() {
        viewModel.getAllLocations()
    }
    private fun navigateToDetail(id: Int){
        val intent = LocationDetailActivity.startLocationIntent(requireContext(), id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}