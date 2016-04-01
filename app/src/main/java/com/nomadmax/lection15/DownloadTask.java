package com.nomadmax.lection15;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Max on 28.03.16.
 */
public class DownloadTask extends AsyncTask<String, String, List<String>> {

    private List<String> mResult;
    private Callback mCallback;
    private Exception mError;

    public interface Callback{
        void success(List<String> _result);
        void progress(String _current);
        void failure(Throwable _error);
    }

    public DownloadTask() {
        mResult = new ArrayList<>();
    }

    public void setCallback(Callback _callback) {
        mCallback = _callback;
    }

    public void releaseCallback() {
        mCallback = null;
    }

    @Override
    protected List<String> doInBackground(String... urls) {
        for (String url : urls) {
            publishProgress(url);
            if (isCancelled()) {
                break;
            }
            if (url.equals("http://sleep")) {
                try {
                    Thread.sleep(10000);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String response = "";
            try {
                URL urlToDownload = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) urlToDownload.openConnection();
                InputStream content = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                e.printStackTrace();
                mError = e;
            }
            mResult.add(response);
        }
        if (isCancelled()) { return null;} else {return mResult;}
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (mCallback != null) {
            mCallback.progress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(List<String> result) {
        super.onPostExecute(result);
        if (mCallback != null) {
            if (mError == null) {
                mCallback.success(mResult);
            } else {
                mCallback.failure(mError);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mCallback != null) {
            mCallback.failure(new Exception("Task cancelled"));
        }
    }

}
