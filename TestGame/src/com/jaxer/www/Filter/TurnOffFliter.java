package com.jaxer.www.Filter;

import com.jaxer.www.api.MapFliter;

public class TurnOffFliter implements MapFliter
{
    
    @Override
    public boolean isExist(byte[] t)
    {
        return false;
    }
    
    @Override
    public void clear()
    {
    
    }
    
}
