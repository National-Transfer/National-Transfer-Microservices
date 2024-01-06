package com.ensa.batchservice.services;


import com.ensa.batchservice.dto.TransferDto;
import com.ensa.batchservice.feign.TransferFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@RequiredArgsConstructor
@Service
public class TransferReader implements ItemReader<TransferDto> {

    private final TransferFeignClient feignClient;

    private Iterator<TransferDto> transferIterator;


    @Override
    public TransferDto read () throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (transferIterator == null || !transferIterator.hasNext()) {
//            List<TransferDto> transfers = new ArrayList<>();
//            BatchTransferDto batchDto = feignClient.getAllTransfersForBatch().getBody();
//            batchDto.getTransfersToValidate().stream().forEach(transfer -> {
//                transfers.add(transfer);
//            });
//            batchDto.getTransfersToServe().stream().forEach(transfer -> {
//                transfers.add(transfer);
//            });
            List<TransferDto> transfers = feignClient.getAllTransfersForBatch().getBody();
            if (transfers == null || transfers.isEmpty()) {
                return null;
            }
            transferIterator = transfers.iterator();
        }
        return transferIterator.hasNext() ? transferIterator.next() : null;
    }
}
