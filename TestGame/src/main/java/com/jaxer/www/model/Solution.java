package com.jaxer.www.model;

import com.jaxer.www.enums.AspectEnum;

import java.util.ArrayList;

/**
 * 走法
 * 
 * @version [版本号, 2015年9月19日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Solution
{
    private int uesTime = 0;
    
    public void addTime()
    {
        uesTime++;
    }
    
    public boolean isHot()
    {
        return uesTime > 10 * 10000;
    }
    
    private ArrayList<Zuobiao> boxList;
    
    /**
     * @return 返回 boxList
     */
    public ArrayList<Zuobiao> getBoxList()
    {
        return boxList;
    }
    
    /**
     *  对boxList进行赋值
     */
    public void setBoxList(ArrayList<Zuobiao> boxList)
    {
        this.boxList = boxList;
    }
    
    // 移动的box的序号
    private int boxIndex = -1;
    
    private Solution lastSolution;
    
    private int boxsNotGole;
    
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
    
    public Solution(AspectEnum step, int boxIndex, Solution lastSolution,
        int boxsNotGole)
    {
        this(step, boxIndex, lastSolution);
        this.boxsNotGole = boxsNotGole;
    }
    
    public int getBoxIndex()
    {
        return boxIndex;
    }
    
    /**
     * @return 返回 lastSolution
     */
    public Solution getLastSolution()
    {
        return lastSolution;
    }
    
    public AspectEnum getStep()
    {
        return this.step;
    }
    
    /**
     * @return 返回 boxsNotGole
     */
    public int getBoxsNotGole()
    {
        return boxsNotGole;
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
        builder.append("L");
        builder.append(level);
        builder.append(",left");
        builder.append(boxsNotGole);
        // builder.append(getBoxListBefore().get(boxIndex));
        builder.append(",");
        builder.append(step.getDesc());
        return builder.toString();
    }
    
}
