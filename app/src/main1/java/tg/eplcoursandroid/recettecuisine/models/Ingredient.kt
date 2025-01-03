package tg.eplcoursandroid.recettecuisine.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Ingredient(
    @PrimaryKey var id: Int = 0,
    var nom: String? = null
) : RealmObject(){

}