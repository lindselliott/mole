package com.example.lindsay.delta5.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class User(
        @PrimaryKey open var _ID: String = "1",
           open var age: Int = -1,
           open var sex: Int = 0 // 0 = Male; 1 = Female
): RealmObject()