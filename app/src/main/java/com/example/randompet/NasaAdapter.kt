package com.example.randompet




import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class NasaAdapter(private val nasaList: List<NasaItem>): RecyclerView.Adapter<NasaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nasaImage: ImageView
        val titleText: TextView = view.findViewById(R.id.title_text)
        val dateText: TextView = view.findViewById(R.id.date_text)

        init {
            // Find our RecyclerView item's ImageView for future use
            nasaImage = view.findViewById(R.id.nasa_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nasa_image, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = nasaList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(nasaList[position].imageUrl)
            .centerCrop()
            .into(holder.nasaImage)

        holder.titleText.text = nasaList[position].title
        holder.dateText.text = nasaList[position].date
    }
}
