package edu.coass.strategy.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.coass.bean.CourseBean;
import edu.coass.bean.FriendBean;

public class FriendSearchStrategy implements  DatabaseResponseStrategy{
    @Override
    public String process(String content,String witaiValue) {
        List<FriendBean> friendList = new Gson().fromJson(content, new TypeToken<List<FriendBean>>() {}.getType()) ;
        int count = friendList.size();
        content = "Now, there are "+count+" friends who have the same interest as you,they are " ;
        for (FriendBean course:friendList){
            System.out.println(course.toString());
            content = content +course.getName()+",";
        }
        content = content.substring(0,content.length()-1) ;
        content = content+",Whether or not you want to add them as friends?";
        return content;
    }
}
