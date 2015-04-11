/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.ejb;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.health.smart.entity.Device;
import com.health.smart.util.GsonHelper;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Draco CHAU
 */
@Singleton
public class GCMBean {

    @PersistenceContext
    private EntityManager em;

    public int boardcast(String message) throws Exception {
        List<Device> devices = em.createQuery("SELECT d FROM Device d").getResultList();
        if (!devices.isEmpty()) {
            try {
                System.out.println("Prepare send GCM");
                String json = generateGCMRequest(devices, message);
                System.out.println("Data ready: " + json);
                return sendGCM(json);
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
        return 0;
    }

    public int sendMsg(int patientId, String message) throws Exception {
        List<Device> devices = em.createQuery("SELECT d FROM Device d WHERE d.patientId = :pid")
                .setParameter("pid", patientId).getResultList();
        if (!devices.isEmpty()) {
            try {
                System.out.println("Prepare send GCM");
                String json = generateGCMRequest(devices, message);
                System.out.println("Data ready: " + json);
                return sendGCM(json);
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
        return 0;
    }

    private int sendGCM(String gcmData) throws Exception {
        byte[] postData = gcmData.getBytes(Charset.forName("UTF-8"));
        int postDataLength = postData.length;
        URL url = new URL("http://android.googleapis.com/gcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=AIzaSyDWPrVhJqbSf9tkkYw2scP7-qjQcuQxIk8");
        conn.setRequestProperty("content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            String result = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;
            while ((output = br.readLine()) != null) {
                result += output;
            }
            System.out.println("Response message: " + result);
            JsonObject obj = GsonHelper.fromString(result);
            return obj.getAsJsonPrimitive("success").getAsInt();
        } else {
            throw new Exception("HTTP Request Failure : " + responseCode);
        }
    }

    private String generateGCMRequest(List<Device> devices, String message) throws Exception {
        JsonArray jsonDeviceArray = new JsonArray();
        for (Device d : devices) {
            JsonPrimitive p = new JsonPrimitive(d.getDeviceId());
            jsonDeviceArray.add(p);
        }
        JsonObject json = new JsonObject();
        json.add("registration_ids", jsonDeviceArray);

        JsonObject data = new JsonObject();
        data.addProperty("message", message);
        json.add("data", data);
        return json.toString();
    }

}
