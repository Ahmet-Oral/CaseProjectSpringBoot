package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.Weather;
import com.ahmetoral.inventorymanagement.repo.WeatherRepo;
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
public class WeatherServiceImp implements WeatherService{

    private final WeatherRepo weatherRepo;



    @Override
    public Page<Weather> getDataWithPagination(Pageable pageable) {
        log.info("Paging data - pageable: " + pageable);
        return weatherRepo.findAll(pageable);
    }

    @SneakyThrows
    @Override
    public Page<Weather> getDataWithFilterAndPagination(String filterBy,String filter, Pageable pageable) {
        log.info("Paging and filtering data --filterBy:" + filterBy + "  --filter: "+filter+"  --pageable: " + pageable);

        switch (filterBy){
            case "temperature":
                log.info("Paging and filtering data --temperature: " + filter);
                log.info("Paging and filtering data --temperatureInt: " + Integer.valueOf(filter));
                return weatherRepo.findAllByTemperature(Integer.valueOf(filter), pageable);
            case "country":
                return weatherRepo.findAllByCountryContaining(filter, pageable);
            case "city":
                return weatherRepo.findAllByCityContaining(filter, pageable);
            case "date":
                if (filter.length() == 4){ // only filter by year
                    // todo cleanup the code
                    String sDate1="01/01/"+filter;
                    String sDate2="01/01/"+(Integer.parseInt(filter)+1);
                    log.info("Paging and filtering data --sDate2: " + sDate2);

                    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                    Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                    return weatherRepo.findAllByDateBetween(new java.sql.Date(date1.getTime()),new java.sql.Date(date2.getTime()), pageable);
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
        log.info("Creating new weather data: " + weather);
        weatherRepo.save(weather);
    }
}
