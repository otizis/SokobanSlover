package com.jaxer.www.model;

import java.util.ArrayList;

import com.jaxer.www.Util.Util;
import com.jaxer.www.Util.ZuobiaoUtil;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.Result;

/**
 * 走法
 * 
 * @author yejiangtao
 * @version [版本号, 2015年9月19日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Solution
{
    // 移动的box的序号
    private int boxIndex = -1;
    
    private Solution lastSolution;
    
    public int level;
    
    private AspectEnum step;
    
    public Solution()
    {
        super();
    }
    
    public Solution(AspectEnum step, int boxIndex, Solution lastSolution)
    {
        super();
        this.step = step;
        this.boxIndex = boxIndex;
        this.lastSolution = lastSolution;
        if (lastSolution != null)
        {
            this.level = lastSolution.level + 1;
        }
    }
    
    /**
     * 根据传入的玩家位置，和箱子位置绘图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String draw(Zuobiao man, ArrayList<Zuobiao> boxList)
    {
        StringBuilder builder = Util.mapStr();
        Util.replaceZuobiao(builder, man, step == null ? "a" : step.getDesc());
        
        for (Zuobiao box : boxList)
        {
            Util.replaceZuobiao(builder, box, "B");
        }
        
        return Util.drawMap(builder);
    }
    
    /**
     * 返回步数推动前的地图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String drawAfter()
    {
        return draw(getManAfterStep(), getBoxListAfter());
    }
    
    /**
     * 返回步数推动前的地图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String drawBefore()
    {
        return draw(getManBeforeStep(), getBoxListBefore());
    }
    
    public int getBoxIndex()
    {
        return boxIndex;
    }
    
    /**
     * 获取步骤后的箱子位置列表
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<Zuobiao> getBoxListAfter()
    {
        // 若是根节点，直接返回
        Solution root = lastSolution;
        if (null == root)
        {
            return Util.cloneBoxList(SokoMap.boxList);
        }
        
        ArrayList<Solution> solutList = new ArrayList<Solution>();
        
        solutList.add(this);
        
        while (root.lastSolution != null)
        {
            solutList.add(0, root);
            root = root.lastSolution;
        }
        
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(SokoMap.boxList);
        
        for (int i = 0; i < solutList.size(); i++)
        {
            int moveIndex = solutList.get(i).getBoxIndex();
            Zuobiao moveBox = cloneBoxList.get(moveIndex);
            moveBox.moveByAspect(solutList.get(i).step);
        }
        return cloneBoxList;
    }
    
    /**
     * 获取箱子列表和人，人的位置放在0
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    /**
     * <一句话功能简述> <功能详细描述>
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<Zuobiao> getBoxAndManAfter()
    {
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(SokoMap.boxList);
        
        // 若是根节点，直接返回
        Solution root = lastSolution;
        if (null == root)
        {
            cloneBoxList.add(0, SokoMap.man);
            return cloneBoxList;
        }
        
        ArrayList<Solution> solutList = new ArrayList<Solution>();
        
        solutList.add(this);
        
        while (root.lastSolution != null)
        {
            solutList.add(0, root);
            root = root.lastSolution;
        }
        
        Zuobiao man = null;
        int size = solutList.size();
        for (int i = 0; i < size; i++)
        {
            Solution solution = solutList.get(i);
            int moveIndex = solution.getBoxIndex();
            Zuobiao moveBox = cloneBoxList.get(moveIndex);
            moveBox.moveByAspect(solution.step);
            if ((i + 1) == size)
            {
                man = ZuobiaoUtil.getMovePlayer(moveBox, solution.step);
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
    public ArrayList<Zuobiao> getBoxListBefore()
    {
        if (null == lastSolution)
        {
            return Util.cloneBoxList(SokoMap.boxList);
        }
        return lastSolution.getBoxListAfter();
    }
    
    /**
     * @return 返回 lastSolution
     */
    public Solution getLastSolution()
    {
        return lastSolution;
    }
    
    /**
     * @return
     */
    public Zuobiao getManAfterStep()
    {
        if (lastSolution == null)
        {
            // 初始位置
            return SokoMap.man;
        }
        // 现在箱子的位置，就是走完后人的位置
        return getBoxListBefore().get(boxIndex);
    }
    
    /**
     * @return
     */
    public Zuobiao getManBeforeStep()
    {
        if (lastSolution == null)
        {
            return SokoMap.man;
        }
        Zuobiao manAfter = getManAfterStep();
        return ZuobiaoUtil.getMovePlayer(manAfter, step);
    }
    
    public AspectEnum getStep()
    {
        return this.step;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (lastSolution == null)
        {
            return "init one";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getBoxListBefore().get(boxIndex));
        builder.append(", level=");
        builder.append(level);
        builder.append(", step=");
        builder.append(step);
        builder.append("]");
        return builder.toString();
    }
    
}
