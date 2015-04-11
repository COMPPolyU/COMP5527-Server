/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.ejb;

import com.health.smart.entity.Function;
import com.health.smart.entity.MedicalHistory;
import com.health.smart.entity.Patient;
import com.health.smart.entity.User;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HMT-X230-00
 */
@Singleton
public class ClinicEJB {

    @PersistenceContext
    private EntityManager em;

    public User login(String userId, String password) throws Exception {
        try {
            User u = em.find(User.class, userId);
            if (u == null) {
                throw new Exception("User not found.");
            }
            List<Function> functions = em.createQuery("SELECT f FROM Function f WHERE f.roles LIKE :role")
                    .setParameter("role", "%" + u.getRole() + "%").getResultList();
            u.setFunction(functions);
            return u;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<Patient> getPatients() throws Exception {
        try {
            return em.createQuery("SELECT p FROM Patient p ORDER BY p.id").getResultList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Patient getPatient(String patientId) throws Exception {
        try {
            return em.find(Patient.class, Integer.parseInt(patientId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<MedicalHistory> getPatientMedicalHistory(int patientId) throws Exception {
        try {
            return em.createQuery("SELECT m FROM MedicalHistory m WHERE m.patientId = :pid ORDER BY m.apptDate DESC")
                    .setParameter("pid", patientId).getResultList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void updatePatientDetails(Patient p) throws Exception {
        try {
            if (p.getId() == 0) {
                p.setPassword(null);
                p.setId(null);
                em.persist(p);
            } else {
                em.merge(p);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
