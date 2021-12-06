package edu.coass.strategy.witai;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import edu.coass.bean.UserInfo;

public class ProfessorCourseStrategy implements WitaiResponseStrategy{
    @Override
    public String process(String responseValue, UserInfo userInfo) {
        System.out.println("professor");
        return BASE_URL + "getProfessorCourse?name=" + responseValue;
    }
}
