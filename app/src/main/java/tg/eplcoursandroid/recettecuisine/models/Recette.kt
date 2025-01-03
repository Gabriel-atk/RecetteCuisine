package tg.eplcoursandroid.recettecuisine.models

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.createObject
import org.bson.types.ObjectId

open class Recette(
    @PrimaryKey var id: Int? = 0,
    var titre: String? = null,
    var description: String? = null,
    var tempsPreparation: Int? = 0,
    var categorie: String? = null,
    var ingredients: RealmList<Ingredient> = RealmList(),
    var etapes: RealmList<Etape> = RealmList(),
    var auteur: Utilisateur? = null,
    var imagePath: String? = null
) : RealmObject() {

    companion object {
        fun create(
            realm: Realm,
            titre: String,
            description: String,
            tempsPreparation: Int,
            categorie: String,
            ingredients: RealmList<Ingredient>,
            etapes: RealmList<Etape>,
            auteur: Utilisateur,
            imagePath: String,
            id: Int
        ): Int {
            val recette = realm.createObject(Recette::class.java, id)
            recette.titre = titre
            recette.description = description
            recette.tempsPreparation = tempsPreparation
            recette.categorie = categorie
            recette.ingredients = ingredients
            recette.etapes = etapes
            recette.auteur = auteur
            recette.imagePath = imagePath
            return id
        }


    }
    fun create(
        realm: Realm,
        titre: String,
        description: String,
        tempsPreparation: Int,
        categorie: String,
        ingredients: RealmList<Ingredient>,
        etapes: RealmList<Etape>,
        auteur: Utilisateur,
        imagePath: String
    ) {
        val id = realm.where(Recette::class.java).max("id")?.toInt()?.plus(1) ?: 1
        val newRecette = realm.createObject(Recette::class.java, id)
        newRecette.titre = titre
        newRecette.description = description
        newRecette.tempsPreparation = tempsPreparation
        newRecette.categorie = categorie
        newRecette.ingredients = ingredients
        newRecette.etapes = etapes
        newRecette.auteur = auteur
        newRecette.imagePath = imagePath
    }

    fun estFavorie(user: Utilisateur): Boolean {
        val recettesFavoris = user.recettesFavoris
        return recettesFavoris.contains(this)

    }
}
