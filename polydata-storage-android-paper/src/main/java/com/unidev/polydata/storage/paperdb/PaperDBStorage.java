package com.unidev.polydata.storage.paperdb;

import com.unidev.polydata.domain.Poly;
import com.unidev.polydata.storage.PolyStorage;

import java.util.Collection;


/**
 * Paperdb storage for polydata records
 */
public class PaperDBStorage implements PolyStorage {

    private String storage;

    public PaperDBStorage(String storageName) {
        this.storage = storageName;
    }


    @Override
    public <P extends Poly> P metadata() {
        return null;
    }

    @Override
    public <P extends Poly> P fetchById(String id) {
        return null;
    }

    @Override
    public Collection<? extends Poly> list() {
        return null;
    }

    @Override
    public long size() {
        return 0;
    }
}