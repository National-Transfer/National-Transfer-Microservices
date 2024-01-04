package com.ensa.transferservice.services;

import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.TransferState;
import com.ensa.transferservice.exceptions.ResourceNotFoundException;
import com.ensa.transferservice.repositories.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepo transferRepo;

    public List<Transfer> getAllTransfers(){
        return transferRepo.findAll();
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
