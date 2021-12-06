package edu.coass;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import edu.coass.bean.FriendBean;
import edu.coass.bean.MyCourseInfoBean;
import edu.coass.bean.UserInfo;
import edu.coass.okhttp.OkHttpUtils;
import edu.coass.okhttp.RBResponse;
import edu.coass.okhttp.WebResponse;
import edu.coass.table.SimpleTableAdapter;
import edu.coass.table.TableAdapter;
import edu.coass.table.TableList;
import okhttp3.Call;

public class MyCourseViewActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String courseInfo=getIntent().getStringExtra("courseInfo");
        Gson gson=new Gson();
        List<MyCourseInfoBean> list = gson.fromJson(courseInfo, new TypeToken<List<MyCourseInfoBean>>() {}.getType());

        String[][] values = new String[list.size()+1][4]  ;
        values[0][0] = "course_name" ;
        values[0][1] = "professor" ;
        values[0][2] = "credits" ;
        values[0][3] = "stat" ;
        int num =1 ;
        for (MyCourseInfoBean course:list){
            values[num][0] = course.getCourse_name();
            values[num][1] = course.getProfessor() ;
            values[num][2] = course.getCredits() ;
            values[num][3] = course.getStat() ;
            num++ ;
        }

        TableAdapter tableAdapter = new SimpleTableAdapter(this, values);

        tableAdapter.setColumnSpacing(10);
        tableAdapter.setRowSpacing(15);

        TableList table = new TableList(this);

        setContentView(table);

        table.setColumnWidths(new float[]{0.35f, 0.30f, 0.15f,0.2f});
        table.setAdapter(tableAdapter);
    }

}