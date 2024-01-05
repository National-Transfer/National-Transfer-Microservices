package com.ensa.kycservice.utils;

import com.ensa.kycservice.dto.AccountRequestDto;
import com.ensa.kycservice.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor

@Component
public class AccountUtil {

    private final WebClient webClient;

    public AccountResponseDto createAccountForClient(AccountRequestDto dto){
        return webClient
                .post()
                .uri("")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(AccountResponseDto.class)
                .block();
    }

    public AccountResponseDto getAccountForClient(Long id){
        return webClient
                .get()
                .uri("",id)
                .retrieve()
                .bodyToMono(AccountResponseDto.class)
                .block();

    }
}
