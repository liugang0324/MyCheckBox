package test.bwie.com.myokhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();  //Get的同步和异步请求
        try {
            post("http://admin.wap.china.com/user/NavigateTypeAction.do?processID=getNavigateNews");  //Post的同步请求
        } catch (IOException e) {
            e.printStackTrace();
        }
        postEnqueue("http://admin.wap.china.com/user/NavigateTypeAction.do?processID=getNavigateNews");//Post的异步请求
    }
    private void post(String url) throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("code", "news")
                .add("pageSize", "20")
                .add("parentid", "0")
                .add("type", "1")


                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.d("zzz","response====="+response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
   private void postEnqueue(String uri){
       final OkHttpClient okHttpClient = new OkHttpClient();
       RequestBody formBody = new FormBody.Builder()
               .add("page", "1")
               .add("code", "news")
               .add("pageSize", "20")
               .add("parentid", "0")
               .add("type", "1")


               .build();
       final Request request = new Request.Builder()
               .url(uri)
               .post(formBody)
               .build();

       new Thread(new Runnable() {
           @Override
           public void run() {
               Call call = okHttpClient.newCall(request);
               call.enqueue(new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                       Log.d("zxc","response====="+e.getMessage());
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       Log.d("zzz","response====="+response.body().string());

                       response.body().close();
                   }
               });
           }
       }).start();
   }

    private void getData() {
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        final Request build = new Request.Builder().url("http://result.eolinker.com/gfGTLlHc049c6b450500b16971f52bd8e83f6b2fed305ab?uri=news").header("User-Agent", "OkHttp Example").build();
     //Get的同步请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(build).execute();
                    Log.d("zzz","response====="+response.body().string());

                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //Get的异步请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call call = okHttpClient.newCall(build);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("zxc","response====="+e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("zzz","response====="+response.body().string());

                        response.body().close();
                    }
                });
            }
        }).start();
    }


}
