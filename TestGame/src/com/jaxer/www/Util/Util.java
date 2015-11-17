package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.HashSet;

import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class Util
{
    
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
    
    public static String drawMap(StringBuilder mapStr, int map_max_x)
    {
        StringBuilder result = new StringBuilder();
        int width = map_max_x + 1;
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
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static StringBuilder replaceZuobiao(StringBuilder a, Zuobiao zb,
        String str, int sokomap_Max_X)
    {
        int indxe = zb.getY() * (sokomap_Max_X + 1) + zb.getX();
        
        a.replace(indxe, indxe + 1, str);
        return a;
    }
    
    /**
     * 有几个箱子不在目标点上
     * 
     * @param boxList
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int boxsNumNotGole(ArrayList<Zuobiao> boxList,
        SokoMap sokoMap)
    {
        int rs = boxList.size();
        for (Zuobiao zuobiao : boxList)
        {
            if (sokoMap.getCell(zuobiao).check(CellType.gole))
            {
                rs--;
            }
        }
        return rs;
    }
    
    /**
     * 所有的目标点都有箱子覆盖
     * 
     * @param boxList
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAllGoalCover(ArrayList<Zuobiao> boxList,
        SokoMap sokoMap)
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
    
    /**
     * 存在死围返回true
     * 
     * @param boxList
     * @param sokoMap
     * @return
     * @see [类、类#方法、类#成员]
     */

    public static boolean checkRound(ArrayList<Zuobiao> boxList, SokoMap sokoMap)
    {
        ArrayList<Zuobiao[]> roundDeadPoint = sokoMap.getRoundDeadPoint();
        if (roundDeadPoint == null)
        {
            return false;
        }
        for (Zuobiao[] siwei : roundDeadPoint)
        {
            
            if (isMatch(boxList, siwei))
            {
                return true;
            }
            
        }
        
        return false;
        
    }
    
    /**
     * boxList是否满足死围
     * 
     * 
     * @param boxList
     * @param siwei [参数说明]
     *            
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static boolean isMatch(ArrayList<Zuobiao> boxList, Zuobiao[] siwei)
    {
        for (Zuobiao zuobiao : siwei)
        {
            if (!boxList.contains(zuobiao))
            {
                return false;
            }
        }
        return true;

    }
    
}
