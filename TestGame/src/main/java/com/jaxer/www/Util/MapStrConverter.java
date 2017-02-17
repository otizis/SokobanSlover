package com.jaxer.www.Util;

import com.jaxer.www.enums.MapType;
import com.jaxer.www.enums.Mf8Enum;
import com.jaxer.www.interfaces.MapLib;
import com.jaxer.www.model.MapText;
import com.jaxer.www.myexception.MyException;

public class MapStrConverter
{
    public static MapLib convert(CharSequence mapStr, MapType mapType)
    {
        switch (mapType)
        {
            case MF8_$:
                return convert(mapStr.toString());
            case Stand_BPS:
                return new MapText(mapStr.toString());
            default:
                throw new MyException("unknow mapType");
        }
    }

    private static MapLib convert(String mapStr)
    {
        if (mapStr == null)
        {
            return null;
        }
        StringBuilder builder = new StringBuilder(mapStr.length());
        for (int i = 0; i < mapStr.length(); i++)
        {
            char a = mapStr.charAt(i);
            char b = Mf8Enum.getDect(a);
            if (b == Mf8Enum.error)
            {
                Logger.error(a + ",convert error;");
                builder.append(a);
            }
            builder.append(b);

        }
        return new MapText(builder.toString());
    }

}
