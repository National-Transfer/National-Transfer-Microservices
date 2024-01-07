package com.ensa.transferservice.services;

import com.ensa.transferservice.dto.requests.BatchTransferRequest;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.TransferState;
import com.ensa.transferservice.exceptions.ResourceNotFoundException;
import com.ensa.transferservice.repositories.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<Transfer> getAllTransfersForBatch(){

        List<Transfer> transfers = new ArrayList<>();

        System.out.println(transferRepo.findByTransferState(TransferState.TO_VALIDATE));

        transfers.addAll(transferRepo.findByTransferState(TransferState.TO_VALIDATE));

        System.out.println(transfers);

        transfers.addAll(transferRepo.findByTransferState(TransferState.TO_SERVE));

        System.out.println(transfers);


        return transfers;
    }

    public List<Transfer> getAllTransfersForClient(String clientId){
        return transferRepo.findByClientId(clientId);
    }

    public String generateOtpForSms(){

        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    public List<Transfer> saveAllTransfers(List<Transfer> transfers) {
        return transferRepo.saveAll(transfers);
    }

    public void deleteAllTransfers(List<Transfer> transfers) {
         transferRepo.deleteAll(transfers);
    }
}
