package k7i3.code.tnc.transport.helper;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by k7i3 on 08.09.15.
 */
public class DateTimeHelper {
    private static final String TAG = "====> DateTimeHelper";
    public static String now() {
        Log.d(TAG, "now()");
        SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        String date = iso8601.format(new Date());
        Log.d(TAG, "date: " + date); //2015-09-10T10:16:44Z
        return date;
    }
}
//    php: gmdate('Y-m-d\TH:i:s\Z'