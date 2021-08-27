package com.mercadolibre.pruebait.dto;

import java.io.Serializable;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class PositionDto implements Serializable {
	
	private static final long serialVersionUID = 4046685383786292537L;
	private float x;
	private float y;
	
	public PositionDto(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

}
