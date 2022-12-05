package com.ahmetoral.caseproject.service;

import com.ahmetoral.caseproject.model.Weather;
import com.ahmetoral.caseproject.repo.WeatherRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImp implements WeatherService {

    private final WeatherRepo weatherRepo;

    @Override
    public Page<Weather> getDataWithPagination(Pageable pageable) {
        log.info("Paging data - pageable: " + pageable);
        return weatherRepo.findAll(pageable);
    }

    @SneakyThrows
    @Override
    public Page<Weather> getDataWithFilterAndPagination(String filterBy, String filter, Pageable pageable) {
        log.info("Paging and filtering data --filterBy:" + filterBy + "  --filter: " + filter + "  --pageable: " + pageable);
        switch (filterBy) {
            case "temperature":
                log.info("Paging and filtering data --temperature: " + filter);
                log.info("Paging and filtering data --temperatureInt: " + Integer.valueOf(filter));
                return weatherRepo.findAllByTemperature(Integer.valueOf(filter), pageable);
            case "country":
                return weatherRepo.findAllByCountryContaining(filter, pageable);
            case "city":
                return weatherRepo.findAllByCityContaining(filter, pageable);
            case "date":
                if (filter.length() == 4) { // only filter by year
                    Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + filter);
                    Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + (Integer.parseInt(filter) + 1));
                    return weatherRepo.findAllByDateBetween(dateStart, dateEnd, pageable);
                }
                break;
            case "condition":
                return weatherRepo.findAllByConditionContaining(filter, pageable);
        }
        log.error("Unknown filterBy - returning null");
        return null;
    }

    @Override
    public void createNewWeather(Weather weather) {
        log.info("is equals {}", weather.getCondition().equals(""));
        if (weather.getCondition().equals("") || weather.getCity().equals("") || weather.getCountry().equals("")) {
            log.info("Invalid weather data {}", weather);
            throw new IllegalArgumentException("Invalid weather data");
        }
        weatherRepo.save(weather);
    }
}
