package com.jaxer.www;

import java.util.Arrays;

import com.jaxer.www.Util.MapStrConverter;
import com.jaxer.www.manager.MapLib;
import com.jaxer.www.model.SokoMap;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
        sokoMap.run("map_");
    }
    
}
