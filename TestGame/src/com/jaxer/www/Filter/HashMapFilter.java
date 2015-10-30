package com.jaxer.www.Filter;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaxer.www.api.MapFliter;

public class HashMapFilter implements MapFliter<String>
{
    
    /**
     * 地图特征值，如果出现过就会记录。 如果重复，表示出现过其他走出这个特征走法，本次为非最优的走法
     */
    private static HashMap<String, ArrayList<String>> mapMap =
        new HashMap<String, ArrayList<String>>(1024);
        
    @Override
    public boolean isExist(String str)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void clear()
    {
        mapMap.clear();
        
    }
    
}
