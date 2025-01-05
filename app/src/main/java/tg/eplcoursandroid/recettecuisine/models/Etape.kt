package tg.eplcoursandroid.recettecuisine.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Etape(
    @PrimaryKey var id: Int = 0,
    var description: String? = null
) : RealmObject(){
    companion object {
        fun create(realm: Realm, description: String, id: Int): Int {
            val etape = realm.createObject(Etape::class.java, id)
            etape.description = description
            return id

        }
    }
    fun create(realm: Realm, description: String){
        val id = realm.where(Etape::class.java).max("id")?.toInt()?.plus(1) ?: 1
        val newEtape = realm.createObject(Etape::class.java, id)
        newEtape.description = description
    }
}