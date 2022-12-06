package com.ahmetoral.caseproject.service;

import com.ahmetoral.caseproject.model.Weather;
import com.ahmetoral.caseproject.repo.WeatherRepo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {WeatherServiceImp.class})
@ExtendWith(MockitoExtension.class) // initialize mocks and clear resources after each test
class WeatherServiceImpTest {

    @Mock
    private WeatherRepo weatherRepoMock;

    private WeatherServiceImp underTest;

    @BeforeEach
    void setUp() {        // run before each test
        underTest = new WeatherServiceImp(weatherRepoMock);
    }

    @Test
    void canGetPaginatedData() {
        //given
        Pageable pageable = PageRequest.of(2, 10, Sort.by("id").descending());
        // when
        underTest.getDataWithPagination(pageable);
        // then
        verify(weatherRepoMock).findAll(pageable);
    }



    @Test
    void canGetDataWithFilterAndPaginationForTemperature() {
        // given
        Pageable pageable = PageRequest.of(2, 10, Sort.by("id").descending());
        String filter = "1";
        String filterBy = "temperature";
        // when
        underTest.getDataWithFilterAndPagination(filterBy, filter, pageable);
        // assert
        verify(weatherRepoMock).findAllByTemperature(Integer.valueOf(filter), pageable);
    }
    @Test
    void canGetDataWithFilterAndPaginationForCountry() {
        // given
        Pageable pageable = PageRequest.of(2, 10, Sort.by("id").descending());
        String filter = "Japan";
        String filterBy = "country";
        // when
        underTest.getDataWithFilterAndPagination(filterBy, filter, pageable);
        // assert
        verify(weatherRepoMock).findAllByCountryContaining(filter, pageable);
    }
    @Test
    void canGetDataWithFilterAndPaginationForCity() {
        // given
        Pageable pageable = PageRequest.of(2, 10, Sort.by("id").descending());
        String filter = "london";
        String filterBy = "city";
        // when
        underTest.getDataWithFilterAndPagination(filterBy, filter, pageable);
        // assert
        verify(weatherRepoMock).findAllByCityContaining(filter, pageable);
    }
    @SneakyThrows
    @Test
    void canGetDataWithFilterAndPaginationForDate() {
        // given
        Pageable pageable = PageRequest.of(2, 10, Sort.by("id").descending());
        String filter = "1999";
        String filterBy = "date";
        Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + filter);
        Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + (Integer.parseInt(filter) + 1));
        // when
        underTest.getDataWithFilterAndPagination(filterBy, filter, pageable);
        // assert
        verify(weatherRepoMock).findAllByDateBetween(dateStart, dateEnd, pageable);
    }
    @Test
    void canGetDataWithFilterAndPaginationForCondition() {
        // given
        Pageable pageable = PageRequest.of(2, 10, Sort.by("id").descending());
        String filter = "Foggy";
        String filterBy = "condition";
        // when
        underTest.getDataWithFilterAndPagination(filterBy, filter, pageable);
        // assert
        verify(weatherRepoMock).findAllByConditionContaining(filter, pageable);
    }

    @Test
    void canCreateNewWeather() {
        // given
        Weather weather = Weather.builder()
                .id(1)
                .temperature(20)
                .country("Turkey")
                .city("Istanul")
                .condition("Sunny")
                .build();
        // when
        underTest.createNewWeather(weather);
        // assert
        verify(weatherRepoMock).save(weather);

    }

}

