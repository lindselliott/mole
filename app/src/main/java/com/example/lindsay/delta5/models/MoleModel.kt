package com.example.lindsay.delta5.models

import android.util.Log
import com.example.lindsay.delta5.entities.Mole
import com.example.lindsay.delta5.utils.ImageUtils
import io.realm.Realm
import io.realm.RealmResults

/**
 *
 * @author Marshall Asch
 * @version 1.0
 * @since 2019-01-26
 */
class MoleModel {
    companion object {
        fun removeMole(realm: Realm, mole: Mole): Boolean {
            if (mole.imagePath != null) {
                ImageUtils.deleteImage(mole.imagePath!!)
            }

            try {
                realm.beginTransaction()
                mole.deleteFromRealm()
                realm.commitTransaction()
            } catch (e: Exception) {
                Log.e("MoleModel", e.message)
                if (realm.isInTransaction) {
                    realm.cancelTransaction()
                }

                return false
            }

            return true
        }

        fun saveMole(realm: Realm, mole: Mole): Boolean {
            try {
                realm.beginTransaction()
                realm.insertOrUpdate(mole)
                realm.commitTransaction()
            } catch (e: Exception) {
                Log.e("MoleModel", e.message)
                if (realm.isInTransaction) {
                    realm.cancelTransaction()
                }

                return false
            }
            return true
        }

        fun getMole(realm: Realm, id: String): Mole? {
            return realm.where(Mole::class.java).equalTo("_ID", id).findFirst()
        }

        fun getAllMoles(realm: Realm): RealmResults<Mole> {
            return realm.where(Mole::class.java).findAll()
        }

        fun removeAllMoles(realm: Realm): Boolean {
            try {
                realm.beginTransaction()
                realm.delete(Mole::class.java)
                realm.commitTransaction()
            } catch (e: java.lang.Exception) {
                if (realm.isInTransaction) {
                    realm.cancelTransaction()
                }
                return false
            }

            return true
        }
    }
}