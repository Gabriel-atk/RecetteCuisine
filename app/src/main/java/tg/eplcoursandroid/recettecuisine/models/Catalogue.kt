package tg.eplcoursandroid.recettecuisine.models

import io.realm.RealmList
import io.realm.RealmObject

open class Catalogue(
    var recettes: RealmList<Recette> = RealmList()
) : RealmObject()