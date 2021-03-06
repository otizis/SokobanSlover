package com.jaxer.www.manager;

import com.google.common.collect.Lists;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Solution;
import com.jaxer.www.model.Zuobiao;

import java.util.ArrayList;
import java.util.LinkedList;

public class SolutionManager
{
    /**
     * 一层一层历遍根据走法衍生的走法
     * 
     * @return 最后成功的解法，无解返回null
     * @see [类、类#方法、类#成员]
     */
    public static void runLeft(SokoMap sokoMap)
    {
        
        // 获取现在能走的走法列表
        LinkedList<Solution> nextSolution = sokoMap.getNextSolutionList();
        
        sokoMap.setNextSolutionList(null);
        
        if (sokoMap.getSuccess() != null)
        {
            return;
        }
        
        if (nextSolution == null)
        {
            return;
        }
        // 每走一步，获取下一步的走法列表，不断循环
        while (!nextSolution.isEmpty())
        {
            
            Logger.info("开始走暂存的第" + nextSolution.getFirst().level + "层分支，有走法：" + nextSolution.size());
            
            // 下一步的走法列表
            SolutionFactory.loopNextSolutionsBatch(nextSolution, sokoMap);
            
            if (sokoMap.getSuccess() != null)
            {
                return;
            }
            
        }
        
    }
    
    /**
     * 一层一层历遍根据走法衍生的走法
     * 
     * @return 最后成功的解法，无解返回null
     * @see [类、类#方法、类#成员]
     */
    public static void runByLevel(SokoMap sokoMap)
    {
        
        // 获取现在能走的走法列表
        LinkedList<Solution> nextSolution = SolutionFactory.getNextSolution(new Solution(), sokoMap);
        
        if (sokoMap.getSuccess() != null)
        {
            return;
        }
        
        if (nextSolution == null)
        {
            return;
        }
        // 每走一步，获取下一步的走法列表，不断循环
        int level = 1;
        while (!nextSolution.isEmpty())
        {
                
            if(Logger.isInfo){
                Logger.info("开始走第" + level + "层分支，有走法：" + nextSolution.size());
            }
            
            // 下一步的走法列表
            SolutionFactory.loopNextSolutionsBatch(nextSolution, sokoMap);
            
            if (sokoMap.getSuccess() != null)
            {
                return;
            }
            
            level++;
        }
        
    }
    
    /**
     * 获取步骤后的箱子位置列表
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Zuobiao> getBoxListAfter(Solution solu, SokoMap sokoMap)
    {
        ArrayList<Zuobiao> boxAndManAfter = getBoxAndManAfter(solu, sokoMap);
        boxAndManAfter.remove(0);
        return boxAndManAfter;
    }
    
    /**
     * 返回步数推动前的地图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String drawAfter(Solution solu, SokoMap sokoMap)
    {
        return draw(getManAfterStep(solu, sokoMap), getBoxListAfter(solu, sokoMap), solu.getStep(), sokoMap);
    }
    
    /**
     * 返回步数推动前的地图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String drawBefore(Solution solu, SokoMap sokoMap)
    {
        return draw(getManBeforeStep(solu, sokoMap), getBoxListBefore(solu, sokoMap), solu.getStep(), sokoMap);
    }
    
    /**
     * 根据传入的玩家位置，和箱子位置绘图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String draw(Zuobiao man, ArrayList<Zuobiao> boxList, AspectEnum step, SokoMap sokoMap)
    {
        StringBuilder builder = sokoMap.mapStr();
        Util.replaceZuobiao(builder, man, "a", sokoMap.getMax_x());
        // Util.replaceZuobiao(builder, man, step == null ? "a" :
        // step.getDesc(), sokoMap.getMax_x());
        
        for (Zuobiao box : boxList)
        {
            Util.replaceZuobiao(builder, box, "B", sokoMap.getMax_x());
        }
        
        return Util.drawMap(builder, sokoMap.getMax_x());
    }
    
    /**
     * 根据提供解法步骤，获取箱子和人的最终坐标
     * 
     * @return 坐标列表，人的坐标放在0
     */
    public static ArrayList<Zuobiao> getBoxAndManAfter(Solution solu, SokoMap sokoMap)
    {
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(sokoMap.getBoxList());

        // 若是根节点，直接返回
        Solution root = solu.getLastSolution();
        if (null == root)
        {
            cloneBoxList.add(0, sokoMap.getMan());
            return cloneBoxList;
        }
        
        ArrayList<Solution> solutList = Lists.newArrayList(solu);
        while (root.getLastSolution() != null)
        {
            solutList.add(root);
            root = root.getLastSolution();
        }

        Zuobiao man = null;
        int size = solutList.size();
        for (int i = size-1; i >= 0; i--)
        {
            Solution solution = solutList.get(i);
            int moveIndex = solution.getBoxIndex();
            Zuobiao moveBox = cloneBoxList.get(moveIndex);
            moveBox.moveByAspect(solution.getStep());
            if (i == 0)
            {
                man = sokoMap.getMovePlayer(moveBox, solution.getStep());
            }
        }

        cloneBoxList.add(0, man);
        return cloneBoxList;
        
    }
    
    /**
     * 获取该走法前的地图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Zuobiao> getBoxListBefore(Solution solu, SokoMap sokoMap)
    {
        if (null == solu.getLastSolution())
        {
            return Util.cloneBoxList(sokoMap.getBoxList());
        }
        return getBoxListAfter(solu.getLastSolution(), sokoMap);
    }
    
    /**
     * @return
     */
    public static Zuobiao getManAfterStep(Solution solu, SokoMap sokoMap)
    {
        if (solu.getLastSolution() == null)
        {
            // 初始位置
            return sokoMap.getMan();
        }
        // 现在箱子的位置，就是走完后人的位置
        return getBoxListBefore(solu, sokoMap).get(solu.getBoxIndex());
    }
    
    /**
     * @return
     */
    public static Zuobiao getManBeforeStep(Solution solu, SokoMap sokoMap)
    {
        if (solu.getLastSolution() == null)
        {
            return sokoMap.getMan();
        }
        Zuobiao manAfter = getManAfterStep(solu, sokoMap);
        return sokoMap.getMovePlayer(manAfter, solu.getStep());
    }
    
}
