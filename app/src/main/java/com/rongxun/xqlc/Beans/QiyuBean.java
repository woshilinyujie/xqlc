package com.rongxun.xqlc.Beans;

/**
 * Created by windows 10 on 2018/5/10.
 */

public class QiyuBean extends QiyuUserBean {
    private int index;
    private String label;

    public void setIndex(int index){
        this.index=index;
    }
    public int getIndex(){
        return index;
    }

    public void setLabel(String label){
        this.label=label;
    }
    public String getLabel(){
        return label;
    }
}
