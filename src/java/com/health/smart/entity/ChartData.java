/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.entity;

import com.mongodb.DBObject;
import java.util.Date;

/**
 *
 * @author HMT-X230-00
 */
public class ChartData implements Comparable<ChartData> {

    private String objectId;
    private String type;
    private Double value;
    private Date date;
    private String time;

    public ChartData(DBObject obj) {
        this.objectId = ((org.bson.types.ObjectId) obj.get("_id")).toString();
        this.type = (String) obj.get("type");
        this.value = (Double) obj.get("value");
        this.date = (Date) obj.get("date");
        this.time = (String) obj.get("time");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public int compareTo(ChartData o) {
        return o.getDate().after(getDate()) ? -1 : 1;
    }

    @Override
    public String toString() {
        return "ChartData{" + "objectId=" + objectId + ", type=" + type + ", value=" + value + ", date=" + date + ", time=" + time + '}';
    }

}
