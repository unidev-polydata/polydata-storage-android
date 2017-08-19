package com.unidev.polydata.storage.sqlite4a;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        return processQuery(sqLiteStmt);
    }

    /**
     * Fetch by poly id
     */
    public Poly fetchByPolyId(String _id) {
        SQLiteStmt sqLiteStmt = db.prepare("SELECT data FROM polys WHERE _id = ?");
        sqLiteStmt.bindString(1, _id);
        return processQuery(sqLiteStmt);
    }

    @Nullable
    public Poly processQuery(SQLiteStmt sqLiteStmt) {
        Poly poly = null;
        SQLiteCursor cursor = sqLiteStmt.executeSelect();
        if (cursor.step()) {
            String data = cursor.getColumnString(1);
            poly = loadPoly(data);
        }
        sqLiteStmt.close();
        return poly;
    }


    @Override
    public <P extends Poly> P metadata() {
        return null;
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
        stmt.close();
        return list;
    }

    @Override
    public long size() {
        SQLiteStmt stmt = db.prepare("SELECT COUNT(*) AS count FROM polys ;");
        return stmt.executeSelect().getColumnLong(1);
    }

    public boolean hasPoly(String id) {
        SQLiteStmt sqLiteStmt = db.prepare("SELECT data FROM polys WHERE id = ?");
        sqLiteStmt.bindString(1, id);
        SQLiteCursor cursor = sqLiteStmt.executeSelect();
        if (cursor.step()) {
            return true;
        }
        sqLiteStmt.close();
        return false;
    }

    public void addPoly(Poly poly) {
        try {
            String raw = objectMapper.writeValueAsString(poly);
            SQLiteStmt insertStmt = db.prepare("INSERT INTO polys(id, data) VALUES(?, ?);");
            insertStmt.bindString(1, poly._id());
            insertStmt.bindString(2, raw);
            insertStmt.executeInsert();
            insertStmt.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void removePoly(String id) {
        SQLiteStmt sqLiteStmt = db.prepare("DELETE FROM polys WHERE id = ?; ");
        sqLiteStmt.bindString(1, id);
        sqLiteStmt.executeUpdateDelete();
        sqLiteStmt.close();
    }

    public Poly loadPoly(String raw) {
        try {
            BasicPoly basicPoly = objectMapper.readValue(raw, BasicPoly.class);
            return basicPoly;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
