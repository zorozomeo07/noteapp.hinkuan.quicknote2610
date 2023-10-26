package noteapp.hinkuan.quicknote2610.util;



import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtil {

    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }
}
