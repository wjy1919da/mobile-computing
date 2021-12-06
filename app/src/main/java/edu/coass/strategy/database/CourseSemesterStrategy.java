package edu.coass.strategy.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.coass.bean.CourseRecommendBean;

public class CourseSemesterStrategy implements  DatabaseResponseStrategy{
    @Override
    public String process(String content,String witaiValue) {
        List<CourseRecommendBean> courseInfoList = new Gson().fromJson(content, new TypeToken<List<CourseRecommendBean>>() {}.getType()) ;

        //content = "Now, there are "+count+" friends who have the same interest as you,they are " ;
        content ="";
        for (CourseRecommendBean course:courseInfoList){
            System.out.println(course.toString());
            content = content+"course name : " +course.getCourse_name()+"\n";
            content = content+"course no   : " +course.getCourse_no()+"\n";
            content = content+"professor   : " +course.getProfessor()+"\n";
            content = content+"requirement : " +course.getRequirement()+"\n";
            content = content+"\n" ;
        }
        return content;
    }
}
