package com.jaxer.www;

public class Logger
{
    static boolean isdebug = false;
    
    static boolean isInfo = true;
    
    public static void debug(String str)
    {
        if (isdebug)
        {
            System.out.println(str);
        }
    }
    
    public static void info(String str)
    {
        if (isInfo)
        {
            System.out.println(str);
        }
    }
    
    public static boolean isDebugEnable()
    {
        return isdebug;
    }
}
