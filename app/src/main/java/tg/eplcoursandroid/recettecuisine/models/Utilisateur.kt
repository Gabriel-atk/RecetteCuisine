package tg.eplcoursandroid.recettecuisine.models

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Utilisateur(
    @PrimaryKey var id: Int? = 0,
    var email: String? = null,
    var nomUtilisateur: String? = null,
    var motDePasse: String? = null,
    var recettesFavoris: RealmList<Recette> = RealmList(),
    var recettesAjouts: RealmList<Recette> = RealmList()
) : RealmObject(){
    companion object{
        fun create(realm: Realm, email: String, nomUtilisateur: String, motDePasse: String): Int {
            val id = realm.where(Utilisateur::class.java).max("id")?.toInt()?.plus(1) ?: 1
            val utilisateur = realm.createObject(Utilisateur::class.java, id)
            utilisateur.email = email
            utilisateur.nomUtilisateur = nomUtilisateur
            utilisateur.motDePasse = motDePasse
            return id

        }
    }

    fun ajouterRecetteFavorie(recette: Recette){
        recettesFavoris.add(recette)
    }
    fun supprimerRecetteFavorie(recette: Recette){
        recettesFavoris.remove(recette)
    }
    fun ajouterRecetteAjout(recette: Recette) {
        recettesAjouts.add(recette)
    }


    fun ListerFavoris(): RealmList<Recette> {
        return recettesFavoris
    }
    fun ListerAjouts(): RealmList<Recette> {
        return recettesAjouts

    }


}