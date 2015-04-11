/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.bean;

import com.health.smart.ejb.ClinicEJB;
import com.health.smart.entity.Patient;
import com.health.smart.util.FCHelper;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class PatientBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ClinicEJB ejb;

    private List<Patient> patients;
    private Patient selectedPatient = new Patient();

    @PostConstruct
    public void refreshPatientList() {
        try {
            patients = ejb.getPatients();
        } catch (Exception e) {
            FCHelper.addError(e);
        }
    }

    public void registerPatient() {
        try {
            selectedPatient = new Patient();
            selectedPatient.setId(0);
        } catch (Exception e) {
            FCHelper.addError(e);
        }
    }

    public void updatePatientDetails() {
        try {
            ejb.updatePatientDetails(selectedPatient);
            refreshPatientList();
            FCHelper.addInfo("Update Success");
        } catch (Exception e) {
            FCHelper.addError(e);
        }
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

}
