package edu.coass.strategy.conversation;

import static edu.coass.utils.ConstantUtli.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Locale;

import edu.coass.bean.ConversationBean;
import edu.coass.bean.CourseInfoBean;
import edu.coass.bean.CourseRecommendBean;
import edu.coass.bean.SuggestionBean;
import edu.coass.utils.ConversationFlowUtils;

public class ConversationFlowManager {
    private static int curQuestionId = 0 ;

    public static List<ConversationBean> conversationList;

    public static  String url = null ;

    public static SuggestionBean suggestion  ;

    public static String getQuestion(){

        if (conversationList == null){
            return null ;
        }
        System.out.println(" Conversation size "+conversationList.size());
        for (ConversationBean tcb:conversationList){
            System.out.println(tcb.toString());
        }
        // 根据当前curQuestionId获取conversation class
        ConversationBean cb = conversationList.get(curQuestionId) ;
        System.out.println(" -----------getQuestion "+curQuestionId+"    "+cb.toString() );
        //
        if (cb.getConversation() == null){
            curQuestionId++ ;
            cb = conversationList.get(curQuestionId) ;
        }
        String[] strArr = cb.getConversation().split("&") ;
        String content = "" ;
        for (String item:strArr){
            content = content+item+"\n" ;
        }
        content = content.substring(0,content.length()-1) ;
        System.out.println(" Conversation "+content);
        return content;
    }

    public static String getAnswerUrl(String value){
        List<CourseInfoBean> courseInfoList = new Gson().fromJson(value, new TypeToken<List<CourseInfoBean>>() {}.getType()) ;

        ConversationBean cb = conversationList.get(curQuestionId) ;;
        String content =cb.getReplyPrefix()+":\n" ;
        for (CourseInfoBean course:courseInfoList){
            System.out.println(course.toString());
            content = content+"course name : " +course.getCourse_name()+"\n";
            content = content+"course no   : " +course.getCourse_no()+"\n";
            content = content+"professor   : " +course.getProfessor()+"\n";
            content = content+"requirement : " +course.getRequirement()+"\n";
            content = content+"\n" ;
        }
        content = content.substring(0,content.length()-1) ;
        curQuestionId ++ ;
        return content;
    }

    public static int  getAnswer(String value) {
        System.out.println("------getAnswer---------"+value);
        System.out.println("------getAnswer---------"+conversationList.get(curQuestionId).toString());
        suggestion = null;

        if (conversationList == null) {
            return -1;
        }
        ConversationBean cb = conversationList.get(curQuestionId);
        String answerNo = cb.getAnswer_no().toLowerCase() ;

        if (answerNo.contains(value.toLowerCase())) {
            curQuestionId = cb.getJumpId();
            System.out.println("-----curQuestionId  "+curQuestionId);
            if (cb.getAction() == 1){
                curQuestionId ++ ;
            }
            if (cb.getAction() == 0){
                return 3;
            }
            return 2;
        }

        String param = cb.getParam();
        String last = param.substring(param.length() - 1, param.length());
        String valueLower = value.toLowerCase();
        String answordsLower = cb.getAnswerWords().toLowerCase();
        if (answordsLower.contains(valueLower)) {
            if (cb.getAction() == 1) {
                if (last.equalsIgnoreCase("?")) {
                    url = BASE_URL + cb.getParam() + "param=" + value;
                } else {
                    url = BASE_URL + cb.getParam();
                }
                suggestion = new SuggestionBean();
                suggestion.setAnswer(url);
                suggestion.setAction(1);
            }
            if (cb.getAction() == 0) {
                curQuestionId = cb.getJumpId();
                return 2 ;
            }
            if (cb.getAction() == 2) {
                curQuestionId = cb.getJumpId();
                return 2 ;
            }
            return 1 ;
        } else {
            return 0;
        }
    }
}
