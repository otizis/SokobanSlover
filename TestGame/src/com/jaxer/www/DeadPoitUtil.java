package com.jaxer.www;

import java.util.HashSet;

import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Solution;

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
        Logger.info("==开始死点推算==");
        load = false;
        for (Cell[] cells : curMap)
        {
            for (Cell cell : cells)
            {
                if (cell.isGole() || cell.getItem() == ItemType.wall
                    || cell.getItem() == ItemType.statue)
                {
                    continue;
                }
                int x = cell.getX();
                int y = cell.getY();
                
                if (isPointDead(curMap, x, y))
                {
                    deadSet.add(getKey(x, y));
                    continue;
                }
                
                if (smartDeadPoint(curMap, x, y))
                {
                    deadSet.add(getKey(x, y));
                    continue;
                }
            }
        }
        Logger.info("==结束死点推算==");
    }
    
    private static String getKey(int x, int y)
    {
        return x + "," + y;
    }
    
    /**
     * 该点位是否是死点,墙角
     * 
     * @param x
     * @param y
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isPointNeedGo(int x, int y)
    {
        return !deadSet.contains(getKey(x, y));
        
    }
    
    /**
     * 是否是靠墙的死点
     * 
     * @param curMap
     * @param x
     * @param y
     * @return
     * @see [类、类#方法、类#成员]
     */
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
    
    /**
     * 死点推断
     * 
     * @param curMap
     * @param y 雕像的y
     * @param x 雕像的x
     * @see [类、类#方法、类#成员]
     */
    public static boolean smartDeadPoint(Cell[][] curMap, int x, int y)
    {

        Cell[][] cloneObject = Util.cloneMapClearStatue(curMap);
        cloneObject[x][y].setItem(ItemType.statue);
        
        Solution solu = new Solution(cloneObject);
        
        // 获取现在能走的走法列表
        Solution runByLevel = SolutionFactory.runByLevel(solu);
        if (null != runByLevel)
        {
            return false;
        }
        
        // 如果一个都不成功，初始化人位置为未到过的地方
        HashSet<Cell> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(cloneObject);
            
        // 四种情况，人在雕像的上下左右
        if (y > 0 && !allPlayerCanGoCells.contains(cloneObject[x][y - 1]))
        {
            // 站人的位置不能是死点，因为该情况下，人为非初始值，雕像也为非初始值。
            // 若出现了，肯定是移动过。若人站在死点，则回退一步推的动作，雕像在死点，不能成立
            if (cloneObject[x][y - 1].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x, y - 1))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x][y - 1].setPlayer();
                    
                    // 获取现在能走的走法列表
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        if (y < (cloneObject[0].length - 1)
            && !allPlayerCanGoCells.contains(cloneObject[x][y + 1]))
        {
            if (cloneObject[x][y + 1].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x, y + 1))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x][y + 1].setPlayer();
                    
                    // 获取现在能走的走法列表
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        if (x > 0 && !allPlayerCanGoCells.contains(cloneObject[x - 1][y]))
        {
            if (cloneObject[x - 1][y].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x - 1, y))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x - 1][y].setPlayer();
                    
                    // 获取现在能走的走法列表
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        if (x < (cloneObject.length - 1)
            && !allPlayerCanGoCells.contains(cloneObject[x + 1][y]))
        {
            if (cloneObject[x + 1][y].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x + 1, y))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x + 1][y].setPlayer();
                    
                    // 获取现在能走的走法列表
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        return true;
    }
}
