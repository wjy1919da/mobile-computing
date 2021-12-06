package edu.coass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.util.List;

import edu.coass.bean.FriendBean;
import edu.coass.table.SimpleTableAdapter;
import edu.coass.table.TableAdapter;
import edu.coass.table.TableList;
import okhttp3.Call;

public class FriendsListActivity extends AppCompatActivity {

    FriendBean friendInfo ;
    private ListView mListView;
    private  String[] name ;
    private  String[] email ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_friendslist);

        String newsJson=getIntent().getStringExtra("friends");
        Gson gson=new Gson();
        List<FriendBean> list = gson.fromJson(newsJson, new TypeToken<List<FriendBean>>() {}.getType());
        System.out.println(newsJson);
        name = new String[list.size()] ;
        email      = new String[list.size()] ;
                                           //row         col
        String[][] tableList = new String[list.size()+1][2];
//        new String[] {"Column 1", "Column 2", "Column 3"},
//        new String[] {"Inputs", "Inputs", "inputs"}
        int count = 1 ;
        tableList[0][0] = "  name" ;
        tableList[0][1] = "email" ;
        for(FriendBean item:list){
            tableList[count][0] = "  "+item.getName();
            tableList[count][1] = item.getEmail();
            count ++ ;
        }

        TableAdapter tableAdapter = new SimpleTableAdapter(this, tableList);


        tableAdapter.setColumnSpacing(5);
        tableAdapter.setRowSpacing(10);

        TableList table = new TableList(this);


        setContentView(table);

        table.setColumnWidths(new float[]{0.5f, 0.5f});
        table.setAdapter(tableAdapter);
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return name [position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//组装数据
            View view=View.inflate(FriendsListActivity.this,R.layout.list_item,null);//在list_item中有两个id,现在要把他们拿过来
            TextView mTextViewnName=(TextView)view.findViewById(R.id.name);
            TextView mTextViewEmail=(TextView)view.findViewById(R.id.email);
            //组件一拿到，开始组装
            mTextViewnName.setText(name[position]);
            mTextViewEmail.setText(email[position]);
            //组装玩开始返回
            return view;
        }
    }

}