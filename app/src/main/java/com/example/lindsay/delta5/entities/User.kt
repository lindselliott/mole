package com.example.lindsay.delta5.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
        @PrimaryKey open var _ID: String = "1",
        open var age: Int = -1,
        open var sex: Int = 0 // 0 = male; 1 = female
) : RealmObject()