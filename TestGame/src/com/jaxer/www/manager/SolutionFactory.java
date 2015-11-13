package com.jaxer.www.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.model.FastSet;
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
    public static LinkedList<Solution> getNextSolution(Solution solu, SokoMap sokoMap)
    {
        
        if (solu == null)
        {
            return null;
        }
        Logger.debug("======开始计算下一步走法。");
        
        ArrayList<Zuobiao> boxList = SolutionManager.getBoxAndManAfter(solu, sokoMap);
        
        Zuobiao man = boxList.remove(0);
        
        FastSet playerCanGoCells = sokoMap.getPlayerCanGoCells(boxList, man);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // 计算每一个箱子能走的步法
        for (int i = 0; i < boxList.size(); i++)
        {
            Zuobiao box = boxList.get(i);
            
            // 历遍上下左右
            for (AspectEnum aspect : AspectEnum.values())
            {
                
                // 推动时，站人的位置人能过去
                Zuobiao zuobiaoMan = sokoMap.getMovePlayer(box, aspect);
                if (!playerCanGoCells.contains(zuobiaoMan))
                {
                    continue;
                }
                // 目标位，已经有箱子或是墙，不用推了
                Zuobiao zuobiaoGo = sokoMap.getMove(box, aspect);
                if (!sokoMap.canGo(boxList, zuobiaoGo))
                {
                    continue;
                }
                // 目标位是死角，不用推了
                if (sokoMap.isPointDie(zuobiaoGo))
                {
                    continue;
                }
                
                // 已经存在的特征地图不再使用
                if (sokoMap.isExist(boxList, box, aspect))
                {
                    Logger.debug("以上结果重复，或不是最优解");
                    continue;
                }
                
                // 移动后是否成功
                box.moveByAspect(aspect);
                int less = Util.boxsNumNotGole(boxList, sokoMap);
                if (less == 0)
                {
                    sokoMap.setSuccess(new Solution(aspect, i, solu));
                    return null;
                }
                box.backByAspect(aspect);
                
                solutions.add(new Solution(aspect, i, solu, less));
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
                // Logger.debug(solutions.get(i).drawBefore());
            }
            Logger.debug("======获取分支走法结束。");
        }
        
        return solutions;
    }
    
    /**
     * 批量获取走法的下一步走法。
     * 
     * @param needSub
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static void loopNextSolutionsBatch(LinkedList<Solution> needSub, SokoMap sokoMap)
    {
        
        LinkedList<Solution> nextSolutionList = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(needSub.size(), "获取下一步走法");
        while (!needSub.isEmpty())
        {
            pc.addProgress();
            
            Solution removeFirst = needSub.removeFirst();
            
            LinkedList<Solution> temp = SolutionFactory.getNextSolution(removeFirst, sokoMap);
            
            removeFirst = null;
            
            if (sokoMap.getSuccess() != null)
            {
                return;
            }
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
            }
        }
        int all = nextSolutionList.size();
        if (all < 500000)
        {
            needSub.addAll(nextSolutionList);
            return;
        }
        
        int level = 1;
        while (true)
        {
            Iterator<Solution> iterator = nextSolutionList.iterator();
            double count = 0;
            while (iterator.hasNext())
            {
                Solution next = iterator.next();
                
                if (next.getBoxsNotGole() == level)
                {
                    needSub.add(next);
                    count++;
                }
            }
            Logger
                .info(all + "中留下" + level + "个箱子未中的有" + count + "个,占" + Math.round(count * 10000 / all) / 100.0 + "%");
            if (level == sokoMap.getGoleList().size())
            {
                break;
            }
            int maxCount = 700000;
            if (count > maxCount)
            {
                
                Logger.info(">" + maxCount + ",抛弃" + (all - count) + "其他。");
                break;
            }
            if (count != 0 && (count / all) > 0.2)
            {
                Logger.info(">20%抛弃" + (all - count) + "其他。");
                break;
            }
            level++;
        }
    }
    
}
