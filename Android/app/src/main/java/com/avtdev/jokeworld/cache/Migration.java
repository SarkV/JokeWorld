package com.avtdev.jokeworld.cache;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        if (oldVersion < 105) {
            realm.deleteAll();
            return;
        }
    }
}
