package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.HashSet;

import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class Util
{
    
    // private static MapFliter fliter = new HashSetFilter();
    // private static MapFliter fliter = new TurnOffFliter();
    
    public static ArrayList<Zuobiao> cloneBoxList(ArrayList<Zuobiao> boxList)
    {
        ArrayList<Zuobiao> cloneBoxList =
            new ArrayList<Zuobiao>(boxList.size());
            
        for (Zuobiao box : boxList)
        {
            cloneBoxList.add(box.myClone());
        }
        return cloneBoxList;
    }
    
    public static HashSet<Zuobiao> coverter(ArrayList<Zuobiao> boxList)
    {
        HashSet<Zuobiao> boxsSet = new HashSet<Zuobiao>();
        for (Zuobiao box : boxList)
        {
            boxsSet.add(box.myClone());
        }
        return boxsSet;
    }
    
    
    public static String drawMap(StringBuilder mapStr,int max_x)
    {
        StringBuilder result = new StringBuilder();
        int width = max_x + 1;
        for (int i = 0; i < mapStr.length(); i++)
        {
            if (i % width == 0)
            {
                result.append("\n");
            }
            result.append(mapStr.charAt(i));
            result.append(" ");
        }
        result.append("\n");
        return result.toString();
    }
    
    
    
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static StringBuilder replaceZuobiao(StringBuilder a, Zuobiao zb,
        String str,int sokomap_Max_X)
    {
        int indxe = zb.getY() * (sokomap_Max_X + 1) + zb.getX();
        
        a.replace(indxe, indxe + 1, str);
        return a;
    }
    
    /**
     * ���е����Ӷ�վ��Ŀ�����
     * 
     * @param boxList
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isAllBoxGoal(ArrayList<Zuobiao> boxList,SokoMap sokoMap)
    {
        for (Zuobiao zuobiao : boxList)
        {
            if (!sokoMap.getCell(zuobiao).check(CellType.gole))
            {
                
                return false;
            }
        }
        return true;
    }
    
    /**
     * ���е�Ŀ��㶼�����Ӹ���
     * 
     * @param boxList
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isAllGoalCover(ArrayList<Zuobiao> boxList,SokoMap sokoMap)
    {
        for (Zuobiao zb : sokoMap.getGoleList())
        {
            if (!boxList.contains(zb))
            {
                return false;
            }
        }
        return true;
    }
    
}
