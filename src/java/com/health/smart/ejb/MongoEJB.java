/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.ejb;

import com.health.smart.entity.ChartData;
import com.health.smart.entity.DayMeasurement;
import com.health.smart.entity.Measurement;
import com.health.smart.util.MongoC;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class MongoEJB {

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public void saveMeasurement(List<Measurement> measurements) throws Exception {
        for (Measurement m : measurements) {
            BasicDBObject doc = new BasicDBObject("type", m.getType())
                    .append("date", df.parse(m.getDate()))
                    .append("time", m.getTime())
                    .append("patientId", m.getPatientId())
                    .append("value", m.getValue());
            MongoC.insert("measurement", doc);
        }
    }
    
    public void saveDayMeasurement(List<DayMeasurement> measurements) throws Exception {
        for (DayMeasurement m : measurements) {
            BasicDBObject fdoc = new BasicDBObject("patientId", m.getPatientId())
                    .append("date", m.getDate()).append("time", m.getTime());
            
            MongoC.remove("measurement", fdoc);
            
            BasicDBObject doc = new BasicDBObject("bpl", m.getBpl())
                    .append("bg", m.getBg())
                    .append("bph", m.getBph())
                    .append("date", m.getDate())
                    .append("time", m.getTime())
                    .append("patientId", m.getPatientId())
                    .append("measureDate", df.parse(m.getDate()))
                    .append("submittedAt", new Date());
            MongoC.insert("measurement", doc);
        }
    }
    
    public List<ChartData> getChartData(int patientId, String type) throws Exception {
        Date to = Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant());
        Date from = Date.from(LocalDateTime.now().minusDays(60).atZone(ZoneId.systemDefault()).toInstant());
        BasicDBObject measureCriteria = new BasicDBObject("$gte", from).append("$lte", to);
        BasicDBObject doc = new BasicDBObject("patientId", patientId).append("date", measureCriteria);
        List<DBObject> results = MongoC.findObject("measurement", doc);
        List<ChartData> chartData = new ArrayList<>();
        for (DBObject obj: results){
            chartData.add(new ChartData(obj));
        }
        return chartData;
    }

    public String getMeasurement(int patientId, Date from, Date to) throws Exception {
        BasicDBObject measureCriteria = new BasicDBObject("$gte", from).append("$lte", to);
        BasicDBObject doc = new BasicDBObject("patientId", patientId).append("date", measureCriteria);
        return MongoC.find("measurement", doc);
    }

    public String getDayMeasurement(int patientId, Date from, Date to) throws Exception {
        BasicDBObject measureCriteria = new BasicDBObject("$gte", from).append("$lte", to);
        BasicDBObject doc = new BasicDBObject("patientId", patientId)
                .append("measureDate", measureCriteria);
        return MongoC.find("measurement", doc);
    }
    
    @Schedule(minute="30", hour="*", persistent=false)
    public void dbtest() {
        try {
            System.out.println("try insert");
            BasicDBObject doc = new BasicDBObject("name", "MongoDB")
                    .append("type", "database")
                    .append("count", 1)
                    .append("lastupdate", new Date())
                    .append("info", new BasicDBObject("x", 203).append("y", 102));
            MongoC.insert("testcol", doc);
        } catch (Exception ex) {
            Logger.getLogger(MongoEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
