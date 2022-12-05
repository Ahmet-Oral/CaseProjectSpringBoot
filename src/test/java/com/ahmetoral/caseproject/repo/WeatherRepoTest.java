package com.ahmetoral.caseproject.repo;

import com.ahmetoral.caseproject.model.Weather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {WeatherRepo.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.ahmetoral.caseproject.model"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class WeatherRepoTest {
    @Autowired
    private WeatherRepo weatherRepo;

    // given
    private final Weather weather = Weather.builder()
            .temperature(1)
            .city("city")
            .country("country")
            .id(1)
            .condition("condition")
            .date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"))
            .build();

    private final Weather weather1 = Weather.builder()
            .temperature(1)
            .city("city")
            .country("country")
            .id(1)
            .condition("condition")
            .date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"))
            .build();

    WeatherRepoTest() throws ParseException {
    }


    @Test
    void canFindAllByTemperature() {
        //when
        weatherRepo.save(weather);
        //then
        assertEquals(1, weatherRepo.findAllByTemperature(1, Pageable.unpaged()).toList().size());
    }
    @Test
    void cantFindAllByTemperature() {
        //when
        weatherRepo.save(weather);
        //then
        assertTrue(weatherRepo.findAllByTemperature(5, Pageable.unpaged()).toList().isEmpty());
    }


    @Test
    void canFindAllByCountryContaining() {
        // when
        weatherRepo.save(weather);
        // then
        assertEquals(1,weatherRepo.findAllByCountryContaining("country", Pageable.unpaged()).toList().size());
    }

    @Test
    void cantFindAllByCountryContaining() {
        // when
        weatherRepo.save(weather);
        // then
        assertTrue(weatherRepo.findAllByCountryContaining("cousdfntry", Pageable.unpaged()).toList().isEmpty());
    }


    @Test
    void canFindAllByCityContaining() {
        weatherRepo.save(weather);
        assertEquals(1, weatherRepo.findAllByCityContaining(weather.getCity(), Pageable.unpaged()).toList().size());
    }

    @Test
    void cantFindAllByCityContaining() {
        weatherRepo.save(weather);
        weatherRepo.save(weather1);
        assertTrue(weatherRepo.findAllByCityContaining("CityadsaTest", Pageable.unpaged()).toList().isEmpty());
    }

    @Test
    void testFindAllByDateBetween() throws ParseException {
        //when
        weatherRepo.save(weather);
        weatherRepo.save(weather1);
        Date dateStart = (new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));
        Date dateEnd = (new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2090"));
        // then
        assertEquals(1, weatherRepo.findAllByDateBetween(dateStart, dateEnd, Pageable.unpaged()).toList().size());
    }
    @Test
    void cantFindAllByDateBetween() throws ParseException {
        //when
        weatherRepo.save(weather);
        weatherRepo.save(weather1);
        Date dateStart = (new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2080"));
        Date dateEnd = (new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2090"));
        // then
        assertEquals(0, weatherRepo.findAllByDateBetween(dateStart,dateEnd, Pageable.unpaged()).toList().size());
    }

    @Test
    void canFindAllByConditionContaining() {
        // when
        weatherRepo.save(weather);
        //then
        assertEquals(1,weatherRepo.findAllByConditionContaining("condition", Pageable.unpaged()).toList().size());
    }

    @Test
    void cantFindAllByConditionContaining() {
        // when
        weatherRepo.save(weather);
        weatherRepo.save(weather1);
        //then
        assertTrue(weatherRepo.findAllByConditionContaining("foooooooo", Pageable.unpaged()).toList().isEmpty());
    }
}

