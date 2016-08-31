/**
 * Copyright (c) 2015,2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unidev.app.testapp.core;

import android.content.Context;
import android.util.Log;

import com.unidev.core.di.AppContext;
import com.unidev.polydata.storage.AssetStorage;
import com.unidev.polydata.storage.sqlite4a.SQLiteStorage;

import java.io.File;

import sqlite4a.SQLite;

/**
 * Application custom backend logic
 */
public class Core {

    public static final String INSTANCE_NAME = "core";

    public static Core getInstance() {
        return AppContext.getInstance(INSTANCE_NAME, Core.class);
    }

    private static final String ASSETS_FILE = "file.json";
    private AssetStorage storage;

    private SQLiteStorage sqLiteStorage;

    public void load(Context context) {
        storage = new AssetStorage();
        storage.load(context, ASSETS_FILE);

        SQLite.loadLibrary(context);

        File favorites = new File(context.getFilesDir(), "favorites");
        Log.d("favorites", "Favorites : " + favorites);
        sqLiteStorage = new SQLiteStorage(favorites.getPath());
    }

    public AssetStorage fetchStorage() {
        return storage;
    }

    public SQLiteStorage fetchFavorites() {
        return sqLiteStorage;
    }

}
