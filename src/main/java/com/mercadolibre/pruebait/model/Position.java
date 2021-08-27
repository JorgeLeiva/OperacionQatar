package com.mercadolibre.pruebait.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "position")
public class Position implements Serializable {
	
	private static final long serialVersionUID = -345060770176637808L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPosition;
	private float x;
	private float y;
	
	public Position(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
}
