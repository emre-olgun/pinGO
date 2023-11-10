package org.oem.pinggo.dto;

import lombok.Data;
import org.oem.pinggo.entity.ProfitOfDay;

import java.time.format.DateTimeFormatter;

@Data
public class ProfitOfDayResponse {
    Long id;
    Long sellerId;
    String date;
    Long  totalProfitfortheday;


    public ProfitOfDayResponse(ProfitOfDay entity) {
        this.id = entity.getId();
        this.sellerId = entity.getSeller().getId();
        this.date = entity.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.totalProfitfortheday = entity.getTotalProfitfortheday();
    }


    //for not found implementation

}
