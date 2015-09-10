package k7i3.code.tnc.transport.helper;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by k7i3 on 08.09.15.
 */
public class SecurityHelper {
    private static final String TAG = "=====> SecurityHelper";
    private static final String SECURITY_ALGORITHM = "MD5";

    public static String encrypt(String data) {
        try {
            Log.d(TAG, "data: " + data);
            MessageDigest md = MessageDigest.getInstance(SECURITY_ALGORITHM);
            byte[] md5 = md.digest(data.getBytes("UTF-8"));
            Log.d(TAG, "md5: " + md5.toString());
            String base64 = Base64.encodeToString(md5, Base64.NO_WRAP); // NO_WRAP = without "/n" at the end of line in JSON
            Log.d(TAG, "base64: " + base64);
            return base64;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Log.d(TAG, "error: " + e.getMessage() + " " + e);
        }
        return null;
    }
}
