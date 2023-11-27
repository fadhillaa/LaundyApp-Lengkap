package com.example.laundryapp.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FunctionHelper {
    public static String rupiahFormat(int value) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return "Rp " + formatter.format(value);
    }
    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return df.format(c);
    }

}
