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
     * @return 返回 thisStepMap
     */
    public Cell[][] getThisStepMap()
    {
        return thisStepMap;
    }

    /**
     * @param 对thisStepMap进行赋值
     */
    public void setThisStepMap(Cell[][] thisStepMap)
    {
        this.thisStepMap = thisStepMap;
    }
    
    /**
     * 运行算法
     * 
     * @param map
     * @see [类、类#方法、类#成员]
     */
    public Solution run()
    {
        
        Long begin = System.currentTimeMillis();
        DeadPoitUtil.loadDeadSet(thisStepMap);
        
        Solution solution = new Solution(thisStepMap);
        
        Logger.info(solution.toString());
        Solution lastOne = SolutionFactory.runByLevel(solution);
        Long end = System.currentTimeMillis();
        
        System.out.println("耗时：" + (end - begin));
        return lastOne;
        
    }
}
