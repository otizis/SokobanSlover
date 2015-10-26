package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.enums.Result;
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
        
        ArrayList<Zuobiao> boxs = solu.getBoxListAfter();
        
        HashSet<Zuobiao> playerCanGoCells = getAllPlayerCanGoCells(solu);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // ����ÿһ���������ߵĲ���
        for (int i = 0; i < boxs.size(); i++)
        {
            Zuobiao box = boxs.get(i);
            
            // ������������
            for (AspectEnum aspce : AspectEnum.values())
            {
                
                Zuobiao zuobiaoGo = ZuobiaoUtil.getMove(box, aspce);
                if (!canGo(solu, zuobiaoGo))
                {
                    continue;
                }
                // Ŀ��λ��������
                if (!DeadPoitUtil.isPointNeedGo(zuobiaoGo))
                {
                    continue;
                }
                
                // �ƶ�ʱ��վ�˵�λ�����ܹ�ȥ
                Zuobiao zuobiaoMan = ZuobiaoUtil.getMovePlayer(box, aspce);
                if (!playerCanGoCells.contains(zuobiaoMan))
                {
                    continue;
                }
                
                solutions.add(new Solution(aspce, i, solu));
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
    
    /**
     * ��ȡ������ƶ�����λ��
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static HashSet<Zuobiao> getAllPlayerCanGoCells(Solution solu)
    {
        // ��������ڵ�λ�÷���set��
        HashSet<Zuobiao> cellsPlayerCanGo = new HashSet<Zuobiao>();
        cellsPlayerCanGo.add(solu.getManAfterStep());
        
        HashSet<Zuobiao> hasLoop = new HashSet<Zuobiao>();
        HashSet<Zuobiao> temp = new HashSet<Zuobiao>();
        boolean plusCellFlag = false;
        do
        {
            plusCellFlag = false;
            Iterator<Zuobiao> iterator = cellsPlayerCanGo.iterator();
            while (iterator.hasNext())
            {
                
                Zuobiao cell = (Zuobiao)iterator.next();
                // �Ѿ����ڵ�����
                if (hasLoop.contains(cell))
                {
                    continue;
                }
                else
                {
                    hasLoop.add(cell);
                }
                // ������������
                for (AspectEnum aspce : AspectEnum.values())
                {
                    
                    Zuobiao afterMove = ZuobiaoUtil.getMove(cell, aspce);
                    if (canGo(solu, afterMove))
                    {
                        temp.add(afterMove);
                        plusCellFlag = true;
                    }
                }
            }
            
            cellsPlayerCanGo.addAll(temp);
            temp.clear();
            
        } while (plusCellFlag);
        
        if (Logger.isdebug)
        {
            StringBuilder mapStr = Util.mapStr();
            for (Zuobiao zuobiao : solu.getBoxListAfter())
            {
                Util.replaceZuobiao(mapStr, zuobiao, "B");
            }
            for (Zuobiao zuobiao : cellsPlayerCanGo)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "a");
            }
            
            Logger.info(Util.drawMap(mapStr));
        }
        return cellsPlayerCanGo;
    }
    
    /**
     * �ⷨ�У���ͼ����Ϊ�գ��Ҳ���box����֮һ������true
     * 
     * 
     * @param solu
     * @param gotoCell
     * @see [�ࡢ��#��������#��Ա]
     */
    private static boolean canGo(Solution solu, Zuobiao gotoZuobiao)
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
        for (Zuobiao box : solu.getBoxListAfter())
        {
            if (box.equals(gotoZuobiao))
            {
                return false;
            }
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
    public static LinkedList<Solution> getNextSolutionsBatch(
        LinkedList<Solution> needSub)
    {
        LinkedList<Solution> nextSolutionList = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(needSub.size(), "��ȡ��һ���߷�");
        while (!needSub.isEmpty())
        {
            pc.addProgress();
            
            Solution removeFirst = needSub.removeFirst();
            
            LinkedList<Solution> temp =
                SolutionFactory.getNextSolution(removeFirst);
                
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
                Logger.debug("=============");
            }
        }
        return nextSolutionList;
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
            
        // ÿ��һ������ȡ��һ�����߷��б�����ѭ��
        int level = 1;
        while (!nextSolution.isEmpty())
        {
            
            Logger.info("��ʼ�ߵ�" + level + "���֧�����߷���" + nextSolution.size());
            
            // ʵʩ�߷�
            LinkedList<Solution> needSub =
                SolutionFactory.getSoultionNeedSub(nextSolution);
                
            // �гɹ�, �ж�����һ��ѭ��
            if (needSub.size() == 1
                && needSub.get(0).getResult() == Result.success)
            {
                return needSub.get(0);
            }
            
            // ��һ�����߷��б�
            nextSolution = SolutionFactory.getNextSolutionsBatch(needSub);
            
            level++;
        }
        
        if (Logger.isdebug)
        {
            Util.printMapSet();
        }
        return null;
    }
    
    /**
     * ��ÿһ���߷��������ߣ������߳ɹ����߷�
     * 
     * @param solutions
     * @return
     */
    public static LinkedList<Solution> getSoultionNeedSub(
        LinkedList<Solution> solutions)
    {
        LinkedList<Solution> needSub = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(solutions.size(), "�����߷��б�");
        
        while (!solutions.isEmpty())
        {
            pc.addProgress();
            Solution solu = solutions.removeFirst();
            
            solu.play();
            
            if (solu.getResult() == Result.success)
            {
                needSub.clear();
                needSub.add(solu);
                pc.end();
                break;
            }
            if (solu.getResult() == Result.needsub)
            {
                needSub.add(solu);
            }
            
        }
        return needSub;
    }
}
