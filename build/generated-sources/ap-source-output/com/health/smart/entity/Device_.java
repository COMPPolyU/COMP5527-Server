package com.health.smart.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Device.class)
public abstract class Device_ {

	public static volatile SingularAttribute<Device, Integer> patientId;
	public static volatile SingularAttribute<Device, Date> registeredAt;
	public static volatile SingularAttribute<Device, Integer> id;
	public static volatile SingularAttribute<Device, String> deviceId;

}

