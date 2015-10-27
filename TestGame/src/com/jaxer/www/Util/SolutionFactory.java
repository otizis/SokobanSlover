package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.LinkedList;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.enums.Result;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Solution;
import com.jaxer.www.model.Zuobiao;

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
        if (solu == null)
        {
            return null;
        }
        long beginTime = System.currentTimeMillis();
        Logger.debug("======开始计算下一步走法。");
        
        ArrayList<Zuobiao> boxs = solu.getBoxListAfter();
        
        ArrayList<Zuobiao> playerCanGoCells = getPlayerCanGoCells(solu);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // 计算每一个箱子能走的步法
        for (int i = 0; i < boxs.size(); i++)
        {
            Zuobiao box = boxs.get(i);
            
            // 历遍上下左右
            for (AspectEnum aspce : AspectEnum.values())
            {
                
                Zuobiao zuobiaoGo = ZuobiaoUtil.getMove(box, aspce);
                if (!canGo(solu, zuobiaoGo))
                {
                    continue;
                }
                // 目标位不是死角
                if (!DeadPoitUtil.isPointNeedGo(zuobiaoGo))
                {
                    continue;
                }
                
                // 推动时，站人的位置人能过去
                Zuobiao zuobiaoMan = ZuobiaoUtil.getMovePlayer(box, aspce);
                if (!playerCanGoCells.contains(zuobiaoMan))
                {
                    continue;
                }
                
                solutions.add(new Solution(aspce, i, solu));
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
                Logger.debug(solutions.get(i).drawBefore());
            }
            Logger.debug("======获取分支走法结束。");
        }
        
        TimeStamps.addTime("Factory getNextSolution",
            System.currentTimeMillis() - beginTime);
        return solutions;
    }
    
    /**
     * 获取玩家能移动到的位置
     * 
     * @param curMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Zuobiao> getPlayerCanGoCells(Solution solu)
    {
        long beginTime = System.currentTimeMillis();
        
        // 地图中可以站人的坐标
        ArrayList<Zuobiao> emptyList = Util.cloneBoxList(SokoMap.manCanGoCells);
        
        // 先移出箱子列表的位置
        emptyList.removeAll(solu.getBoxListAfter());
        
        ArrayList<Zuobiao> canGoList = new ArrayList<Zuobiao>();
        // 把玩家现在的位置放入可以去的set中
        Zuobiao man = solu.getManAfterStep();
        canGoList.add(man);
        emptyList.remove(man);
        
        // 新增的坐标数
        int count = 1;
        // 开始的坐标
        int start = 0;
        while (count > 0)
        {
            int addTemp = 0;
            // 循环上一次循环增加的坐标，四周是否是空
            while (count-- > 0)
            {
                Zuobiao canGo = canGoList.get(start++);
                for (AspectEnum asp : AspectEnum.values())
                {
                    Zuobiao manEdge = ZuobiaoUtil.getMove(canGo, asp);
                    
                    if (emptyList.contains(manEdge))
                    {
                        emptyList.remove(manEdge);
                        canGoList.add(manEdge);
                        addTemp++;
                    }
                }
            }
            count = addTemp;
        }
        
        if (Logger.isdebug)
        {
            StringBuilder mapStr = Util.mapStr();
            for (Zuobiao zuobiao : solu.getBoxListAfter())
            {
                Util.replaceZuobiao(mapStr, zuobiao, "B");
            }
            for (Zuobiao zuobiao : canGoList)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "a");
            }
            
            Logger.info(Util.drawMap(mapStr));
        }
        TimeStamps.addTime("Factory getAllPlayerCanGoCells",
            System.currentTimeMillis() - beginTime);
            
        return canGoList;
    }
    
    /**
     * 解法中，地图坐标为空，且不是box其中之一，返回true
     * 
     * 
     * @param solu
     * @param gotoCell
     * @see [类、类#方法、类#成员]
     */
    private static boolean canGo(Solution solu, Zuobiao gotoZuobiao)
    {
        long beginTime = System.currentTimeMillis();
        
        if (gotoZuobiao == null)
        {
            return false;
        }
        // 目标位，不能是墙
        if (SokoMap.getCell(gotoZuobiao).check(CellType.wall))
        {
            return false;
            
        }
        // 目标位不是箱子
        if (solu.getBoxListAfter().contains(gotoZuobiao))
        {
            return false;
        }
        TimeStamps.addTime("Factory canGo",
            System.currentTimeMillis() - beginTime);
            
        return true;
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
        long beginTime = System.currentTimeMillis();
        
        LinkedList<Solution> needSub = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(solutions.size(), "演算走法列表");
        
        while (!solutions.isEmpty())
        {
            pc.addProgress();
            Solution solu = solutions.removeFirst();
            
            solu.play();
            
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
        TimeStamps.addTime("Factory getSoultionNeedSub",
            System.currentTimeMillis() - beginTime);
            
        return needSub;
    }
}
