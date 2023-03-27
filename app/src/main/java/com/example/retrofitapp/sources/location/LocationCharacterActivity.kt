package com.example.retrofitapp.sources.location

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.databinding.LocationCharacterBinding
import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.character.CharacterDetailActivity
import com.example.retrofitapp.sources.location.data.ResultsLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationCharacterActivity : AppCompatActivity() {

    private lateinit var binding: LocationCharacterBinding
    private lateinit var locationCharacterAdapter: LocationCharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LocationCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = GridLayoutManager(this, 2)
        binding.locationCharacterRv.layoutManager = layoutManager
        locationCharacterAdapter = LocationCharacterAdapter(emptyList())
        binding.locationCharacterRv.adapter = locationCharacterAdapter


        locationCharacterAdapter.onCharacterClick = {

            val intent = Intent(this, CharacterDetailActivity::class.java).apply {
                putExtra("character_id", it.id)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val locationId = intent.getIntExtra("location_id", -1)
                Log.e("RESPONSE", "$locationId")

                RetrofitInstance.api_location.getLocationInfo(locationId).enqueue(object :
                    Callback<ResultsLocation> {
                    override fun onResponse(call: Call<ResultsLocation>, response: Response<ResultsLocation>) {
                        val locationInfo = response.body()

                        binding.typeLocationCharacter.text = locationInfo?.type
                        binding.nameLocationCharacter.text = locationInfo?.name
                        binding.dimensionLocationCharacter.text = locationInfo?.dimension

                        binding.locationCharacterRv.adapter = locationCharacterAdapter.apply {
                            residents = locationInfo?.residents ?: emptyList()
                        }
                        Log.e("RESPONSE", "$locationInfo")
                    }

                    override fun onFailure(call: Call<ResultsLocation>, t: Throwable) {
                    }
                })


                val response = RetrofitInstance.api_location.getLocation()
                withContext(Dispatchers.Main) {

                    locationCharacterAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of characters of location", e)
            }
        }
    }


}
