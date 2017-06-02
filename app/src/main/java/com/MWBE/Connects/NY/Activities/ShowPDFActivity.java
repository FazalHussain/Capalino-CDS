package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.MWBE.Connects.NY.CustomViews.CustomButton;
import com.MWBE.Connects.NY.R;

import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;

public class ShowPDFActivity extends Activity {

    private WebView wv;
    private String url;
    private LinearLayout layout;
    private CustomButton emailbtn;
    private CustomButton donebtn;
    private ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        init();
    }

    private void init() {
        wv = (WebView) findViewById(R.id.pdfview);
        donebtn = (CustomButton) findViewById(R.id.donebtn);
        layout = (LinearLayout) findViewById(R.id.layout_header);
        donebtn.setVisibility(View.VISIBLE);

        Intent i = getIntent();
        if(i.getStringExtra("status").equalsIgnoreCase("pdf")) {

            showpdf();

        }

        else if(i.getStringExtra("status").equalsIgnoreCase("url")) {
            OpenLink();
        }

        else if(i.getStringExtra("status").equalsIgnoreCase("Video")) {
            PlayVideo();

        }



    }

    public void DoneClick(View view){
        finish();
    }

    public void layoutClick(View view){

    }

    private void showpdf() {
        try {

            Intent i = getIntent();
                url = i.getStringExtra("url");
                wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                wv.setBackgroundColor(Color.TRANSPARENT);
                wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);
                wv.getSettings().setBuiltInZoomControls(true);
                wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                if (url.endsWith(".pdf")){
                    String urlEncoded = URLEncoder.encode(url, "UTF-8");
                    url = "https://docs.google.com/viewer?url=" + urlEncoded;
                    //url = String.format("https://docs.google.com/viewer?url={0}", url);
                }
                wv.loadUrl(url);
        }catch (Exception e){
            hidePB();
            e.printStackTrace();
        }

    }

    private void OpenLink() {
        if(getIntent().getStringExtra("status")!=null)
            showPB("Loading...");
            WebSettings settings = wv.getSettings();
            settings.setJavaScriptEnabled(true);


            //The default value is true for API level android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 and below,
            //and false for API level android.os.Build.VERSION_CODES.JELLY_BEAN and above.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                settings.setAllowUniversalAccessFromFileURLs(true);

            settings.setBuiltInZoomControls(true);
            wv.setWebChromeClient(new WebChromeClient());
            wv.loadUrl(getIntent().getStringExtra("url"));

            wv.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                    view.loadUrl(urlNewString);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (pb != null && pb.isShowing())
                        pb.dismiss();

                }
            });
    }

    private void PlayVideo(){
            url = getIntent().getStringExtra("url");
            wv.getSettings().setJavaScriptEnabled(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                wv.getSettings().setAllowUniversalAccessFromFileURLs(true);

            wv.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            wv.setWebChromeClient(new WebChromeClient());
            wv.loadUrl(url);
        }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(wv, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch(InvocationTargetException ite) {
            ite.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    public void BrowseClick(View view){
        Intent i = new Intent(ShowPDFActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(ShowPDFActivity.this,SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(ShowPDFActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){

        try{
            Intent i = new Intent(ShowPDFActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
       finish();
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(ShowPDFActivity.this);
                pb.setMessage(message);
                pb.setCancelable(false);
                pb.show();
            }
        });

    }

    void hidePB() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pb != null && pb.isShowing())
                    pb.dismiss();
            }
        });

    }
}
