package com.jaxer.www.Util;

import java.util.HashSet;

import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.DieCell;
import com.jaxer.www.model.Solution;

public class DeadPoitUtil
{
    private static boolean smart = true;
    
    static HashSet<String> deadSet = new HashSet<String>();
    
    public static void loadDeadSet(Cell[][] curMap)
    {
        Logger.info("==开始死点推算==");
        Logger.turnOff();
        Long begin = System.currentTimeMillis();
        
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
                
                if (smart && smartDeadPoint(curMap, x, y))
                {
                    deadSet.add(getKey(x, y));
                    
                    continue;
                }
            }
        }
        Long end = System.currentTimeMillis();
        Logger.turnOn();
        Logger.info("==结束死点推算,耗时：" + (end - begin));
        Cell[][] cloneMap = Util.cloneMap(curMap);
        
        for (String point : deadSet)
        {
            String[] xy = point.split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            cloneMap[x][y] = new DieCell();
        }
        Logger.info(Util.mapToString(cloneMap));
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
     * 死点推断，移除所有雕像，只放一个雕像在推断的点，看是否能移动到任一个目标点。
     * 如果不能，再测试人位置是否可以有其他情况，历遍上下左右的位置，再次进行推断。
     * 
     * @param curMap
     * @param y 雕像的y
     * @param x 雕像的x
     * @see [类、类#方法、类#成员]
     */
    public static boolean smartDeadPoint(Cell[][] curMap, int x, int y)
    {
        // 不记录计算死点时的地图特征值，每次都重置
        Util.resetMapSet();
        
        Cell[][] cloneObject = Util.cloneMapClearStatue(curMap);
        cloneObject[x][y].setItem(ItemType.statue);
        
        Solution solu = new Solution(cloneObject);
        
        // 获取该模式，人为初始值，能否推动到任一目标点
        Solution runByLevel = SolutionFactory.runByLevel(solu);
        if (null != runByLevel)
        {
            return false;
        }
        
        // 如果不成功，获取该初始化人位置的等价位置
        HashSet<Cell> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(cloneObject);
            
        // 四种情况，人在雕像的上下左右，且不是以上等价情况的位置，再次推断
        if (y > 0)
        {
            
            int player_x = x;
            int player_y = y - 1;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // 如果获取到解法，表示不是死点
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        if (y < (cloneObject[0].length - 1))
        {
            int player_x = x;
            int player_y = y + 1;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // 如果获取到解法，表示不是死点
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        if (x > 0)
        {
            int player_x = x - 1;
            int player_y = y;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // 如果获取到解法，表示不是死点
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        if (x < (cloneObject.length - 1))
        {
            int player_x = x + 1;
            int player_y = y;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // 如果获取到解法，表示不是死点
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 测试人位置是否可以有其他情况，历遍上下左右的位置，再次进行推断。
     * 
     * @param x 雕像位置
     * @param y 雕像位置
     * @param cloneObject
     * @param allPlayerCanGoCells 第一种情况人站位置的同等位置
     * @param player_x 其他玩家位置
     * @param player_y 其他玩家位置
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static Solution checkSwitchPlayer(int x, int y,
        Cell[][] cloneObject, HashSet<Cell> allPlayerCanGoCells, int player_x,
        int player_y)
    {
        if (cloneObject[player_x][player_y].getItem() == ItemType.empty
            && !allPlayerCanGoCells.contains(cloneObject[player_x][player_y]))
        {
            // 站人的位置不能是死点，因为该情况下，人为非初始值，雕像也为非初始值。
            // 若出现了，肯定是移动过。若人站在死点，则回退一步推的动作，雕像在死点，不能成立
            if (!isPointNeedGo(player_x, player_y))
            {
                Cell[][] cloneMapClearPlayer =
                    Util.cloneMapClearPlayerAndStatue(cloneObject);
                cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                cloneMapClearPlayer[player_x][player_y].setPlayer();
                
                // 获取现在能走的走法列表
                Solution run = SolutionFactory
                    .runByLevel(new Solution(cloneMapClearPlayer));
                return run;
                
            }
        }
        return null;
    }
}
