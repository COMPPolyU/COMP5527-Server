/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.ws;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.health.smart.ejb.MongoEJB;
import com.health.smart.ejb.SystemEJB;
import com.health.smart.entity.DayMeasurement;
import com.health.smart.util.GsonHelper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class MobileAPI {

    @Inject
    private SystemEJB sysEjb;
    @Inject
    private MongoEJB mongoEjb;

    final Base64.Decoder decoder = Base64.getDecoder();
    final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    @POST
    @Path("/getToken")
    public Response getToken(String payload) {
        try {
            JsonObject obj = GsonHelper.fromString(payload);
            String userId = obj.getAsJsonPrimitive("userId").getAsString();
            String password = obj.getAsJsonPrimitive("password").getAsString();
            String token = sysEjb.getToken(userId, password);
            JsonObject jObject = new JsonObject();
            jObject.addProperty("token", token);
            return Response.status(200).entity(jObject.toString()).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/savemeasurement")
    public Response saveMeasurement(String payload) {
        try {
            JsonObject request = GsonHelper.fromString(payload);
            String token = request.getAsJsonPrimitive("token").getAsString();
            JsonArray mess = request.getAsJsonArray("measurements");
            int pid = sysEjb.validateToken(token);
            List<DayMeasurement> measurements = new ArrayList<>();
            for (int i = 0; i < mess.size(); i++) {
                DayMeasurement m = GsonHelper.fromJsonElement(mess.get(i), DayMeasurement.class);
                m.setPatientId(pid);
                measurements.add(m);
            }
            mongoEjb.saveDayMeasurement(measurements);
            JsonObject jObject = new JsonObject();
            jObject.addProperty("result", "success");
            return Response.status(200).entity(jObject.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/getmeasurement")
    public Response getMeasurement(String payload) {
        try {
            JsonObject request = GsonHelper.fromString(payload);
            String token = request.getAsJsonPrimitive("token").getAsString();
            int pid = sysEjb.validateToken(token);

            String from = request.getAsJsonPrimitive("from").getAsString();
            String to = request.getAsJsonPrimitive("to").getAsString();

            Date queryFrom = df.parse(from);
            Date queryTo = df.parse(to);

            System.out.println(pid);
            System.out.println(queryFrom);
            System.out.println(queryTo);

            String result = mongoEjb.getDayMeasurement(pid, queryFrom, queryTo);

            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/registerGCM")
    public Response registerGCMDeviceId(String payload) {
        try {
            JsonObject request = GsonHelper.fromString(payload);
            String token = request.getAsJsonPrimitive("token").getAsString();
            int pid = sysEjb.validateToken(token);

            String deviceId = GsonHelper.fromString(payload).getAsJsonPrimitive("deviceId").getAsString();
            sysEjb.registerDeviceId(pid, deviceId);
            JsonObject jObject = new JsonObject();
            jObject.addProperty("result", "success");

            return Response.status(200).entity(jObject.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

}
