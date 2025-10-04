package com.example.flixsterplus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flixsterplus.databinding.ActivityMainBinding
import com.example.flixsterplus.models.Person
import com.example.flixsterplus.network.TmdbClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PeopleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PeopleAdapter(listOf()) { person ->
            // On click => open details
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("person", person) // Person is Parcelable
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        fetchPeople()
    }

    private fun fetchPeople() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val people = withContext(Dispatchers.IO) {
                    TmdbClient.fetchPopularPeople()
                }
                adapter.update(people)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
