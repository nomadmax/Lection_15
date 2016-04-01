package com.nomadmax.lection15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 28.03.16.
 */
public class AsyncTaskActivity extends AppCompatActivity {
    private List<String> mUrlList;
    private List<String> mContentsList;
    private DownloadTask mDownloadTask;
    private int mCounter;

    private Button btnDownload, btnAdd, btnShow, btnCancel;
    private EditText etUrl;
    private ProgressBar pbProgress;
    private TextView tvContent, tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);
        mUrlList = new ArrayList<>();
        mContentsList = new ArrayList<>();
        initViews();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        if (mDownloadTask != null) {
            mDownloadTask.releaseCallback();
        }
        super.onDestroy();
    }

    private void setListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrlList.add(etUrl.getText().toString());
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentsList.size() == 0) return;
                tvContent.setText(mContentsList.get(++mCounter % mContentsList.size()));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownloadTask != null) {
                    mDownloadTask.cancel(false);
                }
            }
        });


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mCounter = -1;
                v.setClickable(false);
                pbProgress.setVisibility(View.VISIBLE);
                tvProgress.setText("");
                if (mDownloadTask != null) {
                    mDownloadTask.cancel(true);
                    mDownloadTask.releaseCallback();
                }
                mDownloadTask = new DownloadTask();
                mDownloadTask.setCallback(new DownloadTask.Callback() {
                    @Override
                    public void success(List<String> _result) {
                        if (isActivityDead()) {
                            return;
                        }
                        mContentsList = _result;
                        pbProgress.setVisibility(View.INVISIBLE);
                        tvProgress.setText("");
                        v.setClickable(true);
                    }

                    @Override
                    public void progress(String _current) {
                        if (isActivityDead()) {
                            return;
                        }
                        tvProgress.setText(_current);
                    }

                    @Override
                    public void failure(Throwable _error) {
                        if (isActivityDead()) {
                            return;
                        }
                        Toast.makeText(AsyncTaskActivity.this, "ERROR: " + _error.getMessage(), Toast.LENGTH_SHORT).show();
                        pbProgress.setVisibility(View.INVISIBLE);
                        tvProgress.setText("");
                        v.setClickable(true);
                    }
                });
                mDownloadTask.execute(mUrlList.toArray(new String[]{}));
                mUrlList.clear();
            }
        });

    }

    private boolean isActivityDead() {
        return (AsyncTaskActivity.this == null || AsyncTaskActivity.this.isFinishing() || AsyncTaskActivity.this.isDestroyed());
    }


    private void initViews() {
        btnDownload = (Button) findViewById(R.id.download_button);
        btnCancel = (Button) findViewById(R.id.cance_button);
        btnAdd = (Button) findViewById(R.id.add_button);
        btnShow = (Button) findViewById(R.id.show_button);
        etUrl = (EditText) findViewById(R.id.url_edittext);
        pbProgress = (ProgressBar) findViewById(R.id.downloading_progress_bar);
        tvContent = (TextView) findViewById(R.id.content_textview);
        tvProgress = (TextView) findViewById(R.id.progress_text);
    }
}
