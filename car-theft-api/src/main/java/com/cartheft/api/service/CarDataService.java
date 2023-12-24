package com.cartheft.api.service;

import com.cartheft.api.model.CarData;
import com.cartheft.api.repository.CarDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CarDataService {

    @Autowired
    private CarDataRepository carRepository;

    public Page<CarData> getCountryOfOriginByCarModel(String carBrand, int page, int pageSize) {
        return carRepository.findByCarModel(carBrand, PageRequest.of(page, pageSize));
    }

}

