package com.mercadolibre.pruebait.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mercadolibre.pruebait.model.Satellite;

@Repository
public interface OperacionFuegoQuasarRepository extends JpaRepository<Satellite, Long> {
	
	public Satellite findByOrdination(int order);
	public Satellite findByName(String name);

}
