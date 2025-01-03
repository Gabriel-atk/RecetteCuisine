package tg.eplcoursandroid.recettecuisine

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tg.eplcoursandroid.recettecuisine.databinding.RecetteBinding
import tg.eplcoursandroid.recettecuisine.models.Recette


class RecetteViewHolder(val ui: RecetteBinding) : RecyclerView.ViewHolder(ui.root) {
    var recetteId: Int? = null
    var onItemClick: ((Int?) -> Unit)? = null

    var recette: Recette?
        get() = this.itemView.tag as? Recette
        set(recette) {
            if (recette == null) return
            this.recetteId = recette.id
            // Use Glide to load the image from the string URL or path
             Glide.with(ui.image.context)
                .load(recette.imagePath?.toInt()) // Assuming 'imagePath' is a String URL or file path
                .placeholder(R.mipmap.ic_salade) // Optional placeholder
                .into(ui.image)
            ui.titre.text = recette.titre
            ui.tempsPreparation.text = recette.tempsPreparation.toString()
            ui.categorie.text = recette.categorie
            ui.description.text = recette.description
            itemView.tag = recette // Save the data on the view for later retrieval
        }

    init {
        ui.root.setOnClickListener {
            Log.i("Recette", "Clicked on item with id: $recetteId")
            onItemClick?.invoke(recette?.id) // Handling click event, passing recetteId
        }
    }
}
