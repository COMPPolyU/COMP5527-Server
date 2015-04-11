/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.ejb;

import com.health.smart.entity.Device;
import com.health.smart.entity.Message;
import com.health.smart.entity.Patient;
import com.health.smart.util.MD5Hash;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HMT-X230-00
 */
@Singleton
public class SystemEJB {

    @PersistenceContext
    private EntityManager em;

    public Integer validateToken(String token) throws Exception {
        try {
            Patient p = (Patient) em.createQuery("SELECT p FROM Patient p WHERE p.token = :token")
                    .setParameter("token", token).getSingleResult();
            return p.getId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void registerDeviceId(int patientId, String deviceId) throws Exception {
        try {
            Patient p = em.find(Patient.class, patientId);
            if (p != null) {
                List<Device> devices = em.createQuery("SELECT d FROM Device d WHERE d.deviceId = :did")
                        .setParameter("did", deviceId).getResultList();
                
                if (devices.isEmpty()){
                    Device device = new Device();
                    device.setDeviceId(deviceId);
                    device.setPatientId(p.getId());
                    device.setRegisteredAt(new Date());
                } else {
                    devices.get(0).setRegisteredAt(new Date());
                }
            } else {
                throw new Exception("Patient not found.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Message getMessage(int messageId) throws Exception {
        try {
            Message p = em.find(Message.class, messageId);
            if (p != null) {
                return p;
            } else {
                throw new Exception("Message not exists.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String getToken(String email, String password) throws Exception {
        try {
            Patient p = (Patient) em.createQuery("SELECT p FROM Patient p WHERE p.email = :email")
                    .setParameter("email", email).getSingleResult();

            if (p.getPassword() == null || p.getPassword().isEmpty()) {
                p.setPassword(MD5Hash.hash(password));
                p.setToken(MD5Hash.hash(UUID.randomUUID().toString()));
            }

            if (!MD5Hash.hash(password).equals(p.getPassword())) {
                throw new Exception("Password incorrect.");
            }
            return p.getToken();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
