package com.jaxer.www.Util;

import com.jaxer.www.enums.Mf8Enum;

public class MapStrUtil
{
    
    public static String convert(String mapStr)
    {
        if (mapStr == null)
        {
            return null;
        }
        StringBuilder builder = new StringBuilder(mapStr.length());
        for (int i = 0; i < mapStr.length(); i++)
        {
            char a = mapStr.charAt(i);
            char b = Mf8Enum.getDect(a);
            if (b == Mf8Enum.error)
            {
                Logger.error(a + ",convert error;");
                System.out.println("");
                builder.append(a);
            }
            builder.append(b);
            
        }
        return builder.toString();
    }
    
    public static void main(String[] args)
    {
        String test = "########------;" + // LR
            "#--#---#------;" + // LR
            "#-*..--#------;" + // LR
            "#-*$-$-#------;" + // LR
            "#-.**$########;" + // LR
            "#---.$---#@-##;" + // LR
            "##.**$-#--*$##;" + // LR
            "#--#-----.$.-#;" + // LR
            "#######-$.*.-#;" + // LR
            "------#-$.$.-#;" + // LR
            "------##$.$*-#;" + // LR
            "------##--#--#;" + // LR
            "------########";
        System.out.println(convert(test));
    }
}
