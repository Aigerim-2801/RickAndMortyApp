package com.example.retrofitapp.presentation.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.LocationFragmentBinding
import com.example.retrofitapp.adapters.LocationAdapter
import com.example.retrofitapp.data.utils.Const
import kotlinx.coroutines.launch


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


        locationAdapter.onLocationClick = { location->
            val bundle = Bundle().apply {
                putInt(Const.LOCATION_ID, location.id)
            }
            val navController = findNavController()
            navController.navigate(R.id.action_locationFragment_to_locationDetailFragment, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.locationsStateFlow.collect{
                locationAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}