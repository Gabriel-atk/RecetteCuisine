package tg.eplcoursandroid.recettecuisine

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import tg.eplcoursandroid.recettecuisine.databinding.ActivityFavorisBinding
import tg.eplcoursandroid.recettecuisine.models.Recette
import tg.eplcoursandroid.recettecuisine.models.Utilisateur

class FavorisActivity : AppCompatActivity() {
    lateinit var ui: ActivityFavorisBinding
    private lateinit var adapter: RecetteAdapter
    private lateinit var realm: Realm
    private lateinit var recyclerView: RecyclerView
    private lateinit var user: Utilisateur

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityFavorisBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setSupportActionBar(ui.toolbar)
        // Initializing RecyclerView after setContentView
        recyclerView = findViewById(R.id.recycler)
        // Configuring Realm
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        val recettes = realm.where(Recette::class.java).findAllAsync()
        val userId = intent.getIntExtra("userId", -1)
        user = realm.where(Utilisateur::class.java).equalTo("id", userId).findFirst()!!
        val recettesFavoris: RealmList<Recette> = user.recettesFavoris
        val recettesFavorisIds = recettesFavoris.map { it.id }.toTypedArray()
        val recettesFiltrees: RealmResults<Recette> = realm.where(Recette::class.java)
            .`in`("id", recettesFavorisIds)
            .findAllAsync()
        adapter = RecetteAdapter(recettesFiltrees)


        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_recipes -> {

                    val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    startActivity(Intent(this, MainActivity::class.java), options.toBundle())

                    true
                }
                R.id.nav_favorites -> {
                    // Already on FavorisActivity, do nothing
                    true
                }
                else -> false
            }
        }

    }


    override fun onResume() {
        super.onResume()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_favorites
    }
}