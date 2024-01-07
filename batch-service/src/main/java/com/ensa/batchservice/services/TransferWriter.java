package com.ensa.batchservice.services;

import com.ensa.batchservice.dto.TransferDto;
import com.ensa.batchservice.feign.TransferFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class TransferWriter implements ItemWriter<TransferDto> {

    private final TransferFeignClient feignClient;

    @Override
    public void write (Chunk<? extends TransferDto> chunk) throws Exception {
        System.out.println("here in writer");
        List<TransferDto> itemsToSave = new ArrayList<>();
        List<TransferDto> idsToDelete = new ArrayList<>();

        for (TransferDto item : chunk) {
            switch (item.getAction()) {
                case "update":
                    item.setTransferState("BLOCKED");
                    itemsToSave.add(item);
                    break;
                case "delete":
                    idsToDelete.add(item);
                    break;
                case "none":
                    break;
            }
        }
        if (!itemsToSave.isEmpty()) {
            feignClient.saveAllTransfers(itemsToSave);
        }

        if (!idsToDelete.isEmpty()) {
            feignClient.deleteAllTransfers(idsToDelete);
        }
    }
}
