package edu.coass.strategy.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.coass.bean.CourseBean;
import edu.coass.bean.MyCourseCreditsBean;

public class MyCreditsStrategy implements  DatabaseResponseStrategy{
    @Override
    public String process(String content,String witaiValue) {
        List<MyCourseCreditsBean> courseList = new Gson().fromJson(content, new TypeToken<List<MyCourseCreditsBean>>() {}.getType()) ;
        content = "Here are your credits:\n" ;
        for (MyCourseCreditsBean course:courseList){
            System.out.println(course.toString());
            content = content +" "+course.getStat();
            content = content +" "+course.getCredits()+"\n" ;
        }
        content = content.substring(0,content.length()-1);
        return content;
    }
}
