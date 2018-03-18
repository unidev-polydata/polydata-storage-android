package com.unidev.polydata.storage.paperdb;

import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;
import com.unidev.polydata.storage.ChangablePolyStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;


/**
 * Paperdb storage for polydata records
 * Invoke Paper.init(context) before usage.
 */
public class PaperDBStorage implements ChangablePolyStorage {

    private static final String METADATA_KEY = "metadata";

    private String storage;

    public PaperDBStorage(String storageName) {
        this.storage = storageName;
    }


    @Override
    public <P extends Poly> P metadata() {
        return null;
    }

    @Override
    public BasicPoly fetchById(String id) {
        return Paper.book(storage).read(id, new BasicPoly());
    }

    @Override
    public Collection<BasicPoly> list() {
        List<String> keys = Paper.book(storage).getAllKeys();
        List<BasicPoly> list = new ArrayList<>();
        for(String key : keys) {
            list.add(fetchById(key));
        }
        return list;
    }

    @Override
    public long size() {
        return Paper.book(storage).getAllKeys().size();
    }

    @Override
    public BasicPoly persist(Poly poly) {
        Book book = Paper.book(storage);
        BasicPoly polyToStore = new BasicPoly();
        polyToStore.putAll(poly);
        book.write(polyToStore._id(), polyToStore);
        return polyToStore;
    }

    @Override
    public boolean remove(String id) {
        Book book = Paper.book(storage);
        if (book.contains(id)) {
            Paper.book(storage).delete(storage);
            return true;
        }
        return false;
    }
}