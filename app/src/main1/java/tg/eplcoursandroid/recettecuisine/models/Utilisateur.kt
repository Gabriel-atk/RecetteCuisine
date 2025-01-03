package tg.eplcoursandroid.recettecuisine.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Utilisateur(
    @PrimaryKey var id: Int? = 0,
    var nom: String? = null,
    var prenom: String? = null,
    var nomUtilisateur: String? = null,
    var motDePasse: String? = null,
    var recettesFavoris: RealmList<Recette> = RealmList(),
    var recettesAjouts: RealmList<Recette> = RealmList()
) : RealmObject()