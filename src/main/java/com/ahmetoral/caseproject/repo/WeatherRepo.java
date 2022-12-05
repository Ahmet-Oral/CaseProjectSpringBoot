package com.ahmetoral.caseproject.repo;


import com.ahmetoral.caseproject.model.Weather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepo extends JpaRepository<Weather, Integer> {

    Page<Weather> findAllByTemperature(Integer filter, Pageable pageable);
    Page<Weather> findAllByCountryContaining(String filter, Pageable pageable);
    Page<Weather> findAllByCityContaining(String filter, Pageable pageable);
    Page<Weather> findAllByDateBetween(java.util.Date date, java.util.Date date2, Pageable pageable);
    Page<Weather> findAllByConditionContaining(String filter, Pageable pageable);

}
