package com.mercadolibre.pruebait.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mercadolibre.pruebait.model.Satellite;

public class MessageUtil {
	
	/**
	 * Método que retorna los mensajes en los satélites
	 * @param data Información de los satélites enviada en el cuerpo del mensaje
	 * @return Arreglo de mensajes de tipo String
	 */
	public static String[][] getMessages(Satellite[] data) {
		List<String[]> messages = Arrays.asList(data)
				.stream()
				.filter((filtrado) -> filtrado.getMessage().length > 0)
				.map(filtrado -> filtrado.getMessage())
				.collect(Collectors.toList());
		String[][] msgResolver = new String[messages.size()][];
		for (int i = 0; i < messages.size(); i++) {
			String[] aux = new String[messages.get(i).length];
			for (int j = 0; j < messages.get(i).length; j++) {
				aux[j] = messages.get(i)[j];
			}
			msgResolver[i] = aux;
		}
		return msgResolver;
	}

}
