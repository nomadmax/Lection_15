package com.nomadmax.lection15;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Max on 28.03.16.
 */
public class HandlerThreadActivity extends AppCompatActivity {

    private DownloadHandlerThread mDownloadHandlerThread;

    private Button btnDownload, btnStop, btnStart;
    private EditText etUrl;
    private ProgressBar pbProgress;
    private TextView tvContent, tvProgress;
    private Handler mDownloadHandler;
    private Handler mMainThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handlerthread);
        mMainThreadHandler = new Handler();
        initViews();
        initHandlerThread();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        mDownloadHandlerThread.quit();
        super.onDestroy();
    }

    private void initHandlerThread() {
        mDownloadHandlerThread = new DownloadHandlerThread("DownloadHandlerThread");
        mDownloadHandlerThread.start();
        mDownloadHandlerThread.getLooper();
        mDownloadHandlerThread.setCallback(new DownloadHandlerThread.Callback() {
            @Override
            public void success(final String _result) {
//                new Handler (Looper.getMainLooper()).post(new Runnable() {
//                tvContent.setText(_result);
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(_result);
                    }
                });
            }
            @Override
            public void failure(String _error) {
                Toast.makeText(HandlerThreadActivity.this, _error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setListeners() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url;
                try {
                    url = new URL(etUrl.getText().toString());
                } catch (MalformedURLException e) {
                    Toast.makeText(HandlerThreadActivity.this, "URL error", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDownloadHandler = mDownloadHandlerThread.getDownloadHandler();
                Message msg = mDownloadHandler.obtainMessage(DownloadHandlerThread.MESSAGE_DOWNLOAD, url);
                mDownloadHandler.sendMessage(msg);
//                msg.sendToTarget();
            }
        });


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadHandlerThread.quit();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHandlerThread();
            }
        });
    }


    private void initViews() {
        btnDownload = (Button) findViewById(R.id.download_button);
        btnStop = (Button) findViewById(R.id.stop_button);
        btnStart = (Button) findViewById(R.id.start_button);
        etUrl = (EditText) findViewById(R.id.url_edittext);
        pbProgress = (ProgressBar) findViewById(R.id.downloading_progress_bar);
        tvContent = (TextView) findViewById(R.id.content_textview);
        tvProgress = (TextView) findViewById(R.id.progress_text);
    }
}
