package com.example.lindsay.delta5.models

import android.util.Log
import com.example.lindsay.delta5.entities.User
import io.realm.Realm

class UserModel {
    companion object {
        fun loadUser(realm: Realm): User? {
            val query = realm.where(User::class.java)
            return query.findFirst()
        }

        fun saveUser(realm: Realm, user:User): Boolean {
            try {
                realm.beginTransaction()
                realm.insertOrUpdate(user)
                realm.commitTransaction()
            } catch (e: Exception) {
                Log.e("Delta", e.message)
                if (realm.isInTransaction) {
                    realm.cancelTransaction()
                }

                return false
            }
            return true
        }
    }
}