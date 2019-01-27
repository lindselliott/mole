package com.example.lindsay.delta5

import com.example.lindsay.delta5.entities.Mole
import com.example.lindsay.delta5.entities.User
import com.example.lindsay.delta5.models.MoleModel
import com.example.lindsay.delta5.models.UserModel
import io.realm.*


class Application: android.app.Application() {
    open lateinit var realm: Realm
    open var user: User? = null
    open lateinit var moles: RealmResults<Mole>

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm and load all relevant data
        val SCHEMA_VERSION = 1L

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("delta5.realm")
                .directory(this.filesDir)
//                .deleteRealmIfMigrationNeeded()         // temp
                .schemaVersion(SCHEMA_VERSION)
//                .migration(RealmMigrations())
                .build()

        Realm.removeDefaultConfiguration()
        Realm.setDefaultConfiguration(config)

        realm = Realm.getDefaultInstance()

        // Temporary for dev purposes
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()

        loadUser()
        loadDatabase()
    }

    private fun loadDatabase() {
        loadUser()
        loadMoles()
    }

    fun loadUser() {
        user = UserModel.loadUser(realm)

        if(user == null) {
            if(UserModel.saveUser(realm, User(age = 22, sex = 0))) {
                user = UserModel.loadUser(realm)
            }
        }

    }

    fun loadMoles() {
        moles = MoleModel.getAllMoles(realm)
    }

}

class RealmMigrations : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var oldVersion = oldVersion

        // DynamicRealm exposes an editable schema
        val schema = realm.schema

        // Migrate to version 1: Add a new class.
        if (oldVersion == 0L) {
            schema.create("User")
            oldVersion++
        }
    }
}