package com.ahmetoral.inventorymanagement.repo;


import com.ahmetoral.inventorymanagement.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepo extends JpaRepository<Weather, Integer> {
}
