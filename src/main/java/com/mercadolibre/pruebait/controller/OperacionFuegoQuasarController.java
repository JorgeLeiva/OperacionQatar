package com.mercadolibre.pruebait.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.pruebait.dto.PositionDto;
import com.mercadolibre.pruebait.model.Payload;
import com.mercadolibre.pruebait.model.ResponsePosition;
import com.mercadolibre.pruebait.model.Satellite;
import com.mercadolibre.pruebait.service.OperacionFuegoQuasarService;
import com.mercadolibre.pruebait.util.DistanceUtil;
import com.mercadolibre.pruebait.util.MessageUtil;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET })
public class OperacionFuegoQuasarController {
	
	@Autowired
	private OperacionFuegoQuasarService operacionFuegoQuasarService;
	
	@PostMapping(value = "topsecret")
	public ResponseEntity<?> topSecret(@RequestBody() Payload data) {
		ResponsePosition responsePosition = null;
		PositionDto position = null;
		String message = null;
		try {
			this.operacionFuegoQuasarService.updateSatellites(data.getSatellites());
			position = this.operacionFuegoQuasarService.getLocation(DistanceUtil.getDistances(data.getSatellites())); 
			message = this.operacionFuegoQuasarService.getMessage(MessageUtil.getMessages(data.getSatellites()));
			if (position != null && message != null) {
				responsePosition = new ResponsePosition(position, message);
			} else {
				return new ResponseEntity<ResponsePosition>(HttpStatus.NOT_FOUND);
			}
		} catch (UnexpectedRollbackException e) {
			return new ResponseEntity<String>("No se ha podido ejecutar la acción", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ResponsePosition>(responsePosition, HttpStatus.OK);
	}
	
	@GetMapping(value = "topsecret_split")
	public ResponseEntity<?> topsecretSplit() {
		PositionDto position = null;
		ResponsePosition responsePosition = null;
		String message = null;
		String[][] messages = null;
		float[] distances = null;
		try {
			List<Satellite> sat = this.operacionFuegoQuasarService.getSatellites();
			distances = new float[sat.size()];
			messages = new String[sat.size()][];
			for (int i = 0; i < sat.size(); i++) {
				if (sat.get(i).getSeparateMessage() == null) {
					responsePosition = null;
					break;
				} else {
					distances[i] = sat.get(i).getDistance();
					String[] separate = sat.get(i).getSeparateMessage().split(";");
					messages[i] = Arrays.asList(separate).stream().map((x) -> x.replace("*", "")).toArray(String[]::new);
				}
			}
			position = this.operacionFuegoQuasarService.getLocation(distances); 
			message = this.operacionFuegoQuasarService.getMessage(messages);
			if (position != null && message != null) {
				responsePosition = new ResponsePosition(position, message);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("No hay suficiente información", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (responsePosition != null) {
			return new ResponseEntity<ResponsePosition>(responsePosition, HttpStatus.OK);
		}
		return new ResponseEntity<String>("No hay suficiente información", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(value = "topsecret_split/{satellite_name}")
	public ResponseEntity<String> topsecretSplit(@PathVariable("satellite_name") String satelliteName, @RequestBody() Satellite satellite) {
		try {
			this.operacionFuegoQuasarService.updateSatellite(satellite, satelliteName);
		} catch (NullPointerException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("No hay suficiente información", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Satélite Registrado", HttpStatus.OK);
	}
	
}
