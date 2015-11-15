package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class SokoMapFull extends SokoMap
{
    
    public SokoMapFull(SokoMap soure, ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        super(soure, boxList, man);
    }
    
    @Override
    public void run(String gifName)
    {
        LinkedList<Solution> success = new LinkedList<Solution>();
        // 生成所有的可能
    
    }
}
