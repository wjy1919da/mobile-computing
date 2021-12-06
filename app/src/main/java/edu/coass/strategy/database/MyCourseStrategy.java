package edu.coass.strategy.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.coass.bean.CourseBean;
import edu.coass.bean.FriendBean;

public class MyCourseStrategy implements  DatabaseResponseStrategy{
    @Override
    public String process(String content,String witaiValue) {
        List<CourseBean> courseList = new Gson().fromJson(content, new TypeToken<List<CourseBean>>() {}.getType()) ;
        content = "" ;
        for (CourseBean course:courseList){
            System.out.println(course.toString());
            content = content +course.getCourse_name()+" "+course.getStat()+"\n" ;
        }
        return content;
    }
}
