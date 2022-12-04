package com.example.browserapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText urlInput;
    ImageView clearUrl,  webBack, webForward, webHome, webRefresh, webShare;
    WebView webView;
    ProgressBar progressBar;
    String searchEngine;
    String homepage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.url_search_edit_text);
        clearUrl = findViewById(R.id.cancel_icon);
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progressBar);
        webHome = findViewById(R.id.web_home);
        webBack = findViewById(R.id.web_back);
        webForward = findViewById(R.id.web_forward);
        webRefresh = findViewById(R.id.web_refresh);
        webShare = findViewById(R.id.web_share);


        searchEngine = "google.com/search?q=";
        homepage = "google.com";

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        loadMyUrl(homepage);
        urlInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager)   getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(urlInput.getWindowToken(), 0);
                    loadMyUrl(urlInput.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        clearUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlInput.setText("");
            }
        });

        webHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl(homepage);
            }
        });

        webBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) webView.goBack();
            }
        });

        webForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()) webView.goForward();
            }
        });

        webRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });


        webShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,  webView.getUrl());
                intent.setType("text/plain");
                startActivity(intent);

            }
        });




    }

    void loadMyUrl(String url){
        boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
        if (matchUrl) webView.loadUrl(url);   // if its in url format web view load url
        else webView.loadUrl(searchEngine+url); // else search with search engine
    }

     class MyWebViewClient extends WebViewClient {
         @Override
         public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
             return super.shouldOverrideUrlLoading(view, request);
         }

         @Override
         public void onPageStarted(WebView view, String url, Bitmap favicon) {
             super.onPageStarted(view, url, favicon);
             urlInput.setText(webView.getUrl());
             progressBar.setVisibility(View.VISIBLE);
         }

         @Override
         public void onPageFinished(WebView view, String url) {
             super.onPageFinished(view, url);
             progressBar.setVisibility(View.INVISIBLE);

         }
     }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}