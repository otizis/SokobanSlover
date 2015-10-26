package com.jaxer.www.enums;

public enum AspectEnum
{
    up("��", ""), right("��", ""), down("��", ""), left("��", "");
    
    String desc;
    
    AspectEnum(String a, String b)
    {
        this.desc = a;
    }
    
    /**
     * @return ���� desc
     */
    public String getDesc()
    {
        return desc;
    }
    
}
