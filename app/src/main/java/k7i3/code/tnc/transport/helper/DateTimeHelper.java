package k7i3.code.tnc.transport.helper;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by k7i3 on 08.09.15.
 */
public class DateTimeHelper {
    private static final String TAG = "=====> DateTimeHelper";
    public static String now() {
        SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd/THH:mm:ss/Z", Locale.getDefault());
        String date = iso8601.format(new Date());
        Log.d(TAG, "date: " + date);
        return date;
//        return iso8601.format(new Date());
    }
}
//    php: gmdate('Y-m-d\TH:i:s\Z'