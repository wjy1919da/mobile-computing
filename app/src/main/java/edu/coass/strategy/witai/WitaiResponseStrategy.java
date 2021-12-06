package edu.coass.strategy.witai;

import edu.coass.bean.UserInfo;

public interface WitaiResponseStrategy {
    String process(String responseValue, UserInfo userInfo) ;
}
