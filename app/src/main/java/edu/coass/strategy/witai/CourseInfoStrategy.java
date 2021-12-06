package edu.coass.strategy.witai;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import edu.coass.bean.UserInfo;

public class CourseInfoStrategy implements WitaiResponseStrategy{
    @Override
    public String process(String responseValue, UserInfo userInfo) {
        System.out.println("professor");
        return BASE_URL + "getCourseInfo?courseName=" + responseValue;
    }
}
