package com.paymybuddy.application.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public String currentDate() {
        Date currentDate = new Date();
        String dateString = dateFormat.format(currentDate);
        return dateString;
    };

}
