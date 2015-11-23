package com.jaxer.www;

import java.util.Arrays;

import com.jaxer.www.Util.MapStrConverter;
import com.jaxer.www.manager.MapLib;
import com.jaxer.www.model.SokoMap;

/**
 * ������
 * 
 * @version [�汾��, 2015��9��21��]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
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
