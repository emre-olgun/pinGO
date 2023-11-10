package org.oem.pinggo.repository;

import org.oem.pinggo.entity.ProfitOfDay;
import org.oem.pinggo.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ProfitOfDayRepository extends JpaRepository<ProfitOfDay, Long> {
    @Query("select (count(p) > 0) from ProfitOfDay p where p.date = :date and p.seller = :seller")
    boolean existsByDateAndSeller(@Param("date") LocalDate date, @Param("seller") Seller seller);
}