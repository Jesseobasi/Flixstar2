package com.example.flixsterplus

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.flixsterplus.databinding.ActivityDetailBinding
import com.example.flixsterplus.models.Person
import com.example.flixsterplus.network.TmdbClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        person = intent.getParcelableExtra("person")
        if (person == null) {
            finish()
            return
        }

        binding.tvName.text = person!!.name
        binding.tvDept.text = person!!.known_for_department ?: ""
        Glide.with(this)
            .load(TmdbClient.imageUrl(person!!.profile_path, size = "w342"))
            .into(binding.ivProfileLarge)

        fetchDetails(person!!.id)
    }

    private fun fetchDetails(personId: Int) {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val json = withContext(Dispatchers.IO) {
                    TmdbClient.fetchPersonDetails(personId)
                }
                val biography = if (json.has("biography")) json.get("biography").asString else "N/A"
                val birthday = if (json.has("birthday") && !json.get("birthday").isJsonNull) json.get("birthday").asString else "Unknown"
                val place = if (json.has("place_of_birth") && !json.get("place_of_birth").isJsonNull) json.get("place_of_birth").asString else "Unknown"

                binding.tvBiography.text = biography.ifBlank { "No biography available." }
                binding.tvBirthday.text = "Birthday: $birthday"
                binding.tvPlace.text = "Place of birth: $place"

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@DetailActivity, "Error loading details", Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
