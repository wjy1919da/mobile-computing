package edu.coass.login;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;

import edu.coass.R;
import edu.coass.okhttp.OkHttpUtils;
import edu.coass.okhttp.RBResponse;
import edu.coass.okhttp.WebResponse;
import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity implements WebResponse {

    LinearLayout lineLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        lineLayout = (LinearLayout)findViewById(R.id.layout);
    }

    public void onClickReturn(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void onClickRegister(View view){
//        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//        startActivity(intent);
        EditText mEditTextUserName = (EditText) findViewById(R.id.userName);
        EditText mEditTextPassword = (EditText) findViewById(R.id.passWord);
        EditText mEditTextRePassword = (EditText) findViewById(R.id.repassWord);
        EditText mEditTextContect = (EditText) findViewById(R.id.contect);

        String userName = mEditTextUserName.getText().toString();
        String passWord = mEditTextPassword.getText().toString() ;
        String repassWord = mEditTextRePassword.getText().toString() ;
        String contact = mEditTextContect.getText().toString() ;
        if (!passWord.equals(repassWord)){
            Toast.makeText(RegisterActivity.this,"password is not match repassword", Toast.LENGTH_SHORT).show();
            return ;
        }

        CheckBox cbDataMine = findViewById(R.id.dataMine);
        CheckBox cbNlp = findViewById(R.id.nlp);
        CheckBox cbSpark = findViewById(R.id.spark);
        CheckBox cbIot = findViewById(R.id.iot);
        CheckBox cbTcpip = findViewById(R.id.tcpip);
        CheckBox cbHci = findViewById(R.id.hci);

        StringBuilder inter = new StringBuilder() ;
        if (cbDataMine.isChecked()){
            inter.append("Data Mine,") ;
        }
        if (cbNlp.isChecked()){
            inter.append("NLP,") ;
        }
        if (cbSpark.isChecked()){
            inter.append("Spark,") ;
        }
        if (cbIot.isChecked()){
            inter.append("Iot,") ;
        }
        if (cbTcpip.isChecked()){
            inter.append("tcp/ip,") ;
        }
        if (cbHci.isChecked()){
            inter.append("Hci,") ;
        }
        String interest = inter.toString() ;
        interest = interest.substring(0,interest.length()-1) ;
        //查询数据库并比对
        String url = BASE_URL+"register?name="+userName+"&password="+passWord+"&interest="+interest+"&contact="+contact;
        OkHttpUtils.getInstance().doGet(RegisterActivity.this,url,"1");
    }

    @Override
    public void onSuccessResponse(Call call, RBResponse resultBean, String requestCode) throws IOException {
        Toast.makeText(RegisterActivity.this,"Register "+resultBean.getResponse(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailResponse(Call call, IOException e, String requestCode) {

    }
}