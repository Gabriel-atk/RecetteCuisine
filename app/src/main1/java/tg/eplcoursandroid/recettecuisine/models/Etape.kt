package tg.eplcoursandroid.recettecuisine.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Etape(
    @PrimaryKey var id: Int = 0,
    var description: String? = null
) : RealmObject()