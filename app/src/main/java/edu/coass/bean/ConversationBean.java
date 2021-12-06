package edu.coass.bean;

public class ConversationBean {
    int questionId ;
    String conversation ;
    String stateCode ;
    String replyPrefix ;
    String answerWords ;
    int    jumpId ;
    String param ;
    String answer_no ;
    int    action ;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }



    public String getAnswerWords() {
        return answerWords;
    }

    public void setAnswerWords(String answerWords) {
        this.answerWords = answerWords;
    }

    public int getJumpId() {
        return jumpId;
    }

    public void setJumpId(int jumpId) {
        this.jumpId = jumpId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getAnswer_no() {
        return answer_no;
    }

    public void setAnswer_no(String answer_no) {
        this.answer_no = answer_no;
    }

    public String getReplyPrefix() {
        return replyPrefix;
    }

    public void setReplyPrefix(String replyPrefix) {
        this.replyPrefix = replyPrefix;
    }

    @Override
    public String toString() {
        return "ConversationBean{" +
                "questionId=" + questionId +
                ", conversation='" + conversation + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", replyPrefix='" + replyPrefix + '\'' +
                ", answerWords='" + answerWords + '\'' +
                ", jumpId=" + jumpId +
                ", param='" + param + '\'' +
                ", answer_no='" + answer_no + '\'' +
                ", action=" + action +
                '}';
    }
}
