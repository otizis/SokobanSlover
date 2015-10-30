package com.jaxer.www.manager;

import java.util.LinkedList;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.model.Solution;

public class SolutionManager
{
    private static Solution success;
    
    /**
     * @return ���� success
     */
    public static Solution getSuccess()
    {
        return success;
    }
    
    /**
     * @param ��success���и�ֵ
     */
    public static void setSuccess(Solution success)
    {
        SolutionManager.success = success;
    }
    
    /**
     * һ��һ����������߷��������߷�
     * 
     * @return ���ɹ��Ľⷨ���޽ⷵ��null
     * @see [�ࡢ��#��������#��Ա]
     */
    public static Solution runByLevel(Solution solution)
    {
        
        // ��ȡ�������ߵ��߷��б�
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
        // ÿ��һ������ȡ��һ�����߷��б�����ѭ��
        int level = 1;
        while (!nextSolution.isEmpty())
        {
            
            Logger.info("��ʼ�ߵ�" + level + "���֧�����߷���" + nextSolution.size());
            
            // ��һ�����߷��б�
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
