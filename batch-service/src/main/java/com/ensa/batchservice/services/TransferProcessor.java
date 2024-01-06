package com.ensa.batchservice.services;

import com.ensa.batchservice.dto.TransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class TransferProcessor implements ItemProcessor<TransferDto, TransferDto> {

    @Override
    public TransferDto process (TransferDto item) throws Exception {
        if (item.getTransferState().equals("TO_VALIDATE")){
            item.setAction("delete");
        } else if (item.getTransferState().equals("TO_SERVE") && item.getExpirationDate().isBefore(LocalDate.now())) {
            item.setTransferState("BLOCKED");
            item.setAction("update");
        } else{
            item.setAction("none");
        }
        return item;
    }
}
