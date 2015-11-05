package com.jaxer.www.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.Util.ZuobiaoUtil;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
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
        Logger.debug("======开始计算下一步走法。");
        
        ArrayList<Zuobiao> boxList = solu.getBoxAndManAfter();
        
        Zuobiao man = boxList.remove(0);
        
        ArrayList<Zuobiao> playerCanGoCells = getPlayerCanGoCells(boxList, man);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // 计算每一个箱子能走的步法
        for (int i = 0; i < boxList.size(); i++)
        {
            Zuobiao box = boxList.get(i);
            
            // 历遍上下左右
            for (AspectEnum aspect : AspectEnum.values())
            {
                
                // 推动时，站人的位置人能过去
                Zuobiao zuobiaoMan = ZuobiaoUtil.getMovePlayer(box, aspect);
                if (!playerCanGoCells.contains(zuobiaoMan))
                {
                    continue;
                }
                // 目标位，已经有箱子或是墙，不用推了
                Zuobiao zuobiaoGo = ZuobiaoUtil.getMove(box, aspect);
                if (!canGo(boxList, zuobiaoGo))
                {
                    continue;
                }
                // 目标位是死角，不用推了
                if (DeadPoitUtil.isPointDie(zuobiaoGo))
                {
                    continue;
                }
                
                // 已经存在的特征地图不再使用
                if (isExist(boxList, box, aspect, playerCanGoCells, zuobiaoGo))
                {
                    Logger.debug("以上结果重复，或不是最优解");
                    continue;
                }
                
                // 移动后是否成功
                box.moveByAspect(aspect);
                if (Util.isAllBoxGoal(boxList))
                {
                    SolutionManager.setSuccess(new Solution(aspect, i, solu));
                    return null;
                }
                box.backByAspect(aspect);
                
                solutions.add(new Solution(aspect, i, solu));
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
        
        return solutions;
    }
    
    private static boolean isExist(ArrayList<Zuobiao> boxs, Zuobiao box,
        AspectEnum aspect, ArrayList<Zuobiao> playerCanGoCells,
        Zuobiao zuobiaoGo)
    {
        // 移动箱子，得到移动后的列表，生成字串后复原
        box.moveByAspect(aspect);
        byte[] boxsStr = Util.coverBox(boxs);
        box.backByAspect(aspect);
        
        // 移动后，箱子位站人，移动后箱子位不能站人
        byte[] manStr = null;
        playerCanGoCells.add(box);
        if (playerCanGoCells.contains(zuobiaoGo))
        {
            playerCanGoCells.remove(zuobiaoGo);
            manStr = Util.coverMan(playerCanGoCells, boxsStr);
            playerCanGoCells.add(zuobiaoGo);
        }
        else
        {
            manStr = Util.coverMan(playerCanGoCells, boxsStr);
        }
        playerCanGoCells.remove(box);
        
        return Util.isExist(manStr);
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
        
        return getPlayerCanGoCells(solu.getBoxListAfter(),
            solu.getManAfterStep());
    }
    
    /**
     * 获取玩家能移动到的位置
     * 
     * @param curMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Zuobiao> getPlayerCanGoCells(
        ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        // 地图中可以站人的坐标
        HashSet<Zuobiao> emptyList = new HashSet<>(SokoMap.manCanGoCells);
        
        // 先移出箱子列表的位置
        emptyList.removeAll(boxList);
        
        ArrayList<Zuobiao> canGoList = new ArrayList<Zuobiao>();
        // 把玩家现在的位置放入可以去的set中
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
            for (Zuobiao zuobiao : boxList)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "B");
            }
            for (Zuobiao zuobiao : canGoList)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "a");
            }
            
            Logger.info(Util.drawMap(mapStr));
        }
        
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
    private static boolean canGo(ArrayList<Zuobiao> boxList,
        Zuobiao gotoZuobiao)
    {
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
        if (boxList.contains(gotoZuobiao))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 批量获取走法的下一步走法。
     * 
     * @param needSub
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Solution loopNextSolutionsBatch(LinkedList<Solution> needSub)
    {
        
        LinkedList<Solution> nextSolutionList = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(needSub.size(), "获取下一步走法");
        while (!needSub.isEmpty())
        {
            pc.addProgress();
            
            Solution removeFirst = needSub.removeFirst();
            
            LinkedList<Solution> temp =
                SolutionFactory.getNextSolution(removeFirst);
                
            removeFirst = null;
            
            if (SolutionManager.getSuccess() != null)
            {
                return SolutionManager.getSuccess();
            }
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
                Logger.debug("=============");
            }
        }
        
        needSub.addAll(nextSolutionList);
        return null;
    }
    
}
