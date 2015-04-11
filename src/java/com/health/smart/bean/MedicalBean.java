/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.smart.bean;

import com.health.smart.ejb.ClinicEJB;
import com.health.smart.ejb.MongoEJB;
import com.health.smart.entity.ChartData;
import com.health.smart.entity.MedicalHistory;
import com.health.smart.entity.Patient;
import com.health.smart.util.FCHelper;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

@Named
@ViewScoped
public class MedicalBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ClinicEJB ejb;
    @Inject
    private MongoEJB mongo;

    private DecimalFormat df = new DecimalFormat("#.00");
    private DateFormat dayF = new SimpleDateFormat("dd");
    private Patient selectedPatient = new Patient();

    private List<MedicalHistory> medicals = new ArrayList<>();
    private Map<String, List<ChartData>> dataMap = new HashMap<>();
    private Map<String, ChartSeries> chartSeriesMap = new HashMap();
    private LineChartModel bloodPressureModel;
    private LineChartModel bloodGlucoseModel;

    @PostConstruct
    public void refreshPatientList() {
        try {
            if (FCHelper.getParameter("patientid") != null) {
                selectedPatient = ejb.getPatient(FCHelper.getParameter("patientid"));
            } else {
                selectedPatient = ejb.getPatient("3");
            }
            medicals = ejb.getPatientMedicalHistory(selectedPatient.getId());
            List<ChartData> source = mongo.getChartData(selectedPatient.getId(), "ALL");
            System.out.println(source.size());
            dataMap = source.stream().collect(Collectors.groupingBy(ChartData::getType));
            createChartSeries();
            createModel();
        } catch (Exception e) {
            e.printStackTrace();
            FCHelper.addError(e);
        }
    }

    private void createChartSeries() {
        for (String s : dataMap.keySet()) {
            ChartSeries cs = new ChartSeries();
            List<ChartData> data = dataMap.get(s);
            Collections.sort(data);
            for (ChartData d : data) {
                cs.set(dayF.format(d.getDate()), d.getValue());
            }
            chartSeriesMap.put(s, cs);
        }
    }

    private void createModel() throws Exception {
        if (dataMap.containsKey("BPH") || dataMap.containsKey("BPL")) {
            bloodPressureModel = new LineChartModel();
            bloodPressureModel.setTitle("Blood Pressure Level");
            bloodPressureModel.setAnimate(true);
            bloodPressureModel.setLegendPosition("se");
            Axis yAxis = bloodPressureModel.getAxis(AxisType.Y);
            yAxis.setMin(50);
            yAxis.setMax(200);
            bloodPressureModel.getAxes().put(AxisType.X, new CategoryAxis("Time"));
            if (dataMap.containsKey("BPH")) {
                ChartSeries bph = chartSeriesMap.get("BPH");
                bph.setLabel("High Blood Pressure");
                bloodPressureModel.addSeries(bph);
            }
            if (dataMap.containsKey("BPL")) {
                ChartSeries bpl = chartSeriesMap.get("BPL");
                bpl.setLabel("Low Blood Pressure");
                bloodPressureModel.addSeries(bpl);
            }
        }

        if (dataMap.containsKey("BG")) {
            bloodGlucoseModel = new LineChartModel();
            bloodGlucoseModel.setTitle("Blood Glucose Level");
            bloodGlucoseModel.setAnimate(true);
            bloodGlucoseModel.setLegendPosition("se");
            Axis byAxis = bloodGlucoseModel.getAxis(AxisType.Y);
            byAxis.setMin(3);
            byAxis.setMax(10);
            bloodGlucoseModel.getAxes().put(AxisType.X, new CategoryAxis("Time"));
            ChartSeries bg = chartSeriesMap.get("BG");
            bg.setLabel("Blood Glucose");
            bloodGlucoseModel.addSeries(bg);
        }
    }

    public String getBMI() {
        if (selectedPatient.getHeight() == null
                || selectedPatient.getWeight() == null
                || selectedPatient.getHeight() < 1
                || selectedPatient.getWeight() < 1) {
            return "";
        } else {
            double bmi = selectedPatient.getWeight() / Math.pow((selectedPatient.getHeight() / 100d), 2.0);
            return df.format(bmi);
        }
    }

    public String getAge() {
        if (selectedPatient.getDob() == null) {
            return "";
        } else {
            LocalDate today = LocalDate.now();
            LocalDate birthday = (new Date(selectedPatient.getDob().getTime())).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Period p = Period.between(birthday, today);
            return Integer.toString(p.getYears());
        }
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public LineChartModel getBloodPressureModel() {
        return bloodPressureModel;
    }

    public void setBloodPressureModel(LineChartModel bloodPressureModel) {
        this.bloodPressureModel = bloodPressureModel;
    }

    public LineChartModel getBloodGlucoseModel() {
        return bloodGlucoseModel;
    }

    public void setBloodGlucoseModel(LineChartModel bloodGlucoseModel) {
        this.bloodGlucoseModel = bloodGlucoseModel;
    }

    public List<MedicalHistory> getMedicals() {
        return medicals;
    }

    public void setMedicals(List<MedicalHistory> medicals) {
        this.medicals = medicals;
    }

}
