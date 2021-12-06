package edu.coass;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import edu.coass.bean.UserInfo;
import edu.coass.login.LoginActivity;
import edu.coass.okhttp.OkHttpUtils;
import edu.coass.okhttp.RBResponse;
import edu.coass.okhttp.WebResponse;
import okhttp3.Call;

public class NavActivity extends AppCompatActivity implements WebResponse{
    UserInfo userInfo ;
    String userJson ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        userJson=getIntent().getStringExtra("userInfo");
        userInfo=new Gson().fromJson(userJson, UserInfo.class) ;
    }

    public void onClickAssistant(View view){
        Intent intent = new Intent(NavActivity.this, MainActivity.class);
        intent.putExtra("userInfo", userJson);
        startActivity(intent);
    }

    public void onClickMyCourseInfo(View view){
        Intent intent = new Intent(NavActivity.this, MyCourseActivity.class);
        intent.putExtra("userInfo", userJson);
        startActivity(intent);
    }

    public void onClickMyFriends(View view){
        String url = BASE_URL+"getFriends?name="+userInfo.getName();
        System.out.println(url);
        OkHttpUtils.getInstance().doGet(NavActivity.this,url,"1");
    }

    @Override
    public void onSuccessResponse(Call call, RBResponse resultBean, String requestCode) throws IOException {
        System.out.println(resultBean.getResponse());
        if (!resultBean.getResponse().equalsIgnoreCase("failed")){

            Intent intent = new Intent(NavActivity.this, FriendsListActivity.class);
            intent.putExtra("friends", resultBean.getResponse());
            startActivity(intent);
        }

    }

    @Override
    public void onFailResponse(Call call, IOException e, String requestCode) {

    }
}