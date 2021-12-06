package edu.coass.login;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import edu.coass.MainActivity;
import edu.coass.NavActivity;
import edu.coass.R;
import edu.coass.bean.ConversationBean;
import edu.coass.bean.CourseBean;
import edu.coass.bean.UserInfo;
import edu.coass.okhttp.OkHttpUtils;
import edu.coass.okhttp.RBResponse;
import edu.coass.okhttp.WebResponse;
import edu.coass.strategy.conversation.ConversationFlowManager;
import edu.coass.utils.ConversationFlowUtils;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements WebResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void onClickLogin(View view){

        EditText mEditTextUserName = (EditText) findViewById(R.id.userName);
        EditText mEditTextPassword = (EditText) findViewById(R.id.passWord);
        String userName = mEditTextUserName.getText().toString();
        String passWord = mEditTextPassword.getText().toString() ;
        //查询数据库并比对

        //String url = BASE_URL+"login?name="+userName+"&password="+passWord;
        String url = BASE_URL+"getConversation";
        System.out.println("getConversation url : "+url);
        OkHttpUtils.getInstance().doGet(LoginActivity.this,url,"conversation");

        url = BASE_URL+"login?name="+"aaa"+"&password="+"123";
        System.out.println("login url : "+url);
        OkHttpUtils.getInstance().doGet(LoginActivity.this,url,"1");
    }

    public void onClickRegister(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);

        startActivity(intent);
    }

    @Override
    public void onSuccessResponse(Call call, RBResponse resultBean, String requestCode) throws IOException {

        //提示框
        //Toast.makeText(LoginActivity.this,  resultBean.getResponse(), Toast.LENGTH_SHORT).show();
        //判断返回串是否为 Failed
        if (resultBean.getResponse().equalsIgnoreCase("Failed")){
            return ;
        }
        if (requestCode.equalsIgnoreCase("conversation")){
            System.out.println(resultBean.getResponse());
            ConversationFlowManager.conversationList = new Gson().fromJson(resultBean.getResponse(),new TypeToken<List<ConversationBean>>() {}.getType());

            return ;
        }
        //解析返回的字符串
        System.out.println("---------- "+resultBean.getResponse());
        //UserInfo userInfo = new UserInfo();
        //List<CourseBean> courseList = new Gson().fromJson(content, new TypeToken<List<CourseBean>>() {}.getType()) ;
        List<UserInfo> userInfoList = new Gson().fromJson(resultBean.getResponse(),new TypeToken<List<UserInfo>>() {}.getType());
        //userInfo.setName(resultBean.getResponse());
        UserInfo userInfo = userInfoList.get(0) ;
        System.out.println(userInfo.toString());
        Intent intent = new Intent(LoginActivity.this, NavActivity.class);
        intent.putExtra("userInfo", new Gson().toJson(userInfo));
        startActivity(intent);
    }

    @Override
    public void onFailResponse(Call call, IOException e, String requestCode) {

        Log.i("TAG", "请求失败"+e.getMessage());
    }
}