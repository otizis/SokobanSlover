package com.jaxer.www;

import java.util.HashSet;
import java.util.Iterator;

import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;

public class Util
{
    
    /**
     * 地图特征值，如果出现过就会记录。 如果重复，表示出现过其他走出这个特征走法，本次为非最优的走法
     */
    private static HashSet<String> mapSet = new HashSet<String>();
    
    /**
     * 克隆地图
     * 
     * @param a
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Cell[][] cloneMap(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
            }
        }
        return b;
    }
    
    /**
     * 克隆地图，并移除玩家标记
     * 
     * @param a
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Cell[][] cloneMapClearPlayer(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
                if (b[i][j].getItem() == ItemType.player)
                {
                    b[i][j].setItem(ItemType.empty);
                }
            }
        }
        return b;
    }
    
    /**
     * 克隆地图，并移除玩家标记
     * 
     * @param a
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Cell[][] cloneMapClearStatue(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
                if (b[i][j].getItem() == ItemType.statue)
                {
                    b[i][j].setItem(ItemType.empty);
                }
            }
        }
        return b;
    }
    
    /**
     * 克隆地图，并移除玩家标记，雕像标记
     * 
     * @param a
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Cell[][] cloneMapClearPlayerAndStatue(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
                if (b[i][j].getItem() == ItemType.player||
                    b[i][j].getItem() == ItemType.statue)
                {
                    b[i][j].setItem(ItemType.empty);
                }
            }
        }
        return b;
    }
    
    /**
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String descMap(Cell[][] map)
    {
        Cell[][] cloneObject = cloneMap(map);
        
        HashSet<Cell> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(cloneObject);
            
        Iterator<Cell> iterator = allPlayerCanGoCells.iterator();
        
        while (iterator.hasNext())
        {
            Cell cell = (Cell)iterator.next();
            cell.setPlayer();
        }
        
        StringBuilder buid = new StringBuilder();
        for (int j = 0; j < cloneObject[0].length; j++)
        {
            for (int i = 0; i < cloneObject.length; i++)
            {
                buid.append(cloneObject[i][j].draw());
            }
        }
        return buid.toString();
    }
    
    /**
     * 是否成功放入set，表示不存在重复
     * 
     * @param mapStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean putIfAb(String mapStr)
    {
        
        if (mapSet.contains(mapStr))
        {
            return false;
        }
        mapSet.add(mapStr);
        return true;
        
    }
    
}
