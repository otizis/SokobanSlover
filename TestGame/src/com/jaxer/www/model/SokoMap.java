package com.jaxer.www.model;

import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.SolutionFactory;

public class SokoMap
{
    private Cell[][] thisStepMap;

    
    public SokoMap(Cell[][] thisStepMap)
    {
        super();
        this.thisStepMap = thisStepMap;
    }

    /**
     * @return ���� thisStepMap
     */
    public Cell[][] getThisStepMap()
    {
        return thisStepMap;
    }

    /**
     * @param ��thisStepMap���и�ֵ
     */
    public void setThisStepMap(Cell[][] thisStepMap)
    {
        this.thisStepMap = thisStepMap;
    }
    
    /**
     * �����㷨
     * 
     * @param map
     * @see [�ࡢ��#��������#��Ա]
     */
    public Solution run()
    {
        
        Long begin = System.currentTimeMillis();
        DeadPoitUtil.loadDeadSet(thisStepMap);
        
        Solution solution = new Solution(thisStepMap);
        
        Logger.info(solution.toString());
        Solution lastOne = SolutionFactory.runByLevel(solution);
        Long end = System.currentTimeMillis();
        
        System.out.println("��ʱ��" + (end - begin));
        return lastOne;
        
    }
}
