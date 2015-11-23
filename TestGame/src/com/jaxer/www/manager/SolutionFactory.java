package com.jaxer.www.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.model.FastSet;
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
    public static LinkedList<Solution> getNextSolution(Solution solu, SokoMap sokoMap)
    {
        
        if (solu == null)
        {
            return null;
        }
        Logger.debug("======��ʼ������һ���߷���");
        
        ArrayList<Zuobiao> boxList = SolutionManager.getBoxAndManAfter(solu, sokoMap);
        
        Zuobiao man = boxList.remove(0);
        
        FastSet playerCanGoCells = sokoMap.getPlayerCanGoCells(boxList, man);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // ����ÿһ���������ߵĲ���
        for (int i = 0; i < boxList.size(); i++)
        {
            Zuobiao box = boxList.get(i);
            
            // ������������
            for (AspectEnum aspect : AspectEnum.getAllEnum())
            {
                
                // �ƶ�ʱ��վ�˵�λ�����ܹ�ȥ
                Zuobiao zuobiaoMan = sokoMap.getMovePlayer(box, aspect);
                if (!playerCanGoCells.contains(zuobiaoMan))
                {
                    continue;
                }
                
                // Ŀ��λ���Ѿ������ӻ���ǽ����������
                Zuobiao zuobiaoGo = sokoMap.getMove(box, aspect);
                if (!sokoMap.canGo(boxList, zuobiaoGo))
                {
                    continue;
                }
                // Ŀ��λ�����㣬��������
                if (sokoMap.isPointDie(zuobiaoGo))
                {
                    continue;
                }
                
                box.moveByAspect(aspect);
                // �Ѿ����ڵ�������ͼ����ʹ��
                if (sokoMap.isExist(boxList, zuobiaoMan))
                {
                    Logger.debug("���Ͻ���ظ����������Ž�");
                    box.backByAspect(aspect);
                    continue;
                }
                
                // �ƶ����Ƿ��������������Χ
                if (Util.checkRound(boxList, sokoMap))
                {
                    box.backByAspect(aspect);
                    continue;
                }

                
                // �ƶ����Ƿ�ɹ�
                int less = Util.boxsNumNotGole(boxList, sokoMap);
                if (less == 0)
                {
                    Solution s = new Solution(aspect, i, solu);
                    if (sokoMap.getSuccess() == null)
                    {
                        sokoMap.setSuccess(s);
                        return null;
                    }
                    if (s.level < sokoMap.getSuccess().level)
                    {
                        sokoMap.setSuccess(s);
                        return null;
                    }
                }
                box.backByAspect(aspect);
                
                solutions.add(new Solution(aspect, i, solu, less));
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
                // Logger.debug(solutions.get(i).drawBefore());
            }
            Logger.debug("======��ȡ��֧�߷�������");
        }
        
        return solutions;
    }
    
    /**
     * ������ȡ�߷�����һ���߷���
     * 
     * @param needSub
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void loopNextSolutionsBatch(LinkedList<Solution> needSub, SokoMap sokoMap)
    {
        Collections.sort(needSub, new Comparator<Solution>()
        {
            
            @Override
            public int compare(Solution o1, Solution o2)
            {
                return o1.getBoxsNotGole() - o2.getBoxsNotGole();
            }
            
        });
        LinkedList<Solution> nextSolutionList = new LinkedList<Solution>();
        
        ProgressCounter pc = new ProgressCounter(needSub.size(), "��ȡ��һ���߷�");
        int maxCount = 100 * 10000;
        while (!needSub.isEmpty())
        {
            pc.addProgress();
            
            Solution removeFirst = needSub.removeFirst();
            
            LinkedList<Solution> temp = SolutionFactory.getNextSolution(removeFirst, sokoMap);
            
            removeFirst = null;
            
            if (sokoMap.getSuccess() != null)
            {
                return;
            }
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
            }
            
        }
        
        int all = nextSolutionList.size();
        if (all < 500000)
        {
            needSub.addAll(nextSolutionList);
            return;
        }
        
        int level = 1;
        while (level <= sokoMap.getGoleList().size())
        {
            Iterator<Solution> iterator = nextSolutionList.iterator();
            int count = 0;
            while (iterator.hasNext())
            {
                Solution next = iterator.next();
                
                if (next.getBoxsNotGole() == level)
                {
                    needSub.add(next);
                    count++;
                    if (count > maxCount)
                    {
                        break;
                    }
                }
            }
            Logger.info(all + "����" + level + "Ŀ��յ�" + count + "��,ռ" + Math.round(count * 100.0 / all) + "%");
            if (count > maxCount)
            {
                break;
            }
            level++;
        }
        
        // ���泬����
        if (sokoMap.getNextSolutionList() == null)
        {
            
            sokoMap.setNextSolutionList(nextSolutionList);
        }
    }
    
}
