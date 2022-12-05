package com.ahmetoral.caseproject.service;

import com.ahmetoral.caseproject.model.Weather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WeatherService {


    Page<Weather> getDataWithPagination(Pageable pageable);
    Page<Weather> getDataWithFilterAndPagination(String filterBy,String filter, Pageable pageable);
    void createNewWeather(Weather weather);
}
