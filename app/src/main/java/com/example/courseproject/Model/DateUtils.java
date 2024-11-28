package com.example.courseproject.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String convertToDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
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
    public static String convertToDateTime(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Đảm bảo chuỗi đầu vào là UTC

        // Định dạng đầu ra theo múi giờ địa phương
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        outputFormat.setTimeZone(TimeZone.getDefault()); // Sử dụng múi giờ của hệ thống hiện tại

        try {
            // Chuyển đổi từ chuỗi UTC thành đối tượng Date
            Date date = inputFormat.parse(inputDate);

            // Định dạng lại đối tượng Date thành chuỗi theo múi giờ địa phương
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

