package com.example.lindsay.delta5.entities


/**
 *
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-26
 */

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Mole(
        @PrimaryKey open var _ID: String = "",
        open var moleName: String = "",
        open var bodyLocation: String = "",
        open var notes: String = "",
        open var imagePath: String? = null,

        open var date: Long? = null,
        open var confidenceMalignant: Long = 0,
        open var confidenceBenign: Long = 0
) : RealmObject()