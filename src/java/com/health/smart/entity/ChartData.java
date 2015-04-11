/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.entity;

import com.mongodb.DBObject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HMT-X230-00
 */
public class ChartData implements Comparable<ChartData> {

    public final List<String> TIMES = Arrays.asList(new String[]{"AFTER_BED","BEFORE_BREAKFAST","AFTER_BREAKFAST","BEFORE_LUNCH",
        "AFTER_LUNCH","BEFORE_DINNER","AFTER_DINNER","BEFORE_BED"});

    private String objectId;
    private Date date;
    private String time;
    private double bph;
    private double bpl;
    private double bg;

    public ChartData(DBObject obj) {
        this.objectId = ((org.bson.types.ObjectId) obj.get("_id")).toString();
        this.bph = (Double) obj.get("bph");
        this.bpl = (Double) obj.get("bpl");
        this.bg = (Double) obj.get("bg");
        this.date = (Date) obj.get("measureDate");
        this.time = (String) obj.get("time");
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getBph() {
        return bph;
    }

    public void setBph(double bph) {
        this.bph = bph;
    }

    public double getBpl() {
        return bpl;
    }

    public void setBpl(double bpl) {
        this.bpl = bpl;
    }

    public double getBg() {
        return bg;
    }

    public void setBg(double bg) {
        this.bg = bg;
    }

    @Override
    public int compareTo(ChartData o) {
        if (o.getDate().equals(getDate())){
            return TIMES.indexOf(o.getTime()) - TIMES.indexOf(getTime());
        }
        return o.getDate().after(getDate()) ? -1 : 1;
    }

    @Override
    public String toString() {
        return "ChartData{" + "objectId=" + objectId + ", date=" + date + ", time=" + time + ", bph=" + bph + ", bpl=" + bpl + ", bg=" + bg + '}';
    }

}
