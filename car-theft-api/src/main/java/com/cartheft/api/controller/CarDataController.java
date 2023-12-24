package com.cartheft.api.controller;

import com.cartheft.api.model.CarData;
import com.cartheft.api.service.CarDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarDataController {

    @Autowired
    private CarDataService carService;

    @GetMapping("/cars")
    public Page<CarData> getCountryOfOriginByCarModel(
            @RequestParam(name = "carBrand") String carBrand,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return carService.getCountryOfOriginByCarModel(carBrand, page, pageSize);
    }
}

