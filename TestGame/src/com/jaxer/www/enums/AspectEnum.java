package com.jaxer.www.enums;

public enum AspectEnum
{
    up("¡ü", ""), right("¡ú", ""), down("¡ý", ""), left("¡û", "");
    
    String desc;
    
    AspectEnum(String a, String b)
    {
        this.desc = a;
    }
    
    /**
     * @return ·µ»Ø desc
     */
    public String getDesc()
    {
        return desc;
    }
    
}
