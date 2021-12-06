package edu.coass.strategy.witai;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import edu.coass.bean.UserInfo;

public class MyCourseStrategy implements WitaiResponseStrategy{
    @Override
    public String process(String responseValue, UserInfo userInfo) {
        System.out.println("MyCourse");
        return BASE_URL+"getMyCourse?name="+userInfo.getName();
    }
}
