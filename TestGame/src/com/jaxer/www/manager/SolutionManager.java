package com.jaxer.www.manager;

import java.util.ArrayList;
import java.util.LinkedList;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Solution;
import com.jaxer.www.model.Zuobiao;

public class SolutionManager
{
    private static Solution success;
    
    /**
     * @return 返回 success
     */
    public static Solution getSuccess()
    {
        return success;
    }
    
    /**
     * @param 对success进行赋值
     */
    public static void setSuccess(Solution success)
    {
        SolutionManager.success = success;
    }
    
    /**
     * 一层一层历遍根据走法衍生的走法
     * 
     * @return 最后成功的解法，无解返回null
     * @see [类、类#方法、类#成员]
     */
    public static Solution runByLevel(Solution solution, SokoMap sokoMap)
    {
        
        // 获取现在能走的走法列表
        LinkedList<Solution> nextSolution = SolutionFactory.getNextSolution(solution, sokoMap);
        
        if (success != null)
        {
            Solution slover = success;
            success = null;
            return slover;
        }
        
        if (nextSolution == null)
        {
            return null;
        }
        // 每走一步，获取下一步的走法列表，不断循环
        int level = 1;
        while (!nextSolution.isEmpty())
        {
            
            Logger.info("开始走第" + level + "层分支，有走法：" + nextSolution.size());
            
            // 下一步的走法列表
            Solution solver = SolutionFactory.loopNextSolutionsBatch(nextSolution, sokoMap);
            
            if (solver != null)
            {
                success = null;
                return solver;
            }
            
            level++;
        }
        
        return null;
    }
    
    /**
     * 获取步骤后的箱子位置列表
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ArrayList<Zuobiao> getBoxListAfter(Solution solu, SokoMap sokoMap)
    {
        // 若是根节点，直接返回
        Solution root = solu.getLastSolution();
        if (null == root)
        {
            return Util.cloneBoxList(sokoMap.getBoxList());
        }
        
        ArrayList<Solution> solutList = new ArrayList<Solution>();
        
        solutList.add(solu);
        
        while (root.getLastSolution() != null)
        {
            solutList.add(0, root);
            root = root.getLastSolution();
        }
        
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(sokoMap.getBoxList());
        
        for (int i = 0; i < solutList.size(); i++)
        {
            int moveIndex = solutList.get(i).getBoxIndex();
            Zuobiao moveBox = cloneBoxList.get(moveIndex);
            moveBox.moveByAspect(solutList.get(i).getStep());
        }
        return cloneBoxList;
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
        return draw(getManBeforeStep(solu, sokoMap),
            getBoxListBefore(solu, sokoMap),
            solu.getStep(),
            sokoMap);
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
        Util.replaceZuobiao(builder, man, step == null ? "a" : step.getDesc(),sokoMap.getMax_x());
        
        for (Zuobiao box : boxList)
        {
            Util.replaceZuobiao(builder, box, "B",sokoMap.getMax_x());
        }
        
        return Util.drawMap(builder, sokoMap.getMax_x());
    }
    
    /**
     * 获取箱子列表和人，人的位置放在0
     * 
     * @return
     * @see [类、类#方法、类#成员]
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
        
        ArrayList<Solution> solutList = new ArrayList<Solution>();
        
        solutList.add(solu);
        
        while (root.getLastSolution() != null)
        {
            solutList.add(0, root);
            root = root.getLastSolution();
        }
        
        Zuobiao man = null;
        int size = solutList.size();
        for (int i = 0; i < size; i++)
        {
            Solution solution = solutList.get(i);
            int moveIndex = solution.getBoxIndex();
            Zuobiao moveBox = cloneBoxList.get(moveIndex);
            moveBox.moveByAspect(solution.getStep());
            if ((i + 1) == size)
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
