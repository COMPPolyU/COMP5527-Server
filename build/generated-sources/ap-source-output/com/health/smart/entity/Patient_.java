package com.health.smart.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Patient.class)
public abstract class Patient_ {

	public static volatile SingularAttribute<Patient, String> password;
	public static volatile SingularAttribute<Patient, String> engName;
	public static volatile SingularAttribute<Patient, String> gender;
	public static volatile SingularAttribute<Patient, Date> dob;
	public static volatile SingularAttribute<Patient, String> identity;
	public static volatile SingularAttribute<Patient, Double> weight;
	public static volatile SingularAttribute<Patient, Integer> id;
	public static volatile SingularAttribute<Patient, String> email;
	public static volatile SingularAttribute<Patient, String> token;
	public static volatile SingularAttribute<Patient, Double> height;

}

