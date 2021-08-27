package com.mercadolibre.pruebait.model;

import java.io.Serializable;

import com.mercadolibre.pruebait.dto.PositionDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ResponsePosition implements Serializable {
	
	private static final long serialVersionUID = -2689841254922369714L;
	private PositionDto position;
	private String message;
	
	public ResponsePosition(PositionDto position, String message) {
		super();
		this.position = position;
		this.message = message;
	}
}
