package com.jaxer.www;

import java.util.Arrays;

import com.jaxer.www.Util.MapStrConverter;
import com.jaxer.www.manager.MapLib;
import com.jaxer.www.model.SokoMap;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 主体类
 * 
 * @version [版本号, 2015年9月21日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Main
{
    
    public static void main(String[] args)
    {
        
        String mapStr = MapStrConverter.convert(MapLib.PerMonth11_06);
        SokoMap sokoMap = new SokoMap(mapStr);
        sokoMap.run("map_");
    }
    
}
