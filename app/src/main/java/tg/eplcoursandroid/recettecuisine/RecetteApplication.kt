package tg.eplcoursandroid.recettecuisine

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import tg.eplcoursandroid.recettecuisine.models.Etape
import tg.eplcoursandroid.recettecuisine.models.Ingredient
import tg.eplcoursandroid.recettecuisine.models.Recette

class RecetteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            //.name("recettecuisine.realm")
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .build()
        Realm.setDefaultConfiguration(config)
        val realm = Realm.getDefaultInstance()

        try {
            initRecettes(realm)
            Log.d("RecetteApplication", "Initialisation des Recettes réussie.")
        } catch (e: Exception) {
            Log.e("RecetteApplication", "Erreur lors de l'initialisation des Recettes", e)
        } finally {
            realm.close() // Fermer Realm après utilisation pour éviter les fuites
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun initRecettes(realm: Realm) {
        if (realm.where(Recette::class.java).count() > 0) return

        realm.executeTransactionAsync { realm ->
            val donnees = realm.where(Recette::class.java).findAll()
            Log.d("initRecettes", "Nombre de recettes trouvées dans la base : ${donnees.size}")

            if (donnees.isEmpty()) {
                Log.d("initRecettes", "Insertion des recettes par défaut.")
                val res = resources
                // Créez un tableau d'identifiants de ressources pour les images
                val imageResourceIds = arrayOf (
                    R.mipmap.ic_salade2_round,
                    R.mipmap.ic_poisson1_round,
                    R.mipmap.ic_food,
                    R.mipmap.ic_brochette1_round,
                    R.mipmap.ic_tartelegume_round,
                    R.mipmap.ic_couscous1_round,
                    R.mipmap.ic_salade_round,
                    R.mipmap.ic_fufu1_round,
                    R.mipmap.ic_salade_round,
                    R.mipmap.ic_salade,
                    R.mipmap.ic_frites_round,
                    R.mipmap.ic_creme1_round,
                    R.mipmap.ic_chocolatfond_round,
                    R.mipmap.ic_cake1_round,
                    R.mipmap.ic_chocolat1_round,
                    R.mipmap.ic_tartepomme_round,

                )
                //val images = res.getStringArray(R.array.images)
                val titres = res.getStringArray(R.array.titres)
                Log.d("initRecettes", "Titres : ${titres.joinToString()}")

                val descriptions = res.getStringArray(R.array.descriptions)
                val tempsPrepa = res.getStringArray(R.array.tempsPreparation)
                val categories = res.getStringArray(R.array.categories)
                val ingredientsArray = res.getStringArray(R.array.ingredients)
                val etapesArray = res.getStringArray(R.array.etapes)

                for (i in titres.indices) {
                    val recette = realm.createObject(Recette::class.java, i)
                    recette.titre = titres[i]
                    recette.description = descriptions[i]
                    recette.tempsPreparation = tempsPrepa[i].filter { it.isDigit() }.toInt() // Extracting the integer part
                    recette.categorie = categories[i]
                    //recette.imagePath = images[i]
                    recette.imagePath = imageResourceIds[i].toString()
                    Log.d("initRecettes", "Ajout de la recette: ${recette.titre}")

                    // Ajouter les ingrédients (convertir en objets Ingredient)
                    val ingredientsList = ingredientsArray[i].split(",").map { it.trim() }
                    for (ingredientNom in ingredientsList) {
                        val ingredientId = realm.where(Ingredient::class.java).max("id")?.toInt()?.plus(1) ?: 1
                        val ingredient = realm.createObject(Ingredient::class.java, ingredientId)
                        ingredient.nom = ingredientNom
                        recette.ingredients.add(ingredient)
                        Log.d("initRecettes", "Ajout de l'ingrédient: ${ingredient.nom}")
                    }

                    // Ajouter les étapes (convertir en objets Etape)
                    val etapesList = etapesArray[i].split(",").map { it.trim() }
                    for (etapeDesc in etapesList) {
                        val etapeId = realm.where(Etape::class.java).max("id")?.toInt()?.plus(1) ?: 1
                        val etape = realm.createObject(Etape::class.java, etapeId)
                        etape.description = etapeDesc
                        recette.etapes.add(etape)
                        Log.d("initRecettes", "Ajout de l'étape: ${etape.description}")
                    }
                }
            } else {
                Log.d("initRecettes", "Les données existent déjà")
            }

            // TODO: Libérer les images si nécessaire
        }
    }


}
