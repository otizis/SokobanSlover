package com.jaxer.www.Filter;

import java.util.HashSet;

import com.jaxer.www.api.MapFliter;

public class HashSetFilter implements MapFliter<String>
{
    private static HashSet<String> mapSet = new HashSet<String>(1024);
    
    @Override
    public boolean isExist(String str)
    {
        
        if (mapSet.contains(str))
        {
            return false;
        }
        mapSet.add(str);
        return true;
    }
    
    @Override
    public void clear()
    {
        mapSet.clear();
        
    }
    
}
