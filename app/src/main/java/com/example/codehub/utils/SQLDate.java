package com.example.codehub.utils;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLDate {
    public static Date convert(String inputDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date utilDate = inputFormat.parse(inputDate);
            assert utilDate != null;
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
