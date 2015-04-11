/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.entity;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author HMT-X230-00
 */
public class DayMeasurement {
    private String ObjectId;
    private int patientId;
    private String date; //
    private String time; //
    private Date measureDate;
    private Double bph; //
    private Double bpl; //
    private Double bg; //
    private String submittedAt; //

    public String getObjectId() {
        return ObjectId;
    }

    public void setObjectId(String ObjectId) {
        this.ObjectId = ObjectId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(Date measureDate) {
        this.measureDate = measureDate;
    }

    

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public Double getBph() {
        return bph;
    }

    public void setBph(Double bph) {
        this.bph = bph;
    }

    public Double getBpl() {
        return bpl;
    }

    public void setBpl(Double bpl) {
        this.bpl = bpl;
    }

    public Double getBg() {
        return bg;
    }

    public void setBg(Double bg) {
        this.bg = bg;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.ObjectId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DayMeasurement other = (DayMeasurement) obj;
        if (!Objects.equals(this.ObjectId, other.ObjectId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DayMeasurement{" + "ObjectId=" + ObjectId + '}';
    }

    
    
    
}
