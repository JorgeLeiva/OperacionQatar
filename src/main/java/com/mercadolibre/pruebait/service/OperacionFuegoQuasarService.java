package com.mercadolibre.pruebait.service;

import java.util.List;

import com.mercadolibre.pruebait.dto.PositionDto;
import com.mercadolibre.pruebait.model.Satellite;

/**
 * Servicio para la gestión de la localización de la nave portacarga imperial
 * y el mensaje enviado hacia los satélites
 * @author jorgeleiva
 * @version 1.0.
 */
public interface OperacionFuegoQuasarService {
	
	public PositionDto getLocation(float... distances) throws Exception;
	public String getMessage(String[]... messages) throws Exception;
	
	public List<Satellite> getSatellites() throws Exception;
	public Satellite updateSatellite(Satellite satellite, String satelliteName) throws Exception;
	public void updateSatellites(Satellite[] satellites) throws Exception;
	public Satellite getSatelliteByOrder(int order) throws Exception;
	public Satellite getSatelliteByName(String name) throws Exception;

}
