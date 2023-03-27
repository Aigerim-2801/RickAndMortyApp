package com.example.retrofitapp.sources.location

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.LocationFragmentBinding
import com.example.retrofitapp.sources.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationFragment : Fragment(R.layout.location_detail) {

    private var _binding: LocationFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationFragmentBinding.inflate(inflater,container,false);

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.locationRv.layoutManager = layoutManager
        locationAdapter = LocationAdapter(emptyList())
        binding.locationRv.adapter = locationAdapter


        locationAdapter.onLocationClick = {

            val intent = Intent(requireContext(), LocationCharacterActivity::class.java).apply {
                putExtra("location_id", it.id)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)

        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api_location.getLocation()
                withContext(Dispatchers.Main) {
                    locationAdapter.location = response.results
                    locationAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of location", e)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}