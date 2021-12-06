package edu.coass.strategy.witai;

import java.util.HashMap;
import java.util.Map;

import edu.coass.bean.UserInfo;
import edu.coass.bean.witaiRes.WitaiRes;

public class WitaiResponseStrategyManager {

    private static Map<String,WitaiResponseStrategy> wrsm= new HashMap<>() ;
    static{
        wrsm.put("my course",new MyCourseStrategy()) ;
        wrsm.put("course_info",new CourseInfoStrategy()) ;
        wrsm.put("courses_semester",new CourseInfoStrategy()) ;
        wrsm.put("course_recommend",new CourseRecommendStrategy()) ;
        wrsm.put("interest_course_recommend",new CourseRecommendByInterestStrategy()) ;
        //CourseSemesterStrategy
        wrsm.put("my_credits",new MyCreditsStrategy()) ;
        //professor_research
        wrsm.put("professor_research",new ProfessorResearchStrategy()) ;

        wrsm.put("professor_info",new ProfessorInfoStrategy()) ;
        wrsm.put("professor_course",new ProfessorCourseStrategy()) ;

    }
    public String getResult(WitaiRes witaiRes, UserInfo userInfo){
        WitaiResponseStrategy wrs = wrsm.get(witaiRes.getType()) ;
        if (wrs != null){
            return wrs.process(witaiRes.getValue(),userInfo) ;
        }
        else {
            return null ;
        }
    }
}
