package edu.coass.bean.witaiRes;

public class WitaiRes {
    String type ;
    String value ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "WitaiRes{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
