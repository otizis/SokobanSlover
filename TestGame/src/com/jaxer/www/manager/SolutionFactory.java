package com.jaxer.www.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.Util.ZuobiaoUtil;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Solution;
import com.jaxer.www.model.Zuobiao;

public class SolutionFactory
{
    /**
     * �������ڵ��߷��������ȡ���������ܵĲ���
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static LinkedList<Solution> getNextSolution(Solution solu)
    {
        
        if (solu == null)
        {
            return null;
        }
        Logger.debug("======��ʼ������һ���߷���");
        
        ArrayList<Zuobiao> boxList = solu.getBoxAndManAfter();
        
        Zuobiao man = boxList.remove(0);
        
        ArrayList<Zuobiao> playerCanGoCells = getPlayerCanGoCells(boxList, man);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // ����ÿһ���������ߵĲ���
        for (int i = 0; i < boxList.size(); i++)
        {
            Zuobiao box = boxList.get(i);
            
            // ������������
            for (AspectEnum aspect : AspectEnum.values())
            {
                
                // �ƶ�ʱ��վ�˵�λ�����ܹ�ȥ
                Zuobiao zuobiaoMan = ZuobiaoUtil.getMovePlayer(box, aspect);
                if (!playerCanGoCells.contains(zuobiaoMan))
                {
                    continue;
                }
                // Ŀ��λ���Ѿ������ӻ���ǽ����������
                Zuobiao zuobiaoGo = ZuobiaoUtil.getMove(box, aspect);
                if (!canGo(boxList, zuobiaoGo))
                {
                    continue;
                }
                // Ŀ��λ�����ǣ���������
                if (DeadPoitUtil.isPointDie(zuobiaoGo))
                {
                    continue;
                }
                
                // �Ѿ����ڵ�������ͼ����ʹ��
                if (isExist(boxList, box, aspect, playerCanGoCells, zuobiaoGo))
                {
                    Logger.debug("���Ͻ���ظ����������Ž�");
                    continue;
                }
                
                // �ƶ����Ƿ�ɹ�
                box.moveByAspect(aspect);
                if (Util.isAllBoxGoal(boxList))
                {
                    SolutionManager.setSuccess(new Solution(aspect, i, solu));
                    return null;
                }
                box.backByAspect(aspect);
                
                solutions.add(new Solution(aspect, i, solu));
            }
        }
        
        if (Logger.isDebugEnable())
        {
            int num = solutions.size();
            Logger.debug(solu.toString());
            Logger.debug("��������" + num + "�ַ�֧�߷���");
            for (int i = 0; i < num; i++)
            {
                Logger.debug("��ţ�" + (1 + i));
                Logger.debug(solutions.get(i).toString());
                Logger.debug(solutions.get(i).drawBefore());
            }
            Logger.debug("======��ȡ��֧�߷�������");
        }
        
        return solutions;
    }
    
    private static boolean isExist(ArrayList<Zuobiao> boxs, Zuobiao box,
        AspectEnum aspect, ArrayList<Zuobiao> playerCanGoCells,
        Zuobiao zuobiaoGo)
    {
        // �ƶ����ӣ��õ��ƶ�����б������ִ���ԭ
        box.moveByAspect(aspect);
        byte[] boxsStr = Util.coverBox(boxs);
        box.backByAspect(aspect);
        
        // �ƶ�������λվ�ˣ��ƶ�������λ����վ��
        byte[] manStr = null;
        playerCanGoCells.add(box);
        if (playerCanGoCells.contains(zuobiaoGo))
        {
            playerCanGoCells.remove(zuobiaoGo);
            manStr = Util.coverMan(playerCanGoCells, boxsStr);
            playerCanGoCells.add(zuobiaoGo);
        }
        else
        {
            manStr = Util.coverMan(playerCanGoCells, boxsStr);
        }
        playerCanGoCells.remove(box);
        
        return Util.isExist(manStr);
    }
    
    /**
     * ��ȡ������ƶ�����λ��
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static ArrayList<Zuobiao> getPlayerCanGoCells(Solution solu)
    {
        
        return getPlayerCanGoCells(solu.getBoxListAfter(),
            solu.getManAfterStep());
    }
    
    /**
     * ��ȡ������ƶ�����λ��
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static ArrayList<Zuobiao> getPlayerCanGoCells(
        ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        // ��ͼ�п���վ�˵�����
        HashSet<Zuobiao> emptyList = new HashSet<>(SokoMap.manCanGoCells);
        
        // ���Ƴ������б��λ��
        emptyList.removeAll(boxList);
        
        ArrayList<Zuobiao> canGoList = new ArrayList<Zuobiao>();
        // ��������ڵ�λ�÷������ȥ��set��
        canGoList.add(man);
        emptyList.remove(man);
        
        // ������������
        int count = 1;
        // ��ʼ������
        int start = 0;
        while (count > 0)
        {
            int addTemp = 0;
            // ѭ����һ��ѭ�����ӵ����꣬�����Ƿ��ǿ�
            while (count-- > 0)
            {
                Zuobiao canGo = canGoList.get(start++);
                for (AspectEnum asp : AspectEnum.values())
                {
                    Zuobiao manEdge = ZuobiaoUtil.getMove(canGo, asp);
                    
                    if (emptyList.contains(manEdge))
                    {
                        emptyList.remove(manEdge);
                        canGoList.add(manEdge);
                        addTemp++;
                    }
                }
            }
            count = addTemp;
        }
        
        if (Logger.isdebug)
        {
            StringBuilder mapStr = Util.mapStr();
            for (Zuobiao zuobiao : boxList)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "B");
            }
            for (Zuobiao zuobiao : canGoList)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "a");
            }
            
            Logger.info(Util.drawMap(mapStr));
        }
        
        return canGoList;
    }
    
    /**
     * �ⷨ�У���ͼ����Ϊ�գ��Ҳ���box����֮һ������true
     * 
     * 
     * @param solu
     * @param gotoCell
     * @see [�ࡢ��#��������#��Ա]
     */
    private static boolean canGo(ArrayList<Zuobiao> boxList,
        Zuobiao gotoZuobiao)
    {
        if (gotoZuobiao == null)
        {
            return false;
        }
        // Ŀ��λ��������ǽ
        if (SokoMap.getCell(gotoZuobiao).check(CellType.wall))
        {
            return false;
            
        }
        // Ŀ��λ��������
        if (boxList.contains(gotoZuobiao))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * ������ȡ�߷�����һ���߷���
     * 
     * @param needSub
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static Solution loopNextSolutionsBatch(LinkedList<Solution> needSub)
    {
        
        LinkedList<Solution> nextSolutionList = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(needSub.size(), "��ȡ��һ���߷�");
        while (!needSub.isEmpty())
        {
            pc.addProgress();
            
            Solution removeFirst = needSub.removeFirst();
            
            LinkedList<Solution> temp =
                SolutionFactory.getNextSolution(removeFirst);
                
            removeFirst = null;
            
            if (SolutionManager.getSuccess() != null)
            {
                return SolutionManager.getSuccess();
            }
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
                Logger.debug("=============");
            }
        }
        
        needSub.addAll(nextSolutionList);
        return null;
    }
    
}
