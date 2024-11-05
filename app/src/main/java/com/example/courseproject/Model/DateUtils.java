package com.example.courseproject.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String convertToDate(String inputDate) {
        // Định dạng ngày giờ đầu vào
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());

        // Định dạng ngày tháng năm đầu ra
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            // Chuyển đổi chuỗi thành đối tượng Date
            Date date = inputFormat.parse(inputDate);
            // Chuyển đổi đối tượng Date thành chuỗi theo định dạng mong muốn
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Hoặc bạn có thể trả về chuỗi rỗng hoặc một thông báo lỗi
        }
    }
}

