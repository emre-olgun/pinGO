package org.oem.pinggo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.dto.ProfitOfDayResponse;
import org.oem.pinggo.entity.ProfitOfDay;
import org.oem.pinggo.repository.ProfitOfDayRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfitOfDayService {

    private final ProfitOfDayRepository profitOfDayRepository;

    public void save(ProfitOfDay profitOfDay) {
        if (checkUniqu(profitOfDay)) {
            profitOfDayRepository.save(profitOfDay);
        }
    }

    private boolean checkUniqu(ProfitOfDay profitOfDay) {
        return !profitOfDayRepository.existsByDateAndSeller(profitOfDay.getDate(), profitOfDay.getSeller());

    }


    public ResponseEntity<?> findAllProfitRecords() {
List<ProfitOfDayResponse> profitOfDayResponseList=profitOfDayRepository.findAll().stream().map(ProfitOfDayResponse::new).toList();
        return ResponseEntity.ok(profitOfDayResponseList);
    }
}
