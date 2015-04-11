/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.Type;

/**
 *
 * @author HMTSystem
 */
public class GsonHelper {

    private static final Gson G = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static String toString(Object obj) {
        return G.toJson(obj);
    }

    public static JsonObject fromString(String str) {
        return new JsonParser().parse(str).getAsJsonObject();
    }

    public static <T extends Object> T fromString(String str, Type typeOfT) {
        return G.fromJson(str, typeOfT);
    }

    public static <T extends Object> T fromJsonElement(JsonElement jsonElement, Type typeOfT) {
        return G.fromJson(jsonElement, typeOfT);
    }
}
