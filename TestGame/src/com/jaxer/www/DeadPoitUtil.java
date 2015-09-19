package com.jaxer.www;

import java.util.HashSet;

import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;

public class DeadPoitUtil
{
    static boolean load = true;
    
    public static boolean isNeedLoadDeadSet()
    {
        return load;
    }
    
    
    static HashSet<String> deadSet = new HashSet<String>();
    
    public static void loadDeadSet(Cell[][] curMap)
    {
        load = false;
        for (Cell[] cells : curMap)
        {
            for (Cell cell : cells)
            {
                int x = cell.getX();
                int y = cell.getY();
                
                if (isPointDead(curMap, x, y))
                {
                    deadSet.add(x + "," + y);
                }
                
            }
        }
        
    }
    
    /** 
     * 该点位是否需要去，如果是死点就不用推了
     * @param x
     * @param y
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isPointNeedGo(int x,int y)
    {
        return !deadSet.contains(x+","+y);
        
    }
    
    private static boolean isPointDead(Cell[][] curMap, int x, int y)
    {
        
        // 上 右 下 左 为固定的位置，标为1
        int[] UpRiDoLe = {0, 0, 0, 0};
        if (x == 0 || curMap[x - 1][y].getItem() == ItemType.wall)
        {
            UpRiDoLe[3] = 1;
        }
        else if (x == (curMap.length - 1)
            || curMap[x + 1][y].getItem() == ItemType.wall)
        {
            UpRiDoLe[1] = 1;
        }
        
        if (y == 0 || curMap[x][y - 1].getItem() == ItemType.wall)
        {
            UpRiDoLe[0] = 1;
        }
        else if (y == (curMap[0].length - 1)
            || curMap[x][y + 1].getItem() == ItemType.wall)
        {
            UpRiDoLe[2] = 1;
        }
        
        for (int i = 0; i < 4; i++)
        {
            int j = i + 1;
            if (j == 4)
            {
                j = 0;
            }
            if (UpRiDoLe[i] + UpRiDoLe[j] == 2)
            {
                return true;
            }
        }
        return false;
    }
}
