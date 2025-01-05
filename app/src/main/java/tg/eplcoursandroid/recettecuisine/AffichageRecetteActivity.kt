package tg.eplcoursandroid.recettecuisine

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import io.realm.Realm
import tg.eplcoursandroid.recettecuisine.databinding.ActivityAffichageRecetteBinding
import tg.eplcoursandroid.recettecuisine.models.Recette

class AffichageRecetteActivity : AppCompatActivity() {
    private lateinit var ui: ActivityAffichageRecetteBinding
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityAffichageRecetteBinding.inflate(layoutInflater)
        setContentView(ui.root)
        Realm.init(this)
        realm = Realm.getDefaultInstance()

        val recetteId = intent.getIntExtra("recetteId", -1)
        val recette = realm.where(Recette::class.java).equalTo("id", recetteId).findFirst()

        if (recetteId != -1) {
            if (recette != null) {
                // Use Glide to load the image from the string URL or path
                Glide.with(ui.image.context)
                    .load(recette.imagePath)
                    .placeholder(R.mipmap.ic_salade)
                    .error(R.mipmap.ic_salade)
                    .into(ui.image)
                ui.titre.text = recette.titre
                ui.tempsPreparation.text = recette.tempsPreparation.toString()
                ui.description.text = recette.description
            }
        }
        val etapesContainer = findViewById<LinearLayout>(R.id.etapesContainer)
        if (recette != null && recette.etapes.isNotEmpty()) {
            for (etape in recette.etapes) {
                val etapeTextView = TextView(this)
                etapeTextView.text = etape.description
                etapeTextView.textSize = 16f
                etapeTextView.setPadding(0, 8, 0, 8)
                etapesContainer.addView(etapeTextView)
            }
        }else{
            val etapeTextView = TextView(this)
            etapeTextView.text = "Aucune etape"
            etapeTextView.textSize = 16f
            etapeTextView.setPadding(0, 8, 0, 8)
        }
        val ingredientsContainer = findViewById<LinearLayout>(R.id.ingredientsContainer)
        if (recette != null && recette.ingredients.isNotEmpty()) {
            for (ingredient in recette.ingredients) {
                val ingredientTextView = TextView(this)
                ingredientTextView.text = ingredient.nom
                ingredientTextView.textSize = 16f
                ingredientTextView.setPadding(0, 8, 0, 8)
                ingredientsContainer.addView(ingredientTextView)
            }
        }else{
            val ingredientTextView = TextView(this)
            ingredientTextView.text = "Aucun ingredient"
            ingredientTextView.textSize = 16f
            ingredientTextView.setPadding(0, 8, 0, 8)

        }

    }
}