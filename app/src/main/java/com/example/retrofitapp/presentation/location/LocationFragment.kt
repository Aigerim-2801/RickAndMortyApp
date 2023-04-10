package com.example.retrofitapp.presentation.location

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
import com.example.retrofitapp.adapters.LocationAdapter


class LocationFragment : Fragment(R.layout.location_item) {

    private var _binding: LocationFragmentBinding? = null
    private val binding get() = _binding!!

    private val locationAdapter = LocationAdapter()
    private val viewModel: LocationViewModel by viewModels()

    private var loading = false
    private var previousTotalItemCount = 0
    private val visibleThreshold = 18

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationFragmentBinding.inflate(inflater,container,false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.locationRv.layoutManager = layoutManager
        binding.locationRv.adapter = locationAdapter

        viewModel.locationsMutableLiveData.observe(viewLifecycleOwner) {
            locationAdapter.submitList(it)
        }

        binding.locationRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (dy <= 0) return

                if (loading && totalItemCount > previousTotalItemCount){
                    loading = false
                    previousTotalItemCount = totalItemCount
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    viewModel.getAllLocations()
                    loading = true
                    previousTotalItemCount = 0
                    previousTotalItemCount = totalItemCount
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

    private fun navigateToDetail(id: Int){
        val intent = LocationDetailActivity.startLocationIntent(requireContext(), id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}