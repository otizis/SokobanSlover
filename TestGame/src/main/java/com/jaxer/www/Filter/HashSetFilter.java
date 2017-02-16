package com.jaxer.www.Filter;

import com.jaxer.www.api.MapFliter;

import java.util.HashSet;

public class HashSetFilter implements MapFliter
{
    private static HashSet<String> mapSet = new HashSet<String>(1024);
    
    @Override
    public boolean isExist(byte[] all)
    {
        StringBuilder builder = new StringBuilder();
        for (byte b : all)
        {
            builder.append(b);
        }
        String a = builder.toString();
        if (mapSet.contains(a))
        {
            return true;
        }
        mapSet.add(a);
        return false;
    }
    
    @Override
    public void clear()
    {
        mapSet.clear();
        
    }
    
}
