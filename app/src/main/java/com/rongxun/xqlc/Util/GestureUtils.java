package com.rongxun.xqlc.Util;

import java.util.HashMap;

/**
 * Created by jcb on 2017/8/16.
 */

public class GestureUtils {
    private static HashMap map;
    public static HashMap getMap(){
        if(map==null){
            map=new HashMap();
        }
        return map;
    }


}
