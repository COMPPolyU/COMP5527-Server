package com.health.smart.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Message.class)
public abstract class Message_ {

	public static volatile SingularAttribute<Message, Date> createdAt;
	public static volatile SingularAttribute<Message, String> createdBy;
	public static volatile SingularAttribute<Message, Integer> id;
	public static volatile SingularAttribute<Message, String> content;

}

