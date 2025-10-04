package com.example.flixsterplus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flixsterplus.databinding.ItemPersonBinding
import com.example.flixsterplus.models.Person
import com.example.flixsterplus.network.TmdbClient

class PeopleAdapter(
    private var people: List<Person>,
    private val onClick: (Person) -> Unit
) : RecyclerView.Adapter<PeopleAdapter.PersonVH>() {

    inner class PersonVH(private val vb: ItemPersonBinding) : RecyclerView.ViewHolder(vb.root) {
        fun bind(person: Person) {
            // FIX: Changed tvName to personNameTextView
            vb.personNameTextView.text = person.name

            // FIX: Changed tvDept to knownForTextView
            vb.knownForTextView.text = person.known_for_department ?: ""

            val url = TmdbClient.imageUrl(person.profile_path, size = "w185")

            // FIX: Changed ivProfile to personImageView
            Glide.with(vb.root).load(url).centerCrop().into(vb.personImageView)

            vb.root.setOnClickListener { onClick(person) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH {
        val vb = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonVH(vb)
    }

    override fun onBindViewHolder(holder: PersonVH, position: Int) {
        holder.bind(people[position])
    }

    override fun getItemCount(): Int = people.size

    fun update(newPeople: List<Person>) {
        people = newPeople
        notifyDataSetChanged()
    }
}
