package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.jaxer.www.Filter.BloomFliter;
import com.jaxer.www.Filter.HashSetFilter;
import com.jaxer.www.Filter.TurnOffFliter;
import com.jaxer.www.api.MapFliter;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;
import com.jaxer.www.myexception.MyException;

public class Util
{
    
    private static MapFliter fliter = new BloomFliter();
    
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
    
    /**
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     */
    /**
     * <一句话功能简述> <功能详细描述>
     * 
     * @param coll
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] coverBox(ArrayList<Zuobiao> coll)
    {
        byte[] desc = new byte[(SokoMap.max_x + 1) * (SokoMap.max_y + 1)];
        for (Zuobiao b : coll)
        {
            int i = getLen(b);
            desc[i] = 1;
        }
        if (Logger.isdebug)
        {
            Logger.debug(Arrays.toString(desc));
        }
        return desc;
    }
    
    /**
     * 将人坐标打孔到给的地图特征码中。 <功能详细描述>
     * 
     * @param mans
     * @param mapByte
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] coverMan(ArrayList<Zuobiao> mans, byte[] mapByte)
    {
        for (Zuobiao b : mans)
        {
            int index = getLen(b);
            if (mapByte[index] == 1)
            {
                throw new MyException("描述有重复。");
            }
            mapByte[index] = 2;
        }
        if (Logger.isdebug)
        {
            Logger.debug(Arrays.toString(mapByte));
        }
        return mapByte;
    }
    
    /**
     * 转化二维坐标为一维数组的序号
     * 
     * @param b
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static int getLen(Zuobiao b)
    {
        int index = b.getX() + (b.getY() * (SokoMap.max_x + 1));
        return index;
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
    
    /**
     * 是否成功放入set，表示不存在重复
     * 
     * @param mapStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isExist(byte[] all)
    {
        
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
