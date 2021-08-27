package com.mercadolibre.pruebait.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
@Table(name = "satellite")
public class Satellite implements Serializable {

	private static final long serialVersionUID = -7061034064127161993L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private float distance;
	private String[] message;
	private String separateMessage;
	
	@ManyToOne(targetEntity = Position.class)
	@JoinColumn(name = "id_position")
	private Position position;
	private int ordination;
	
	public Satellite(String name, float distance, String[] message) {
		super();
		this.name = name;
		this.distance = distance;
		this.message = message;
	}
}
