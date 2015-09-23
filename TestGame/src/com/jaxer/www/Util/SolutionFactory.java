package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.ProgressCounter;
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
    public static LinkedList<Solution> getNextSolution(Solution solu)
    {
        
        Cell[][] curMap = solu.getSokoMapAfter();
        if (solu == null || curMap == null)
        {
            return null;
        }
        Logger.debug("======开始计算分支走法。");
        
        ArrayList<Cell> statues = getAllStatues(curMap);
        
        HashSet<Cell> playerCanGoCells = getAllPlayerCanGoCells(curMap);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // 根据
        for (int i = 0; i < statues.size(); i++)
        {
            Cell statue = statues.get(i);
            int x = statue.getX();
            int y = statue.getY();
            
            // 是否能上下移动
            if ((y >= 1 && (y + 1) < curMap[x].length)
                && curMap[x][y + 1].canMoveIn() && curMap[x][y - 1].canMoveIn())
            {
                // 推动时，站人的位置人能过去，且目标位不是死角
                if (playerCanGoCells.contains(curMap[x][y - 1])
                    && DeadPoitUtil.isPointNeedGo(x, y + 1))
                {
                    solutions.add(new Solution(AspectEnum.down, x, y, solu));
                    
                }
                if (playerCanGoCells.contains(curMap[x][y + 1])
                    && DeadPoitUtil.isPointNeedGo(x, y - 1))
                {
                    solutions.add(new Solution(AspectEnum.up, x, y, solu));
                }
            }
            
            // 能否左右移动
            if (x >= 1 && (x + 1) < curMap.length
                && curMap[x + 1][y].canMoveIn() && curMap[x - 1][y].canMoveIn())
            {
                
                if (playerCanGoCells.contains(curMap[x - 1][y])
                    && DeadPoitUtil.isPointNeedGo(x + 1, y))
                {
                    solutions.add(new Solution(AspectEnum.right, x, y, solu));
                    
                }
                if (playerCanGoCells.contains(curMap[x + 1][y])
                    && DeadPoitUtil.isPointNeedGo(x - 1, y))
                {
                    solutions.add(new Solution(AspectEnum.left, x, y, solu));
                    
                }
                
            }
            
        }
        
        if (Logger.isDebugEnable())
        {
            int num = solutions.size();
            Logger.debug(solu.toString());
            Logger.debug("接下来有" + num + "种分支走法：");
            for (int i = 0; i < num; i++)
            {
                Logger.debug("序号：" + (1 + i));
                Logger.debug(solutions.get(i).toString());
            }
            Logger.debug("======获取分支走法结束。");
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
        // 把玩家现在的位置放入set中
        HashSet<Cell> cells = new HashSet<Cell>();
        for (int i = 0; i < curMap.length; i++)
        {
            for (int j = 0; j < curMap[0].length; j++)
            {
                if (curMap[i][j].getItem() == ItemType.player)
                {
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
     * 批量获取走法的下一步走法。
     * 
     * @param needSub
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static LinkedList<Solution> getNextSolutionsBatch(
        LinkedList<Solution> needSub)
    {
        LinkedList<Solution> nextSolutionList = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(needSub.size(), "获取下一步走法");
        while (!needSub.isEmpty())
        {
            pc.addProgress();
            Solution removeFirst = needSub.removeFirst();
            LinkedList<Solution> temp =
                SolutionFactory.getNextSolution(removeFirst);
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
                Logger.debug("=============");
            }
        }
        return nextSolutionList;
    }
    
    /**
     * 一层一层历遍根据走法衍生的走法
     * 
     * @return 最后成功的解法，无解返回null
     * @see [类、类#方法、类#成员]
     */
    public static Solution runByLevel(Solution solution)
    {
        // 获取现在能走的走法列表
        LinkedList<Solution> nextSolution =
            SolutionFactory.getNextSolution(solution);
            
        // 每走一步，获取下一步的走法列表，不断循环
        int level = 1;
        while (!nextSolution.isEmpty())
        {
            
            Logger.info("开始走第" + level + "层分支，有走法：" + nextSolution.size());
            
            // 实施走法
            LinkedList<Solution> needSub =
                SolutionFactory.getSoultionNeedSub(nextSolution);
                
            // 有成功, 中断向下一级循环
            if (needSub.size() == 1
                && needSub.get(0).getResult() == Result.success)
            {
                return needSub.get(0);
            }
            
            // 下一步的走法列表
            nextSolution = SolutionFactory.getNextSolutionsBatch(needSub);
            
            level++;
        }
        
        if (Logger.isdebug)
        {
            Util.printMapSet();
        }
        return null;
    }
    
    /**
     * 对每一种走法进行行走，返回走成功的走法
     * 
     * @param solutions
     * @return
     */
    public static LinkedList<Solution> getSoultionNeedSub(
        LinkedList<Solution> solutions)
    {
        LinkedList<Solution> needSub = new LinkedList<Solution>();
        ProgressCounter pc = new ProgressCounter(solutions.size(), "演算走法列表");
        while (!solutions.isEmpty())
        {
            pc.addProgress();
            Solution solu = solutions.removeFirst();
            solu.play(solu.getSokoMapBefor());
            if (solu.getResult() == Result.success)
            {
                needSub.clear();
                needSub.add(solu);
                pc.end();
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
