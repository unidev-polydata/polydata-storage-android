package com.unidev.polydata.storage.sqlite4a;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;
import com.unidev.polydata.storage.PolyStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sqlite4a.SQLite;
import sqlite4a.SQLiteCursor;
import sqlite4a.SQLiteDb;
import sqlite4a.SQLiteStmt;

/**
 * SQL lite backed poly storage
 */
public class SQLiteStorage implements PolyStorage {

    private SQLiteDb db;

    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public SQLiteStorage(String path) {
        db = SQLite.open(path, SQLite.OPEN_READWRITE | SQLite.OPEN_CREATE);
        initdb();
    }

    protected void initdb() {
        db.exec("CREATE TABLE IF NOT EXISTS polys( id TEXT PRIMARY KEY, data TEXT);");
    }

    public SQLiteDb fetchDb() {
        return db;
    }


    @Override
    public Poly fetchById(String id) {
        SQLiteStmt sqLiteStmt = db.prepare("SELECT data FROM polys WHERE id = ?");
        sqLiteStmt.bindString(1, id);
        Poly poly = null;
        SQLiteCursor cursor = sqLiteStmt.executeSelect();
        if (cursor.step()) {
            String data = cursor.getColumnString(1);
            poly = loadPoly(data);
        }
        return poly;
    }

    @Override
    public Collection<? extends Poly> list() {
        List<Poly> list = new ArrayList<>();
        SQLiteStmt stmt = db.prepare("SELECT * FROM polys ;");
        SQLiteCursor cursor = stmt.executeSelect();
        while(cursor.step()) {
            String data = cursor.getColumnString(1);// fetch data
            Poly poly = loadPoly(data);
            list.add(poly);
        }
        return list;
    }

    protected Poly loadPoly(String raw) {
        try {
            BasicPoly basicPoly = objectMapper.readValue(raw, BasicPoly.class);
            return basicPoly;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
