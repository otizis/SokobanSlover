package com.jaxer.www.manager;

import java.util.LinkedList;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.model.Solution;

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
    public static Solution runByLevel(Solution solution)
    {
        
        // 获取现在能走的走法列表
        LinkedList<Solution> nextSolution =
            SolutionFactory.getNextSolution(solution);
            
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
            Solution solver =
                SolutionFactory.loopNextSolutionsBatch(nextSolution);
                
            if (solver != null)
            {
                success = null;
                return solver;
            }
            
            level++;
        }
        
        return null;
    }
    
}
