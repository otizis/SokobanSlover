package com.jaxer.www;

import com.jaxer.www.Util.MapStrConverter;
import com.jaxer.www.enums.MapType;
import com.jaxer.www.interfaces.MapLib;
import com.jaxer.www.manager.BPSMMapLib;
import com.jaxer.www.model.SokoMap;

/**
 * app main
 */
public class Main
{

    public static void main(String[] args)
    {
        MapLib mapText = MapStrConverter.convert(BPSMMapLib.test3, MapType.Stand_BPS);

        new SokoMap(mapText).run("map_");
    }

}
