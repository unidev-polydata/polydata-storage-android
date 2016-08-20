package com.unidev.polydata.storage;

import android.content.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unidev.polydata.domain.Poly;

import java.util.Collection;

/**
 * Storage backed by assets json files
 */
public class AssetStorage implements PolyStorage {

    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public void load(Context context) {

    }

    @Override
    public Poly fetchById(String s) {
        return null;
    }

    @Override
    public Collection<Poly> list() {
        return null;
    }
}
