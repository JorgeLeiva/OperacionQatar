package com.mercadolibre.pruebait.model;

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
public class Payload implements Serializable {

	private static final long serialVersionUID = 6923083820786629724L;
	private Satellite[] satellites;

}
