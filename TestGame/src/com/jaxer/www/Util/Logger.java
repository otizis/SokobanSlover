package com.jaxer.www.Util;

public class Logger
{
    static boolean isdebug = false;
    
    static boolean isInfo = true;
    
    static boolean isoutput = true;
    
    public static void debug(String str)
    {
        if (!isoutput)
        {
            return;
        }
        if (isdebug)
        {
            System.out.println(str);
        }
    }
    
    public static void info(String str)
    {
        if (!isoutput)
        {
            return;
        }
        if (isInfo)
        {
            System.out.println(str);
        }
    }
    
    public static boolean isDebugEnable()
    {
        return isdebug;
    }
    
    public static void turnOff()
    {
        isoutput = false;
    }
    
    public static void turnOn()
    {
        isoutput = true;
    }
}
