package com.jaxer.www.Util;

import java.util.HashMap;
import java.util.Set;

public class TimeStamps
{
    public static HashMap<String, Long> timeMap = new HashMap<String, Long>();
    
    public static void addTime(String mothed, long time)
    {
        Long timeBefore = timeMap.get(mothed);
        if (timeBefore == null)
        {
            timeMap.put(mothed, time);
        }
        else
        {
            timeMap.put(mothed, timeBefore + time);
        }
    }
    
    public static void printMap()
    {
        Set<String> keySet = timeMap.keySet();
        for (String string : keySet)
        {
            System.out.println(string + ":" + timeMap.get(string));
        }
    }
}
