package tg.eplcoursandroid.recettecuisine.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

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
) : RealmObject()