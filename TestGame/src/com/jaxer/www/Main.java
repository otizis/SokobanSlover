package com.jaxer.www;

import java.util.Arrays;

import com.jaxer.www.Util.MapStrConverter;
import com.jaxer.www.manager.MapLib;
import com.jaxer.www.model.SokoMap;

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
//        sokoMap.run("map_");14524
        long b = System.currentTimeMillis();
        int i = 0;while (i++ < 10000000)
        {
            sokoMap.getPlayerCanGoCells2(sokoMap.getBoxList(), sokoMap.getMan());
        }
        System.out.println(System.currentTimeMillis() -b);
        System.out.println(Arrays.toString(sokoMap.getPlayerCanGoCells2(sokoMap.getBoxList(), sokoMap.getMan()).getMapBytes()));
    }
    
}
