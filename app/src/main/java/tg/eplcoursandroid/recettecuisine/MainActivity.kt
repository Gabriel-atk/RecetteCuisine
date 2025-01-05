package tg.eplcoursandroid.recettecuisine

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import tg.eplcoursandroid.recettecuisine.databinding.ActivityMainBinding
import tg.eplcoursandroid.recettecuisine.models.Recette

class MainActivity : AppCompatActivity() {
    private lateinit var ui: ActivityMainBinding
    private lateinit var adapter: RecetteAdapter
    private lateinit var realm: Realm
    private lateinit var recyclerView: RecyclerView

    private val realmChangeListener = RealmChangeListener<RealmResults<Recette>> {
        // Listener used in the adapter to handle data changes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setSupportActionBar(ui.toolbar)

        // Initializing RecyclerView after setContentView
        recyclerView = findViewById(R.id.recycler)

        // Configuring Realm
        Realm.init(this)
        realm = Realm.getDefaultInstance()

        val recettes = realm.where(Recette::class.java).findAllAsync()
        adapter = RecetteAdapter(recettes)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Adding item decoration
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Handling data changes in Realm
        recettes.addChangeListener { results ->
            if (results.isNotEmpty()) {
                adapter.notifyDataSetChanged()
                Log.d("MainActivity", "Recettes loaded: ${results.size}")
            } else {
                Log.d("MainActivity", "No recipes found.")
            }
        }

        // Setting onItemClick listener for the adapter
        adapter.onItemClick = ::onItemClicked

        //bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.nav_favorites -> {
                    val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    startActivity(Intent(this, FavorisActivity::class.java), options.toBundle())

                    true
                }
                R.id.nav_recipes -> {
                    // Deja déjà sur la page principale
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.cleanup() // Removing Realm listeners
        realm.close() // Closing Realm instance
    }

    private fun onItemClicked(recetteId: Int?) {
        realm.executeTransactionAsync { realm ->
            val recette = realm.where(Recette::class.java).equalTo("id", recetteId).findFirst()
            if (recette != null) {
                val intent = Intent(this, AffichageRecetteActivity::class.java)
                intent.putExtra("recetteId", recette.id)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handling item selection
        return when (item.itemId) {
            R.id.search -> {
                onRechercheActivity()
                true
            }

            R.id.create -> {
                onAjouterRecetteActivity()
                true
            }


            else -> false
        }
    }

    private fun onFavoritesActivity() {
        val intent = Intent(this, FavorisActivity::class.java)
        startActivity(intent)
    }

    private fun onMainActivity() {
        TODO("Not yet implemented")
    }
    private fun onAjouterRecetteActivity() {
        val intent = Intent(this, AjouterRecetteActivity::class.java)
        startActivity(intent)
    }

    private fun onRechercheActivity() {
        val intent = Intent(this, RechercheRecetteActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_recipes
    }



}
