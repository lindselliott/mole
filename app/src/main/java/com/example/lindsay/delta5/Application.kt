package com.example.lindsay.delta5

import com.example.lindsay.delta5.entities.User
import com.example.lindsay.delta5.models.UserModel
import io.realm.DynamicRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.FieldAttribute
import io.realm.RealmSchema
import io.realm.RealmMigration



class Application: android.app.Application() {
    private lateinit var realm: Realm
    open var user: User? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm and load all relevant data
        val SCHEMA_VERSION = 1L

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("delta5.realm")
                .directory(this.filesDir)
//                .schemaVersion(SCHEMA_VERSION)
//                .migration(RealmMigrations())
                .build()

        Realm.removeDefaultConfiguration()
        Realm.setDefaultConfiguration(config)

        realm = Realm.getDefaultInstance()

        // Temporary for dev purposes
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()

        getUser()
    }

    /**
     * Will load the user information if it exists and will create a new user if there is none in yet
     */
    private fun getUser() {
        user = UserModel.getUser(realm)

        if(user == null) {
            if(UserModel.saveUser(realm, User(age = 22, sex = 0))) {
                user = UserModel.getUser(realm)
            }
        }
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