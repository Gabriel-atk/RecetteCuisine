package tg.eplcoursandroid.recettecuisine

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.realm.Case
import io.realm.Realm
import io.realm.kotlin.where
import tg.eplcoursandroid.recettecuisine.models.Recette

class RechercheRecetteActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var etRecherche: EditText
    private lateinit var btnRechercher: Button
    private lateinit var rvResultats: RecyclerView
   // private lateinit var adapter: RecetteAdapter // Créez cet adapter pour afficher les résultats

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rechercher_recette)

        // Initialiser Realm
        realm = Realm.getDefaultInstance()

        // Associer les vues
       // etRecherche = findViewById(R.id.choixRecherche)
        //btnRechercher = findViewById(R.id.saisieRecherche)
        //rvResultats = findViewById(R.id.rvResultats)

        // Configurer le RecyclerView
        //rvResultats.layoutManager = LinearLayoutManager(this)
       // adapter = RecetteAdapter(listOf()) // Initialisez avec une liste vide
      //  rvResultats.adapter = adapter

        // Définir l'action du bouton
        btnRechercher.setOnClickListener {
            val query = etRecherche.text.toString().trim()
            rechercherRecettes(query)
        }
    }

    private fun rechercherRecettes(query: String) {
        // Requête Realm pour rechercher par titre ou ingrédient
        val resultats = realm.where<Recette>()
            .contains("titre", query, Case.INSENSITIVE)
            .or()
            .contains("ingredients.nom", query, Case.INSENSITIVE)
            .findAll()

        // Mettre à jour l'adapter avec les résultats
        //adapter.updateData(resultats)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
