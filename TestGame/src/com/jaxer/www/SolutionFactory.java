package com.jaxer.www;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Result;
import com.jaxer.www.model.Solution;

public class SolutionFactory
{
    /**
     * 根据现在的走法结果，获取接下来可能的步骤
     * 
     * @param curMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Solution> getNextSolution(Solution solu)
    {
        
        if (solu == null || solu.getThisStepMap() == null)
        {
            return null;
        }
        Util.debug("======开始计算分支走法。");
        Cell[][] curMap = solu.getThisStepMap();
        
        ArrayList<Cell> statues = getAllStatues(curMap);
        
        HashSet<Cell> playerCanGoCells = getAllPlayerCanGoCells(curMap);
        
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        
        // 根据
        for (int i = 0; i < statues.size(); i++)
        {
            Cell statue = statues.get(i);
            int x = statue.getX();
            int y = statue.getY();
            
            // 是否能上下移动
            if ((y >= 1 && (y + 1) < curMap[x].length)
                && curMap[x][y + 1].getItem() == ItemType.empty
                && curMap[x][y - 1].getItem() == ItemType.empty)
            {
                if (playerCanGoCells.contains(curMap[x][y - 1])
                    && DeadPoitUtil.isPointNeedGo(x, y + 1))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x][y - 1].setPlayer();
                    solutions.add(
                        new Solution(cloneMap, AspectEnum.down, x, y, solu));
                        
                }
                if (playerCanGoCells.contains(curMap[x][y + 1])
                    && DeadPoitUtil.isPointNeedGo(x, y - 1))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x][y + 1].setPlayer();
                    solutions
                        .add(new Solution(cloneMap, AspectEnum.up, x, y, solu));
                }
            }
            
            // 能否左右移动
            if (x >= 1 && (x + 1) < curMap.length
                && curMap[x + 1][y].getItem() == ItemType.empty
                && curMap[x - 1][y].getItem() == ItemType.empty)
            {
                
                if (playerCanGoCells.contains(curMap[x - 1][y])
                    && DeadPoitUtil.isPointNeedGo(x + 1, y))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x - 1][y].setPlayer();
                    solutions.add(
                        new Solution(cloneMap, AspectEnum.right, x, y, solu));
                        
                }
                if (playerCanGoCells.contains(curMap[x + 1][y])
                    && DeadPoitUtil.isPointNeedGo(x - 1, y))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x + 1][y].setPlayer();
                    solutions.add(
                        new Solution(cloneMap, AspectEnum.left, x, y, solu));
                        
                }
                
            }
            
        }
        
        Solution.sort(solutions);
        if (Util.isDebugEnable())
        {
            int num = solutions.size();
            Util.debug(solu.toString());
            Util.debug("接下来有" + num + "种分支走法：");
            for (int i = 0; i < num; i++)
            {
                Util.debug("序号：" + (1 + i));
                Util.debug(solutions.get(i).toString());
            }
            Util.debug("======获取分支走法结束。");
        }
        return solutions;
    }
    
    /**
     * 获取玩家能移动到的位置
     * 
     * @param curMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HashSet<Cell> getAllPlayerCanGoCells(Cell[][] curMap)
    {
        HashSet<Cell> cells = new HashSet<Cell>();
        for (int i = 0; i < curMap.length; i++)
        {
            for (int j = 0; j < curMap[0].length; j++)
            {
                if (curMap[i][j].getItem() == ItemType.player)
                {
                    curMap[i][j].setItem(ItemType.empty);
                    cells.add(curMap[i][j]);
                    
                    break;
                }
            }
            if (!cells.isEmpty())
            {
                break;
            }
        }
        HashSet<Cell> hasLoop = new HashSet<Cell>();
        HashSet<Cell> temp = new HashSet<Cell>();
        boolean plusCellFlag = false;
        do
        {
            plusCellFlag = false;
            Iterator<Cell> iterator = cells.iterator();
            while (iterator.hasNext())
            {
                
                Cell cell = (Cell)iterator.next();
                // 已经存在的跳过
                if (hasLoop.contains(cell))
                {
                    continue;
                }
                else
                {
                    hasLoop.add(cell);
                }
                int x = cell.getX();
                int y = cell.getY();
                if (y > 0)
                {
                    Cell upCell = curMap[x][y - 1];
                    if (upCell.getItem() == ItemType.empty)
                    {
                        temp.add(upCell);
                        plusCellFlag = true;
                    }
                }
                if (x > 0)
                {
                    Cell leftCell = curMap[x - 1][y];
                    if (leftCell.getItem() == ItemType.empty)
                    {
                        temp.add(leftCell);
                        plusCellFlag = true;
                    }
                }
                if (y < (curMap[0].length - 1))
                {
                    Cell downCell = curMap[x][y + 1];
                    if (downCell.getItem() == ItemType.empty)
                    {
                        temp.add(downCell);
                        plusCellFlag = true;
                    }
                }
                if (x < (curMap.length - 1))
                {
                    Cell rightCell = curMap[x + 1][y];
                    if (rightCell.getItem() == ItemType.empty)
                    {
                        temp.add(rightCell);
                        plusCellFlag = true;
                    }
                }
            }
            
            cells.addAll(temp);
            temp.clear();
            
        } while (plusCellFlag);
        
        return cells;
    }
    
    /**
     * 是否在角落，再也不能动了
     * 
     * @param curMap
     * @param x
     * @param y
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static boolean isStayEver(Cell[][] curMap, int x, int y)
    {
        
        // 点位是否在地图的死角上
        if (DeadPoitUtil.isNeedLoadDeadSet())
        {
            DeadPoitUtil.loadDeadSet(curMap);
        }
        
        return DeadPoitUtil.deadSet.contains(x + "," + y);
    }
    
    /**
     * 获取地图中所有的雕像列表
     * 
     * @param curMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static ArrayList<Cell> getAllStatues(Cell[][] curMap)
    {
        ArrayList<Cell> statues = new ArrayList<Cell>();
        for (int i = 0; i < curMap.length; i++)
        {
            for (int j = 0; j < curMap[0].length; j++)
            {
                if (curMap[i][j].isStatue())
                {
                    statues.add(curMap[i][j]);
                }
            }
        }
        return statues;
    }
    
    /**
     * 判断该走法是否是死胡同
     * 
     * @param solution
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isdead(Solution solution)
    {
        if (solution == null || solution.getThisStepMap() == null)
        {
            return true;
        }
        Cell[][] curMap = solution.getThisStepMap();
        
        // 历遍所有雕像
        ArrayList<Cell> statues = getAllStatues(curMap);
        for (int i = 0; i < statues.size(); i++)
        {
            Cell statue = statues.get(i);
            if (statue.isGole())
            {
                continue;
            }
            int x = statue.getX();
            int y = statue.getY();
            if (x == 0 || (x + 1) == curMap.length || y == 0
                || (y + 1) == curMap[0].length)
            {
                return true;
            }
            
            // 靠墙的判断
            if (isStayEver(curMap, x, y))
            {
                return true;
            }
            
        }
        
        return false;
    }
    
    /**
     * 批量获取走法的下一步走法。
     * 
     * @param needSub
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Solution> getNextSolutionsBatch(
        ArrayList<Solution> needSub)
    {
        ArrayList<Solution> nextSolutionList = new ArrayList<Solution>();
        for (Solution solu : needSub)
        {
            ArrayList<Solution> temp = SolutionFactory.getNextSolution(solu);
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
            }
            Util.debug("=============");
        }
        return nextSolutionList;
    }
    
    /**
     * 对每一种走法进行行走，返回走成功的走法
     * 
     * @param solutions
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Solution> getSoultionNeedSub(
        ArrayList<Solution> solutions)
    {
        ArrayList<Solution> needSub = new ArrayList<Solution>();
        
        for (Solution solu : solutions)
        {
            solu.play();
            if (solu.getResult() == Result.success)
            {
                Util.result.add(solu);
                break;
            }
            if (solu.getResult() == Result.needsub)
            {
                needSub.add(solu);
                
            }
            
        }
        return needSub;
    }
}
