package com.jaxer.www.enums;

public enum AspectEnum
{
    up("^", ""), right(">", ""), down("v", ""), left("<", "");
    
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
    
    static final AspectEnum[] allEnum = {AspectEnum.up, AspectEnum.left, AspectEnum.down, AspectEnum.right};
    
    public static AspectEnum[] getAllEnum(){
        
        return  allEnum;
    }
}
