/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.ejb;

import com.health.smart.entity.ChartData;
import com.health.smart.entity.DayMeasurement;
import com.health.smart.util.MongoC;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.text.DateFormat;
import java.text.NumberFormat;
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
import javax.inject.Inject;

@Singleton
public class MongoEJB {

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    @Inject
    private GCMBean ejb;

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

    public List<ChartData> getChartData(int patientId) throws Exception {
        Date to = Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant());
        Date from = Date.from(LocalDateTime.now().minusDays(60).atZone(ZoneId.systemDefault()).toInstant());
        BasicDBObject measureCriteria = new BasicDBObject("$gte", from).append("$lte", to);
        BasicDBObject doc = new BasicDBObject("patientId", patientId).append("measureDate", measureCriteria);
        List<DBObject> results = MongoC.findObject("measurement", doc);
        List<ChartData> chartData = new ArrayList<>();
        for (DBObject obj : results) {
            chartData.add(new ChartData(obj));
        }
        return chartData;
    }

    public String getDayMeasurement(int patientId, Date from, Date to) throws Exception {
        BasicDBObject measureCriteria = new BasicDBObject("$gte", from).append("$lte", to);
        BasicDBObject doc = new BasicDBObject("patientId", patientId)
                .append("measureDate", measureCriteria);
        return MongoC.find("measurement", doc);
    }

    @Schedule(minute = "*/15", hour = "*", persistent = false)
    public void checkMeasurement() throws Exception {
        BasicDBObject gt60 = new BasicDBObject("$gte", 160);
        BasicDBObject alertExists = new BasicDBObject("$exists", false);
        BasicDBObject doc = new BasicDBObject("bph", gt60).append("alerted", alertExists);
        List<DBObject> result = MongoC.findObject("measurement", doc);

        for (DBObject obj : result) {
            int pid = (Integer) obj.get("patientId");
            double value = (Double) obj.get("bph");
            int success = ejb.sendMsg(pid, String.format("Your blood pressure level is too high (%s)! Please make appointment ASAP!", NumberFormat.getInstance().format(value)));
            if (success > 0) {
                obj.put("alerted", true);
                BasicDBObject filter = new BasicDBObject();
                filter.append("_id", obj.get("_id"));
                MongoC.update("measurement", filter, obj);
            }
        }
    }

}
