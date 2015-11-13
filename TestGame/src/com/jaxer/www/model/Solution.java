package com.jaxer.www.model;

import com.jaxer.www.enums.AspectEnum;

/**
 * �߷�
 * 
 * @version [�汾��, 2015��9��19��]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class Solution
{
    // �ƶ���box�����
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
    
    public Solution(AspectEnum step, int boxIndex, Solution lastSolution, int boxsNotGole)
    {
        this(step, boxIndex, lastSolution);
        this.boxsNotGole = boxsNotGole;
    }
    
    public int getBoxIndex()
    {
        return boxIndex;
    }
    
    /**
     * @return ���� lastSolution
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
     * @return ���� boxsNotGole
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
        // builder.append(getBoxListBefore().get(boxIndex));
        builder.append(",");
        builder.append(step.getDesc());
        return builder.toString();
    }
    
}
