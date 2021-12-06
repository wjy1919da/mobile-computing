package edu.coass.strategy.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.coass.bean.ProfessorInfoBean;

public class ProfessorInfoStrategy implements  DatabaseResponseStrategy{
    @Override
    public String process(String content,String witaiValue) {
        List<ProfessorInfoBean> contactList = new Gson().fromJson(content, new TypeToken<List<ProfessorInfoBean>>() {}.getType()) ;

        //content = "Now, there are "+count+" friends who have the same interest as you,they are " ;
        content ="Now there are infomation of professor "+witaiValue+": \n";
        for (ProfessorInfoBean contact:contactList){
            System.out.println(contact.toString());
            content = content+"position : " +contact.getPosition()+"\n";
            content = content+"department   : " +contact.getDepartment()+"\n";
            content = content+"email : " +contact.getEmail()+"\n";
            content = content+"phone   : " +contact.getPhone()+"\n";
            content = content+"homePage : " +contact.getHomepage()+"\n";
        }
        return content;
    }
}
