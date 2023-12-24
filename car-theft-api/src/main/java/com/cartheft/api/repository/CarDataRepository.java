package com.cartheft.api.repository;

import com.cartheft.api.model.CarData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CarDataRepository extends PagingAndSortingRepository<CarData, Long>, CrudRepository<CarData, Long> {

    @Query("SELECT c FROM CarData c WHERE c.carBrand = :carBrand")
    Page<CarData> findByCarModel(@Param("carBrand") String carBrand, Pageable pageable);
}



