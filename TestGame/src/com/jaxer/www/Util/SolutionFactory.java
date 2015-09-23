package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.ProgressCounter;
import com.jaxer.www.model.Result;
import com.jaxer.www.model.Solution;

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
        
        Cell[][] curMap = solu.getSokoMapAfter();
        if (solu == null || curMap == null)
        {
            return null;
        }
        Logger.debug("======��ʼ�����֧�߷���");
        
        ArrayList<Cell> statues = getAllStatues(curMap);
        
        HashSet<Cell> playerCanGoCells = getAllPlayerCanGoCells(curMap);
        
        LinkedList<Solution> solutions = new LinkedList<Solution>();
        
        // ����
        for (int i = 0; i < statues.size(); i++)
        {
            Cell statue = statues.get(i);
            int x = statue.getX();
            int y = statue.getY();
            
            // �Ƿ��������ƶ�
            if ((y >= 1 && (y + 1) < curMap[x].length)
                && curMap[x][y + 1].canMoveIn() && curMap[x][y - 1].canMoveIn())
            {
                // �ƶ�ʱ��վ�˵�λ�����ܹ�ȥ����Ŀ��λ��������
                if (playerCanGoCells.contains(curMap[x][y - 1])
                    && DeadPoitUtil.isPointNeedGo(x, y + 1))
                {
                    solutions.add(new Solution(AspectEnum.down, x, y, solu));
                    
                }
                if (playerCanGoCells.contains(curMap[x][y + 1])
                    && DeadPoitUtil.isPointNeedGo(x, y - 1))
                {
                    solutions.add(new Solution(AspectEnum.up, x, y, solu));
                }
            }
            
            // �ܷ������ƶ�
            if (x >= 1 && (x + 1) < curMap.length
                && curMap[x + 1][y].canMoveIn() && curMap[x - 1][y].canMoveIn())
            {
                
                if (playerCanGoCells.contains(curMap[x - 1][y])
                    && DeadPoitUtil.isPointNeedGo(x + 1, y))
                {
                    solutions.add(new Solution(AspectEnum.right, x, y, solu));
                    
                }
                if (playerCanGoCells.contains(curMap[x + 1][y])
                    && DeadPoitUtil.isPointNeedGo(x - 1, y))
                {
                    solutions.add(new Solution(AspectEnum.left, x, y, solu));
                    
                }
                
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
    public static HashSet<Cell> getAllPlayerCanGoCells(Cell[][] curMap)
    {
        // ��������ڵ�λ�÷���set��
        HashSet<Cell> cells = new HashSet<Cell>();
        for (int i = 0; i < curMap.length; i++)
        {
            for (int j = 0; j < curMap[0].length; j++)
            {
                if (curMap[i][j].getItem() == ItemType.player)
                {
                    cells.add(curMap[i][j]);
                    break;
                }
            }
            if (!cells.isEmpty())
            {
                break;
            }
        }
        
        HashSet<Cell> hasLoop = new HashSet<Cell>();
        HashSet<Cell> temp = new HashSet<Cell>();
        boolean plusCellFlag = false;
        do
        {
            plusCellFlag = false;
            Iterator<Cell> iterator = cells.iterator();
            while (iterator.hasNext())
            {
                
                Cell cell = (Cell)iterator.next();
                // �Ѿ����ڵ�����
                if (hasLoop.contains(cell))
                {
                    continue;
                }
                else
                {
                    hasLoop.add(cell);
                }
                int x = cell.getX();
                int y = cell.getY();
                if (y > 0)
                {
                    Cell upCell = curMap[x][y - 1];
                    if (upCell.getItem() == ItemType.empty)
                    {
                        temp.add(upCell);
                        plusCellFlag = true;
                    }
                }
                if (x > 0)
                {
                    Cell leftCell = curMap[x - 1][y];
                    if (leftCell.getItem() == ItemType.empty)
                    {
                        temp.add(leftCell);
                        plusCellFlag = true;
                    }
                }
                if (y < (curMap[0].length - 1))
                {
                    Cell downCell = curMap[x][y + 1];
                    if (downCell.getItem() == ItemType.empty)
                    {
                        temp.add(downCell);
                        plusCellFlag = true;
                    }
                }
                if (x < (curMap.length - 1))
                {
                    Cell rightCell = curMap[x + 1][y];
                    if (rightCell.getItem() == ItemType.empty)
                    {
                        temp.add(rightCell);
                        plusCellFlag = true;
                    }
                }
            }
            
            cells.addAll(temp);
            temp.clear();
            
        } while (plusCellFlag);
        
        return cells;
    }
    
    /**
     * ��ȡ��ͼ�����еĵ����б�
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private static ArrayList<Cell> getAllStatues(Cell[][] curMap)
    {
        ArrayList<Cell> statues = new ArrayList<Cell>();
        for (int i = 0; i < curMap.length; i++)
        {
            for (int j = 0; j < curMap[0].length; j++)
            {
                if (curMap[i][j].isStatue())
                {
                    statues.add(curMap[i][j]);
                }
            }
        }
        return statues;
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
            solu.play(solu.getSokoMapBefor());
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
