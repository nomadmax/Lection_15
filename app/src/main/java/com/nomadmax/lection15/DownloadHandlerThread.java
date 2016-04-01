package com.nomadmax.lection15;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Max on 28.03.16.
 */
public class DownloadHandlerThread extends HandlerThread {

    public static final String TAG = "blabla";
    public static final int MESSAGE_DOWNLOAD = 555;
    private Handler mDownloadHandler;
    private Callback mCallback;

    public interface Callback{
        void success(String _result);
        void failure(String _error);
    }


    public DownloadHandlerThread(String name) {
        super(name);
    }

    public Handler getDownloadHandler() {
        return mDownloadHandler;
    }

    @Override
    protected void onLooperPrepared() {
            mDownloadHandler = new DownloadHandler();
    }


    public void setCallback(Callback _callback) {
        mCallback = _callback;
    }
    public void releaseCallback(){
        mCallback = null;
    }

    private String downloadUrl(URL _url) {

        if (_url.toString().equals("http://sleep")) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String response = "";
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) _url.openConnection();
            InputStream content = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public class DownloadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (mCallback == null) {
                return;
            }
            switch (msg.what){
                case MESSAGE_DOWNLOAD:
                    URL url = (URL) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + url.toString());
                    String result = downloadUrl(url);
                    if (result != null) {
                        mCallback.success(result);
                    } else {
                        mCallback.failure(result);
                    }
                    break;
                default:
            }
        }
    }
}
