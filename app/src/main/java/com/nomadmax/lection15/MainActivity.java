package com.nomadmax.lection15;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnSleep, btnNewThread, btnAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();


    }

    private void setListeners() {
        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        btnNewThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            btnNewThread.setText("Text changed");
//                            Toast.makeText(MainActivity.this, "Entered Thread, going to sleep", Toast.LENGTH_SHORT).show();
                            Log.d("blabla", "Entered Thread, going to sleep");
                            Thread.sleep(3000);
                            Log.d("blabla", "Slept for a while, finishing");
//                            Toast.makeText(MainActivity.this, "Slept for a while, finishing", Toast.LENGTH_SHORT).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btnAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useAsyncTask();
            }
        });
    }

    private void useAsyncTask() {
        MyTask myTask = new MyTask();
        myTask.execute();
    }

    private void initViews() {
        btnSleep = (Button) findViewById(R.id.sleepButton);
        btnNewThread = (Button) findViewById(R.id.threadSleepButton);
        btnAsyncTask = (Button) findViewById(R.id.asyncTaskButton);

    }


    private class MyTask extends AsyncTask <Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Entered Thread, going to sleep", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this, "Slept for a while, finishing", Toast.LENGTH_SHORT).show();
            btnAsyncTask.setText("Text changed");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
