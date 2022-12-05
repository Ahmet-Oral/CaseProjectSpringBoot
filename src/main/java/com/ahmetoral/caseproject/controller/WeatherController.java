package com.ahmetoral.caseproject.controller;


import com.ahmetoral.caseproject.model.Weather;
import com.ahmetoral.caseproject.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/page/{sortBy}/{isDesc}/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<Weather>> getPagedData(@PathVariable("sortBy") String sortBy, @PathVariable("isDesc") Boolean isDesc, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        log.info("Paging data without with sorting. - sortBy=" + sortBy + ", isDesc=" + isDesc + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize);
        Pageable pageable = isDesc
                ? PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending())
                : PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        Page<Weather> page = weatherService.getDataWithPagination(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/page/{sortBy}/{isDesc}/{pageNumber}/{pageSize}/{filterBy}/{filter}")
    public ResponseEntity<Page<Weather>> getPagedAndFilteredData(@PathVariable("sortBy") String sortBy, @PathVariable("isDesc") Boolean isDesc,
                                                                 @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize,
                                                                 @PathVariable("filterBy") String filterBy, @PathVariable("filter") String filter) {

        log.info("Paging data without with sorting and filtering. - " +
                "sortBy=" + sortBy + ", isDesc=" + isDesc + ", pageNumber=" + pageNumber + ", pageSize="
                + pageSize + ", filterBy=" + filterBy + ", filter=" + filter);

        Pageable pageable = isDesc
                ? PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending())
                : PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        Page<Weather> page = weatherService.getDataWithFilterAndPagination(filterBy, filter, pageable);
        return ResponseEntity.ok().body(page);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNew(@RequestBody Weather weather) {
        log.info("Creating new weather data: " + weather);
        weatherService.createNewWeather(weather);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/weather/new").toUriString());
        return ResponseEntity.created(uri).body("Weather data has been successfully created");

    }


}
