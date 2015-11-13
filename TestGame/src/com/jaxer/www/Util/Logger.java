package com.jaxer.www.Util;

public class Logger
{
    public static boolean isdebug = false;
    
    public static boolean isInfo = true;
    
    public static boolean isoutput = true;
    
    // 是否输出死点计算过程
    public static boolean isprintDeadInfo = true;
    
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
    
    public static void error(String string)
    {
        System.err.println(string);
        
    }
}
