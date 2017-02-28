package com.example.yls.webviewjavascript;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button mButton1, mButton2;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/web.html");
        mWebView.addJavascriptInterface(MainActivity.this, "android");
    }

    private void initViews() {
        mButton1 = (Button) findViewById(R.id.btn1);
        mButton2 = (Button) findViewById(R.id.btn2);
        mWebView = (WebView) findViewById(R.id.webView);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljs()");
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljswith(" + "'http://blog.csdn.net/Leejizhou'" + ")");
            }
        });


    }

    @JavascriptInterface
    public void startFunction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "show", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @JavascriptInterface
    public void startFunction(String text) {
        new AlertDialog.Builder(MainActivity.this).setMessage(text).show();
    }

    @JavascriptInterface
    public void add(int a, int b) {
        int c = a + b;
        Toast.makeText(MainActivity.this, "a+b=" + c, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void StartActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SecActivity.class);
        startActivity(intent);
    }

    @JavascriptInterface
    public void call(String num) {
        if (num.isEmpty()) {
            Toast.makeText(MainActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        startActivity(intent);
    }

    @JavascriptInterface
    public void sendMsg(String num, String msg) {

        if (num.isEmpty() || msg.isEmpty()) {
            Toast.makeText(MainActivity.this, "号码或短信不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> list = smsManager.divideMessage(msg);
        for (String text : list) {
            smsManager.sendTextMessage(num, null, text, null, null);
        }
        Toast.makeText(MainActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();

    }


}
