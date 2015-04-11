package com.health.smart.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MedicalHistory.class)
public abstract class MedicalHistory_ {

	public static volatile SingularAttribute<MedicalHistory, String> symptom;
	public static volatile SingularAttribute<MedicalHistory, Integer> patientId;
	public static volatile SingularAttribute<MedicalHistory, String> diagnose;
	public static volatile SingularAttribute<MedicalHistory, String> medicine;
	public static volatile SingularAttribute<MedicalHistory, Integer> id;
	public static volatile SingularAttribute<MedicalHistory, Date> apptDate;

}

