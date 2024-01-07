package com.ensa.transferservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)

@Getter
@AllArgsConstructor
public enum MsgType {

    TO_RECIPIENT,
    TO_CLIENT,
    OTP

}
