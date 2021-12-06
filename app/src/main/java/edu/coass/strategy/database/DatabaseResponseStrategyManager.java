package edu.coass.strategy.database;

import java.util.HashMap;
import java.util.Map;

public class DatabaseResponseStrategyManager {
    private String professor_name ;
    private static Map<String,DatabaseResponseStrategy> drsm = new HashMap<>() ;
    static {
        drsm.put("my course",new MyCourseStrategy()) ;
        drsm.put("FriendsSearch",new FriendSearchStrategy()) ;
        drsm.put("course_info",new CourseInfoStrategy()) ;
        drsm.put("course_recommend",new CourseRecommendStrategy()) ;
        drsm.put("courses_semester",new CourseSemesterStrategy()) ;
        //interest_course_recommend
        drsm.put("interest_course_recommend",new CourseRecommendStrategy()) ;
        drsm.put("my_credits",new MyCreditsStrategy()) ;
        //professor_research
        drsm.put("professor_research",new ProfessorResearchStrategy()) ;
        drsm.put("professor_info",new ProfessorInfoStrategy()) ;
        drsm.put("professor_course",new ProfessorCourseStrategy()) ;
    }
    public String getResult(String key,String content,String witaiValue){
        DatabaseResponseStrategy drs = drsm.get(key) ;
        if (drs != null ){
            return drs.process(content,witaiValue) ;
        }
        return null ;
    }

}
