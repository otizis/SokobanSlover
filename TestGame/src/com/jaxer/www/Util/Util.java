package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.jaxer.www.Filter.BloomFliter;
import com.jaxer.www.api.MapFliter;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class Util
{
    
    private static MapFliter<byte[]> fliter = new BloomFliter();
    // private static MapFliter fliter = new HashSetFilter();
    
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
    
    /**
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] descZuobiaoList(ArrayList<Zuobiao> coll)
    {
        byte[] desc = new byte[(SokoMap.max_x + 1) * (SokoMap.max_y + 1)];
        for (Zuobiao b : coll)
        {
            desc[b.getX()+(b.getY()*SokoMap.max_x)] = 1;
        }
        return desc;
        // Zuobiao[] array = coll.toArray(new Zuobiao[coll.size()]);
        // Arrays.sort(array);
        // return Arrays.toString(array);
    }
    
    public static String drawMap(StringBuilder mapStr)
    {
        StringBuilder result = new StringBuilder();
        int width = SokoMap.max_x + 1;
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
    public static StringBuilder mapStr()
    {
        Cell[][] map = SokoMap.thisStepMap;
        
        StringBuilder buid = new StringBuilder();
        for (int j = 0; j < map[0].length; j++)
        {
            for (int i = 0; i < map.length; i++)
            {
                buid.append(map[i][j].draw());
            }
        }
        return buid;
    }
    
//    /**
//     * 是否成功放入set，表示不存在重复
//     * 
//     * @param mapStr
//     * @return
//     * @see [类、类#方法、类#成员]
//     */
//    public static boolean putIfAb(String boxsStr, String manStr, String keys)
//    {
//        return fliter.isExist(boxsStr.concat(manStr));
//        
//    }
    
    /**
     * 是否成功放入set，表示不存在重复
     * 
     * @param mapStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean putIfAb(byte[] boxsStr, byte[] manStr, String keys)
    {
        byte[] all = Arrays.copyOf(boxsStr, boxsStr.length+manStr.length);
        System.arraycopy(manStr, 0, all, boxsStr.length, manStr.length);
        
        return fliter.isExist(all);
        
    }
    
    /**
     * 清空记录
     * 
     * @param mapStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static void resetMapSet()
    {
        fliter.clear();
        
    }
    
    /**
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static StringBuilder replaceZuobiao(StringBuilder a, Zuobiao zb,
        String str)
    {
        int indxe = zb.getY() * (SokoMap.max_x + 1) + zb.getX();
        
        a.replace(indxe, indxe + 1, str);
        return a;
    }
    
    /**
     * 所有的箱子都站在目标点上
     * 
     * @param boxList
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAllBoxGoal(ArrayList<Zuobiao> boxList)
    {
        for (Zuobiao zuobiao : boxList)
        {
            if (!SokoMap.getCell(zuobiao).check(CellType.gole))
            {
                
                return false;
            }
        }
        return true;
    }
    
    /**
     * 所有的目标点都有箱子覆盖
     * 
     * @param boxList
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAllGoalCover(ArrayList<Zuobiao> boxList)
    {
        for (Zuobiao zb : SokoMap.goleList)
        {
            if (!boxList.contains(zb))
            {
                return false;
            }
        }
        return true;
    }
    
}
