package tg.eplcoursandroid.recettecuisine

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import tg.eplcoursandroid.recettecuisine.databinding.RecetteBinding
import tg.eplcoursandroid.recettecuisine.models.Recette

class RecetteAdapter(private var recettes: RealmResults<Recette>) : RecyclerView.Adapter<RecetteViewHolder>() {

    var onItemClick: ((Int?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetteViewHolder {
        val ui = RecetteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecetteViewHolder(ui)
    }

    override fun getItemCount(): Int {
        Log.d("recette","getItemCount: ${recettes.size}")
        return recettes.size
    }

    override fun onBindViewHolder(holder: RecetteViewHolder, position: Int) {
        holder.recette = recettes[position]
        holder.onItemClick = onItemClick
    }

    init {
        // This ensures that any changes in the data are reflected in the RecyclerView
        recettes.addChangeListener { _, changeSet ->
            // Handle insertions
            for (change in changeSet.insertionRanges) {
                notifyItemRangeInserted(change.startIndex, change.length)
            }
            // Handle removals
            for (change in changeSet.deletionRanges) {
                notifyItemRangeRemoved(change.startIndex, change.length)
            }
            // Handle changes (e.g., updates to existing items)
            for (change in changeSet.changeRanges) {
                notifyItemRangeChanged(change.startIndex, change.length)
            }
        }
    }

    // Clean up resources when the adapter is no longer needed
    fun cleanup() {
        recettes.removeAllChangeListeners()
    }

    // If you want to update the data manually
    fun updateData(newData: RealmResults<Recette>) {
        this.recettes.removeAllChangeListeners() // Remove listeners on old data
        recettes = newData // Replace with new data
        this.recettes.addChangeListener { _, changeSet ->
            // Notify the adapter of changes
            for (change in changeSet.insertionRanges) {
                notifyItemRangeInserted(change.startIndex, change.length)
            }
            for (change in changeSet.deletionRanges) {
                notifyItemRangeRemoved(change.startIndex, change.length)
            }
            for (change in changeSet.changeRanges) {
                notifyItemRangeChanged(change.startIndex, change.length)
            }
        }
        notifyDataSetChanged() // Notify the adapter of the entire data update
    }
}
