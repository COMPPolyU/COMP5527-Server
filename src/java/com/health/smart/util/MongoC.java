/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.util;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author HMTSystem
 */
public class MongoC {

    public static final String database = "comp5527";
    private static MongoClient client;

    private static synchronized MongoClient getClient() throws UnknownHostException {
        if (client == null) {
            MongoCredential credential = MongoCredential.createMongoCRCredential("m.smart.health", "comp5527", "comp5527g3".toCharArray());
            client = new MongoClient(new ServerAddress("ds031947.mongolab.com", 31947), Arrays.asList(credential));
        }
        return client;
    }

    public static void insert(String collection, String document) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        coll.insert((DBObject) JSON.parse(document), WriteConcern.ACKNOWLEDGED);
    }
    
    public static void remove(String collection, DBObject criteria) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        coll.remove(criteria);
    }

    public static void insert(String collection, DBObject document) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        coll.insert(document, WriteConcern.ACKNOWLEDGED);
    }

    public static void update(String collection, String document) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        DBObject docObj = (DBObject) JSON.parse(document);
        coll.update((DBObject) JSON.parse((String) docObj.get("criteria")),
                (DBObject) JSON.parse((String) docObj.get("update")));
    }
    
    public static void update(String collection, DBObject original, DBObject newObj) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        coll.update(original,newObj);
    }

    public static String find(String collection, String document) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        DBObject docObj = (DBObject) JSON.parse(document);
        List<DBObject> objects;
        if (!docObj.containsField("projection")) {
            objects = coll.find((DBObject) JSON.parse((String) docObj.get("criteria"))).toArray();
        } else {
            objects = coll.find((DBObject) JSON.parse((String) docObj.get("criteria")),
                    (DBObject) JSON.parse((String) docObj.get("projection"))).toArray();
        }
        return objects.toString();
    }

    public static String find(String collection, DBObject document) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        List<DBObject> objects = coll.find(document).toArray();
        return objects.toString();
    }
    
    public static  List<DBObject>  findObject(String collection, DBObject document) throws Exception {
        DBCollection coll = getClient().getDB(database).getCollection(collection);
        return coll.find(document).toArray();
    }

}
