package edu.coass.strategy.witai;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import edu.coass.bean.UserInfo;

public class CourseRecommendByInterestStrategy implements WitaiResponseStrategy{
    @Override
    public String process(String responseValue, UserInfo userInfo) {
        System.out.println("CourseRecommendStrategy");
        return BASE_URL + "getCourseRecommend?interests=" + responseValue;
    }
}
