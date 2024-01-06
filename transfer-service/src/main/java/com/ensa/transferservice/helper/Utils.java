package com.ensa.transferservice.helper;


import java.math.BigDecimal;

public class Utils {
    public static final BigDecimal  MAX_TRANSFER_LIMIT_PER_TRANSACTION = BigDecimal.valueOf(2000.00);
    public static final BigDecimal  ANNUAL_AMOUNT_TRANSFER_LIMIT = BigDecimal.valueOf(20000.00);
    public static final BigDecimal   MAX_TRANSFER_LIMIT_PER_TRANSACTION_FOR_AGENT = BigDecimal.valueOf(80000.00);
    public static final Integer NUMBER_OF_DAYS_FOR_TRANSFER_TO_EXPIRE = 7;
    public static final BigDecimal TRANSFER_NOTIFICATION_COST = BigDecimal.valueOf(10);

}
