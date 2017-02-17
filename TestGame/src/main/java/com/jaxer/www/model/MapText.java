package com.jaxer.www.model;

import com.jaxer.www.interfaces.MapLib;

/**
 * Created by jaxer on 2017/2/17.
 */
public class MapText implements MapLib
{
    String mapText;

    public MapText(String mapText)
    {
        this.mapText = mapText;
    }

    public String getMapText()
    {
        return mapText;
    }

    public void setMapText(String mapText)
    {
        this.mapText = mapText;
    }

    @Override
    public String getStandMapStr()
    {
        return mapText;
    }
}
