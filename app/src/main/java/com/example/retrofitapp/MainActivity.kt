package com.example.retrofitapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.sources.ItemAdapter
import com.example.retrofitapp.sources.ItemDetailActivity
import com.example.retrofitapp.sources.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var characterAdapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerview)

        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        characterAdapter = ItemAdapter(emptyList())
        recyclerView.adapter = characterAdapter

        characterAdapter.onCharacterClick = {
            val intent = Intent(this, ItemDetailActivity::class.java)
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getResults()
                withContext(Dispatchers.Main) {
                    characterAdapter.character = response.results
                    characterAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of characters", e)
            }
        }
    }
}
