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
package com.unidev.polydata.storage;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.BasicPolyList;
import com.unidev.polydata.domain.Poly;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage backed by assets json files
 */
public class AssetStorage implements PolyStorage, Serializable {

    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private Map<String, BasicPoly> storage;
    private PolyStorageDTO storageDTO;

    /**
     * Load poly records from assets
     * @param context Application context
     * @param filePath Path to file in assets
     */
    public void load(Context context, String filePath) {
        AssetManager assets = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assets.open(filePath);
            storageDTO = objectMapper.readValue(inputStream, PolyStorageDTO.class);
            storage = new HashMap<>();
            for(BasicPoly poly : storageDTO.getRecords()) {
                storage.put(poly._id(), poly);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public Map<String, BasicPoly> getStorage() {
        return storage;
    }

    @Override
    public BasicPoly fetchById(String id) {
        return storage.get(id);
    }

    @Override
    public Collection<? extends Poly> list() {
        return storage.values();
    }

    public BasicPoly getMeta() {
        return storageDTO.getMeta();
    }

    public BasicPoly meta() {
        return storageDTO.getMeta();
    }

    public PolyStorageDTO storageDTO() {
        return storageDTO;
    }


}
