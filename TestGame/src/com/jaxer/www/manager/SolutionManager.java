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
    /**
     * һ��һ����������߷��������߷�
     * 
     * @return ���ɹ��Ľⷨ���޽ⷵ��null
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void runLeft(SokoMap sokoMap)
    {
        
        // ��ȡ�������ߵ��߷��б�
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
        // ÿ��һ������ȡ��һ�����߷��б�����ѭ��
        while (!nextSolution.isEmpty())
        {
            
            Logger.info("��ʼ���ݴ�ĵ�" + nextSolution.getFirst().level + "���֧�����߷���" + nextSolution.size());
            
            // ��һ�����߷��б�
            SolutionFactory.loopNextSolutionsBatch(nextSolution, sokoMap);
            
            if (sokoMap.getSuccess() != null)
            {
                return;
            }
            
        }
        
    }
    
    /**
     * һ��һ����������߷��������߷�
     * 
     * @return ���ɹ��Ľⷨ���޽ⷵ��null
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void runByLevel(SokoMap sokoMap)
    {
        
        // ��ȡ�������ߵ��߷��б�
        LinkedList<Solution> nextSolution = SolutionFactory.getNextSolution(new Solution(), sokoMap);
        
        if (sokoMap.getSuccess() != null)
        {
            return;
        }
        
        if (nextSolution == null)
        {
            return;
        }
        // ÿ��һ������ȡ��һ�����߷��б�����ѭ��
        int level = 1;
        while (!nextSolution.isEmpty())
        {
                
            if(Logger.isInfo){
                Logger.info("��ʼ�ߵ�" + level + "���֧�����߷���" + nextSolution.size());
            }
            
            // ��һ�����߷��б�
            SolutionFactory.loopNextSolutionsBatch(nextSolution, sokoMap);
            
            if (sokoMap.getSuccess() != null)
            {
                return;
            }
            
            level++;
        }
        
    }
    
    /**
     * ��ȡ����������λ���б�
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static ArrayList<Zuobiao> getBoxListAfter(Solution solu, SokoMap sokoMap)
    {
        // ���Ǹ��ڵ㣬ֱ�ӷ���
        Solution root = solu.getLastSolution();
        if (null == root)
        {
            return Util.cloneBoxList(sokoMap.getBoxList());
        }
        
        if (solu.isHot())
        {
            return Util.cloneBoxList(solu.getBoxList());
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
        solu.addTime();
        if (solu.isHot())
        {
            solu.setBoxList(Util.cloneBoxList(cloneBoxList));
        }
        return cloneBoxList;
    }
    
    /**
     * ���ز����ƶ�ǰ�ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static String drawAfter(Solution solu, SokoMap sokoMap)
    {
        return draw(getManAfterStep(solu, sokoMap), getBoxListAfter(solu, sokoMap), solu.getStep(), sokoMap);
    }
    
    /**
     * ���ز����ƶ�ǰ�ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static String drawBefore(Solution solu, SokoMap sokoMap)
    {
        return draw(getManBeforeStep(solu, sokoMap), getBoxListBefore(solu, sokoMap), solu.getStep(), sokoMap);
    }
    
    /**
     * ���ݴ�������λ�ã�������λ�û�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
     * ��ȡ�����б���ˣ��˵�λ�÷���0
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static ArrayList<Zuobiao> getBoxAndManAfter(Solution solu, SokoMap sokoMap)
    {
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(sokoMap.getBoxList());
        
        // ���Ǹ��ڵ㣬ֱ�ӷ���
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
     * ��ȡ���߷�ǰ�ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
            // ��ʼλ��
            return sokoMap.getMan();
        }
        // �������ӵ�λ�ã�����������˵�λ��
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
