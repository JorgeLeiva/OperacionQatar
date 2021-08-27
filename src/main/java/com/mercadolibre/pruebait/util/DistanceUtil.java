package com.mercadolibre.pruebait.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mercadolibre.pruebait.model.Satellite;

public class DistanceUtil {

	/**
	 * Método que convierte las distancias a un valor float primitivo
	 * @param data Información de los satélites enviada en el cuerpo del mensaje
	 * @return Arreglo de distancias de tipo float
	 */
	public static float[] getDistances(Satellite[] data) {
		List<Float> distances = Arrays.asList(data)
				.stream()
				.filter((filtrado) -> filtrado.getDistance() > 0)
				.map(filtrado -> filtrado.getDistance())
				.collect(Collectors.toList());
		float[] converterFloat = new float[distances.size()];
		for (int i = 0; i < distances.size(); i++) {
			converterFloat[i] = distances.get(i).floatValue();
		}
		return converterFloat;
	}

}
