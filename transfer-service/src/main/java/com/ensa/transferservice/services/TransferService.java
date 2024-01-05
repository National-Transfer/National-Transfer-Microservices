package com.ensa.transferservice.services;

import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.exceptions.ResourceNotFoundException;
import com.ensa.transferservice.repositories.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepo transferRepo;

    public Page<Transfer> getAllTransfers(int page, int size){
        Pageable pageRequest = PageRequest.of(page, size);
        return transferRepo.findAll(pageRequest);
    }

    public Transfer getTransferByReference(String reference){
        return transferRepo.findByReference(reference).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")

        );
    }
    public String generateOtpForSms(){

        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

}
