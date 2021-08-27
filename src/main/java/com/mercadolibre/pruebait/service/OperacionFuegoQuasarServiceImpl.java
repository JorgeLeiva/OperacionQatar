package com.mercadolibre.pruebait.service;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mercadolibre.pruebait.dto.PositionDto;
import com.mercadolibre.pruebait.model.Position;
import com.mercadolibre.pruebait.model.Satellite;
import com.mercadolibre.pruebait.repository.OperacionFuegoQuasarRepository;

/**
 * Implementación del servicio {@code OperacionFuegoQuasarService}
 * @author jorgeleiva
 * @version 1.0.
 */
@Service
public class OperacionFuegoQuasarServiceImpl implements OperacionFuegoQuasarService {
	
	@Autowired
	private OperacionFuegoQuasarRepository operacionFuegoQuasarRepository;

	@Override
	@Transactional
	public PositionDto getLocation(float... distances) throws Exception {
		// Ecuación de Trilateración para triangular ubicación de la nave
		// Posición de los satélites:
		/*
		 * double[] Kenobi = { -500f, -200f }; 
		 * double[] Skywalker = { 100f, -100f };
		 * double[] Sato = { 500f, 100f };
		 */
		Position satellite1 = this.operacionFuegoQuasarRepository.findByOrdination(0).getPosition();
		Position satellite2 = this.operacionFuegoQuasarRepository.findByOrdination(1).getPosition();
		Position satellite3 = this.operacionFuegoQuasarRepository.findByOrdination(2).getPosition();
	
		// Valida que por lo menos existan tres satélites para la trilateración
		if (distances.length != 3) {
			throw new Exception("Debe enviar los tres satélites conocidos (Kenobi, Skywalker y Sato)");
		}

		ArrayRealVector P1 = new ArrayRealVector(new double[] { satellite1.getX(), satellite1.getY() });
		ArrayRealVector P2 = new ArrayRealVector(new double[] { satellite2.getX(), satellite2.getY() });
		ArrayRealVector P3 = new ArrayRealVector(new double[] { satellite3.getX(), satellite3.getY() });
		
		RealVector ex = P2.subtract(P1).mapDivide(P1.getDistance(P2));
		double i = ex.dotProduct(P3.subtract(P1));
		double distance = P3.getDistance(P1);
		ArrayRealVector vector = new ArrayRealVector(new double[] {distance, 0});
		RealVector ey = (P3.subtract(P1).subtract(ex.mapMultiply(i)))
				.mapDivide((vector.subtract(ex.mapMultiply(i))).getNorm());
		double d = P2.getDistance(P1);
		double j = ey.dotProduct(P3.subtract(P1));

		double R1 = distances[0];
		double R2 = distances[1];
		double R3 = distances[2];
		
		/**
		 * Hallar los puntos (x, y) por medio de la Trilateración
		 * x = ((R1² - R2² + d²) / 2d)
		 * y = ((R1² - R3² + i² + j²) / 2j - (i/j * x))
		 */
		double x = (Math.pow(R1,2) - Math.pow(R2,2) + Math.pow(d,2))/(2*d);
        double y = ((Math.pow(R1,2) - Math.pow(R3,2) + Math.pow(i,2) + Math.pow(j,2))/(2*j)) - ((i/j)*x);
        ArrayRealVector trilateracion = P1.add(ex.mapMultiply(x)).add(ey.mapMultiply(y)); 
        DecimalFormat df = new DecimalFormat("#.0");
        float positionX = Float.parseFloat(df.format(trilateracion.toArray()[0]));
        float positionY = Float.parseFloat(df.format(trilateracion.toArray()[1]));
        
		/*
		 * Validación de que la nave se encuentra intersectada por el radio de los
		 * satélites. Si el valor de z es un número negativo es porque el radio de los
		 * satélites no se intersectan, Si el valor es cero o superior el radio del
		 * satélite se intersecta con los otros dos.
		 */
        //double z = Math.sqrt(Math.pow(R1, 2) - Math.pow(positionX, 2) - Math.pow(positionY, 2));
        //boolean valid = Double.isNaN(z);
        
		return new PositionDto(positionX, positionY);
	}

	@Override
	public String getMessage(String[]... messages) throws Exception {
		String[] messageCompleted = null;
		String messageRecived = "";
		if (messages.length > 0) {
			messageCompleted = new String[messages[0].length];
			for (int i = 0; i < messages.length; i++) {
				for (int j = 0; j < messages[i].length; j++) {
					if (!messages[i][j].equals("")) {
						messageCompleted[j] = messages[i][j];
					}
				}
			}
		}
		for (String msg : messageCompleted) {
			if (msg == null) { return null; }
			messageRecived = messageRecived.concat(msg).concat(" ");
		}
		return messageRecived.trim();
	}

	@Override
	public List<Satellite> getSatellites() throws Exception {
		return this.operacionFuegoQuasarRepository.findAll();
	}

	@Override
	@Transactional
	public Satellite updateSatellite(Satellite satellite, String satelliteName) throws Exception {
		Satellite sat = this.getSatelliteByName(satelliteName);
		if (sat == null) { throw new NullPointerException("El nombre del satélite no es conocido"); }
		sat.setDistance(satellite.getDistance());
		String message = "";
		for (String msg : satellite.getMessage()) {
			msg = (msg.equals("")) ? "*" : msg;
			message = message.concat(msg).concat(";");
		}
		sat.setSeparateMessage(message);
		return this.operacionFuegoQuasarRepository.save(sat);
	}

	@Override
	public Satellite getSatelliteByOrder(int order) throws Exception {
		return this.operacionFuegoQuasarRepository.findByOrdination(order);
	}

	@Override
	@Transactional
	public void updateSatellites(Satellite[] satellites) throws Exception {
		for (int i = 0; i < satellites.length; i++) {
			String message = "";
			Satellite sat = this.getSatelliteByName(satellites[i].getName());
			sat.setDistance(satellites[i].getDistance());
			sat.setOrdination(i);
			for (String msg : satellites[i].getMessage()) {
				msg = (msg.equals("")) ? "*" : msg;
				message = message.concat(msg).concat(";");
			}
			sat.setSeparateMessage(message.substring(0, (message.lastIndexOf(";"))));
			this.operacionFuegoQuasarRepository.save(sat);
		}
	}

	@Override
	public Satellite getSatelliteByName(String name) throws Exception {
		return this.operacionFuegoQuasarRepository.findByName(name);
	}

}
