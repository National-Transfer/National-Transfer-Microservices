package com.ensa.kycservice.utils;

import com.ensa.kycservice.dto.AccountRequestDto;
import com.ensa.kycservice.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor

@Component
public class AccountUtil {

    private final WebClient.Builder webClient;

    public AccountResponseDto createAccountForClient(AccountRequestDto dto){
        return webClient.build()
                .post()
                .uri("/createAccount")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(AccountResponseDto.class)
                .block();
    }

    public AccountResponseDto getAccountForClient(Long ownerId){
        return webClient.build()
                .get()
                .uri("/owner/{ownerId}",ownerId)
                .retrieve()
                .bodyToMono(AccountResponseDto.class)
                .block();

    }
}
