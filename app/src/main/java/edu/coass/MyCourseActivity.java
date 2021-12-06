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

public class MyCourseActivity extends AppCompatActivity implements WebResponse {

    UserInfo userInfo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycourse);
        String userJson=getIntent().getStringExtra("userInfo");
        userInfo=new Gson().fromJson(userJson, UserInfo.class) ;
    }

    public void onClickSubmit(View view){

        EditText mEditTextCourseName = (EditText) findViewById(R.id.courseName);
        EditText mEditTextProfessor = (EditText) findViewById(R.id.professor);
        EditText mEditTextScore = (EditText) findViewById(R.id.score);
        EditText mEditTextState = (EditText) findViewById(R.id.state);

        String courseName = mEditTextCourseName.getText().toString();
        String professor = mEditTextProfessor.getText().toString() ;
        String score = mEditTextScore.getText().toString() ;
        String state = mEditTextState.getText().toString() ;

        //查询数据库并比对
        String url = BASE_URL+"setMyCourse?coursename="+courseName+"&professor="+professor+"&score="+score+"&state="+state+"&name="+userInfo.getName();
        System.out.println(url);
        OkHttpUtils.getInstance().doGet(MyCourseActivity.this,url,"1");
        
    }


    public void onClickMyCourseTable(View view){
        String url = BASE_URL+"getMyCourseInfo?name="+userInfo.getName();
        System.out.println(url);
        OkHttpUtils.getInstance().doGet(MyCourseActivity.this,url,"2");
    }

    @Override
    public void onSuccessResponse(Call call, RBResponse resultBean, String requestCode) throws IOException {
        //Toast.makeText(MyCourseActivity.this,"Insert myCourse "+resultBean.getResponse(), Toast.LENGTH_SHORT).show();
        Integer reqCode = Integer.valueOf(requestCode);
        if ( reqCode == 2 ){
            System.out.println(resultBean.getResponse());
            Intent intent = new Intent(MyCourseActivity.this, MyCourseViewActivity.class);
            intent.putExtra("courseInfo", resultBean.getResponse());
            startActivity(intent);
        }
    }

    @Override
    public void onFailResponse(Call call, IOException e, String requestCode) {

    }
}