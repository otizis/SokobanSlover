package com.jaxer.www.Filter;

import com.jaxer.www.Util.BigFastMap;
import com.jaxer.www.api.MapFliter;
import com.jaxer.www.myexception.MyException;

public class FastMapFilter implements MapFliter
{
    private BigFastMap map = new BigFastMap(3, 5);
    
    @Override
    public boolean isExist(byte[] str)
    {
        if (map.contain(str))
        {
            return true;
        }
        map.add(str);
        return false;
        
    }
    
    @Override
    public void clear()
    {
        throw new MyException("没有实现");
    }
    
}
