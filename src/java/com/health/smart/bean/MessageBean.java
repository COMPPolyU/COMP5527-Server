/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.bean;

import com.health.smart.ejb.GCMBean;
import com.health.smart.entity.Patient;
import com.health.smart.util.FCHelper;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class MessageBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private GCMBean ejb;
    private Patient selectedPatient = new Patient();
    private String message;

    public void sendGCM() {
        try {
            if (selectedPatient == null || selectedPatient.getId() == null) {
                throw new Exception("Select a patient first.");
            }
            int success = ejb.sendMsg(selectedPatient.getId(), message);
            message = null;
            selectedPatient = new Patient();
            FCHelper.addInfo(String.format("%d messages delivered.", success));
        } catch (Exception e) {
            FCHelper.addError(e);
        }
    }

    public void sendGCM(Patient p) {
        try {
            if (p == null || p.getId() == null) {
                throw new Exception("Select a patient first.");
            }
            int success = ejb.sendMsg(p.getId(), message);
            message = null;
            FCHelper.addInfo(String.format("%d messages delivered.", success));
        } catch (Exception e) {
            FCHelper.addError(e);
        }
    }

    public void boardcast() {
        try {
            int success = ejb.boardcast(message);
            message = null;
            FCHelper.addInfo(String.format("%d messages delivered.", success));
        } catch (Exception e) {
            FCHelper.addError(e);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }
}
