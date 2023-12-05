package com.uniandes.unimaps.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.uniandes.unimaps.R
import com.uniandes.unimaps.models.InterestingPlace
import com.uniandes.unimaps.utils.ImageLoader

class RVAdapter(
    private val persons: List<InterestingPlace>
) :
    RecyclerView.Adapter<RVAdapter.PersonViewHolder>() {

    private var onClickListener: OnClickListener? = null
    class PersonViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var personName: TextView = itemView.findViewById(R.id.person_name)
        var personAge: TextView = itemView.findViewById(R.id.person_age)
        var personJob: TextView = itemView.findViewById(R.id.person_job)
        var personPhoto: ImageView = itemView.findViewById(R.id.avatar)

        var navFlecha: ImageView = itemView.findViewById(R.id.right_arrow)

    }
    override fun getItemCount(): Int {
        return persons.size
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PersonViewHolder {
        val v: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return PersonViewHolder(v)
    }
    override fun onBindViewHolder(personViewHolder: PersonViewHolder, idx: Int) {
        personViewHolder.personName.text = persons[idx].name
        personViewHolder.personAge.text = "Click para ir!"
        personViewHolder.personJob.text = persons[idx].description
        //personViewHolder.personPhoto.setImageResource(persons[idx].photoID)
        // Usar Glide para cargar la imagen

        Glide.with(personViewHolder.itemView.context)
            .load(persons[idx].photoID)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(personViewHolder.personPhoto)

        personViewHolder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick( idx, persons[idx] )
            }
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: InterestingPlace)
    }
}