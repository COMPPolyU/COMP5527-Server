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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private DateFormat dayF = new SimpleDateFormat("MM/dd");
    private Patient selectedPatient = new Patient();

    private List<MedicalHistory> medicals = new ArrayList<>();

    private LineChartModel bloodPressureHighModel;
    private LineChartModel bloodPressureLowModel;
    private LineChartModel bloodGlucoseModel;

    private String[] selectedTimes = new String[]{"AFTER_BED"};

    @PostConstruct
    public void refreshPatientList() {
        try {
            if (FCHelper.getParameter("patientid") != null) {
                selectedPatient = ejb.getPatient(FCHelper.getParameter("patientid"));
            } else {
                selectedPatient = ejb.getPatient("1");
            }
            medicals = ejb.getPatientMedicalHistory(selectedPatient.getId());
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
            FCHelper.addError(e);
        }
    }

    public void refresh() throws Exception {
        createModel();
        List<ChartData> datas = mongo.getChartData(selectedPatient.getId());
        Collections.sort(datas);
        createSeries(datas);
    }

    private void createSeries(List<ChartData> source) throws Exception {
        Map<String, ChartSeries> bphLines = new HashMap<>();
        Map<String, ChartSeries> bpLLines = new HashMap<>();
        Map<String, ChartSeries> bgLines = new HashMap<>();

        List<String> showTimes = new ArrayList();
        if (selectedTimes != null) {
            showTimes.addAll(Arrays.asList(selectedTimes));
        }

        for (ChartData d : source) {
            String cat = d.getTime();
            if (showTimes.contains(cat)) {
                if (!bphLines.containsKey(cat)) {
                    ChartSeries cs = new ChartSeries();
                    cs.setLabel(cat);
                    bphLines.put(cat, cs);
                }
                bphLines.get(cat).set(dayF.format(d.getDate()), d.getBph());
                if (!bpLLines.containsKey(cat)) {
                    ChartSeries cs = new ChartSeries();
                    cs.setLabel(cat);
                    bpLLines.put(cat, cs);
                }
                bpLLines.get(cat).set(dayF.format(d.getDate()), d.getBpl());
                if (!bgLines.containsKey(cat)) {
                    ChartSeries cs = new ChartSeries();
                    cs.setLabel(cat);
                    bgLines.put(cat, cs);
                }
                bgLines.get(cat).set(dayF.format(d.getDate()), d.getBg());
            }
        }

        for (ChartSeries c : bphLines.values()) {
            bloodPressureHighModel.addSeries(c);
        }
        for (ChartSeries c : bpLLines.values()) {
            bloodPressureLowModel.addSeries(c);
        }
        for (ChartSeries c : bgLines.values()) {
            bloodGlucoseModel.addSeries(c);
        }

        fillEmpty(bloodPressureHighModel);
        fillEmpty(bloodPressureLowModel);
        fillEmpty(bloodGlucoseModel);
    }
    
    private void fillEmpty(LineChartModel model){
        if (model.getSeries().isEmpty()) {
            for (String s : selectedTimes) {
                ChartSeries cs = new ChartSeries();
                cs.setLabel(s);
                cs.set(dayF.format(new Date()), 0);
                model.addSeries(cs);
            }
        }
    }

    private void createModel() throws Exception {
        bloodPressureHighModel = new LineChartModel();
        bloodPressureHighModel.setTitle("Systolic Level");
        bloodPressureHighModel.setAnimate(true);
        bloodPressureHighModel.setLegendPosition("ne");
        bloodPressureHighModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));

        bloodPressureLowModel = new LineChartModel();
        bloodPressureLowModel.setTitle("Diastolic Level");
        bloodPressureLowModel.setAnimate(true);
        bloodPressureLowModel.setLegendPosition("ne");
        bloodPressureLowModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));

        bloodGlucoseModel = new LineChartModel();
        bloodGlucoseModel.setTitle("Glycemia Level");
        bloodGlucoseModel.setAnimate(true);
        bloodGlucoseModel.setLegendPosition("ne");
        bloodGlucoseModel.getAxes().put(AxisType.X, new CategoryAxis("Date"));

        Axis yAxis = bloodPressureHighModel.getAxis(AxisType.Y);
        yAxis.setMin(100);
        yAxis.setMax(200);

        yAxis = bloodPressureLowModel.getAxis(AxisType.Y);
        yAxis.setMin(60);
        yAxis.setMax(120);

        yAxis = bloodGlucoseModel.getAxis(AxisType.Y);
        yAxis.setMin(3.0);
        yAxis.setMax(30.0);
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

    public LineChartModel getBloodPressureHighModel() {
        return bloodPressureHighModel;
    }

    public void setBloodPressureHighModel(LineChartModel bloodPressureHighModel) {
        this.bloodPressureHighModel = bloodPressureHighModel;
    }

    public LineChartModel getBloodPressureLowModel() {
        return bloodPressureLowModel;
    }

    public void setBloodPressureLowModel(LineChartModel bloodPressureLowModel) {
        this.bloodPressureLowModel = bloodPressureLowModel;
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

    public String[] getSelectedTimes() {
        return selectedTimes;
    }

    public void setSelectedTimes(String[] selectedTimes) {
        this.selectedTimes = selectedTimes;
    }

}
