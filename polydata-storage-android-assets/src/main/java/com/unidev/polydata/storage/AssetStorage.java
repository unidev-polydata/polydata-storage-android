package com.unidev.polydata.storage;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.unidev.polydata.domain.BasicPolyList;
import com.unidev.polydata.domain.Poly;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Storage backed by assets json files
 */
public class AssetStorage implements PolyStorage {

    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private BasicPolyList basicPolyList;

    public void load(Context context, String filePath) {
        AssetManager assets = context.getAssets();
        try (InputStream inputStream = assets.open(filePath)){
            basicPolyList = objectMapper.readValue(inputStream, BasicPolyList.class);
        } catch (IOException e) {
            Log.e("AssetStorageLoader", "Failed to load storage from " + filePath);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Poly fetchById(String id) {
        for(Poly poly : basicPolyList) {
            if (id.equals(poly._id())) {
                return poly;
            }
        }
        return null;
    }

    @Override
    public Collection<Poly> list() {
        return basicPolyList;
    }
}
