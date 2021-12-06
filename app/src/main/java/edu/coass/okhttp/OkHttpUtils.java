package edu.coass.okhttp;



import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    private static OkHttpUtils okHttpUtils = null;
    private static Handler mainHandler;

    public static synchronized OkHttpUtils getInstance() {
        if (okHttpUtils == null) {
            okHttpUtils = new OkHttpUtils();
            //更新UI线程
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return okHttpUtils;
    }
    public static void post(final WebResponse mResponse, String url, String json, String requestCode) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                System.out.println("On Failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + response.body().string());
                //final RBResponse result = getJson(response.body().string(), jsonBean);
                String res = response.body().string() ;  //只能取一次
                RBResponse rbResponse = new RBResponse();
                rbResponse.setResponse( res);
                //UI线程 回调出去可以直接更新UI
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mResponse.onSuccessResponse(call, rbResponse, requestCode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    public  void doGet(final WebResponse mResponse, String url, String requestCode){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mResponse.onFailResponse(call, e, requestCode);
                System.out.println("On Failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + response.body().string());
                //final RBResponse result = getJson(response.body().string(), jsonBean);
                String res = response.body().string() ;  //只能取一次
                RBResponse rbResponse = new RBResponse();
                rbResponse.setResponse( res);
                //UI线程 回调出去可以直接更新UI
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mResponse.onSuccessResponse(call, rbResponse, requestCode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
