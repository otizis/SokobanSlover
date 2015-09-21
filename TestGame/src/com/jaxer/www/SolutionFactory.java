package com.jaxer.www;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
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
    public static ArrayList<Solution> getNextSolution(Solution solu)
    {
        
        if (solu == null || solu.getThisStepMap() == null)
        {
            return null;
        }
        Logger.debug("======��ʼ�����֧�߷���");
        Cell[][] curMap = solu.getThisStepMap();
        
        ArrayList<Cell> statues = getAllStatues(curMap);
        
        HashSet<Cell> playerCanGoCells = getAllPlayerCanGoCells(curMap);
        
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        
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
                    Cell[][] cloneMap = Util.cloneMapClearPlayer(curMap);
                    cloneMap[x][y - 1].setPlayer();
                    solutions.add(
                        new Solution(cloneMap, AspectEnum.down, x, y, solu));
                        
                }
                if (playerCanGoCells.contains(curMap[x][y + 1])
                    && DeadPoitUtil.isPointNeedGo(x, y - 1))
                {
                    Cell[][] cloneMap = Util.cloneMapClearPlayer(curMap);
                    cloneMap[x][y + 1].setPlayer();
                    solutions
                        .add(new Solution(cloneMap, AspectEnum.up, x, y, solu));
                }
            }
            
            // �ܷ������ƶ�
            if (x >= 1 && (x + 1) < curMap.length
                && curMap[x + 1][y].canMoveIn() && curMap[x - 1][y].canMoveIn())
            {
                
                if (playerCanGoCells.contains(curMap[x - 1][y])
                    && DeadPoitUtil.isPointNeedGo(x + 1, y))
                {
                    Cell[][] cloneMap = Util.cloneMapClearPlayer(curMap);
                    cloneMap[x - 1][y].setPlayer();
                    solutions.add(
                        new Solution(cloneMap, AspectEnum.right, x, y, solu));
                        
                }
                if (playerCanGoCells.contains(curMap[x + 1][y])
                    && DeadPoitUtil.isPointNeedGo(x - 1, y))
                {
                    Cell[][] cloneMap = Util.cloneMapClearPlayer(curMap);
                    cloneMap[x + 1][y].setPlayer();
                    solutions.add(
                        new Solution(cloneMap, AspectEnum.left, x, y, solu));
                        
                }
                
            }
            
        }
        
        Solution.sort(solutions);
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
    public static ArrayList<Solution> getNextSolutionsBatch(
        ArrayList<Solution> needSub)
    {
        ArrayList<Solution> nextSolutionList = new ArrayList<Solution>();
        for (Solution solu : needSub)
        {
            if (solu.getKey().startsWith(
                "0[21][0][2][1][0][0][4][4][6][1][1][0][2][3][1][1][1][1][0][1][1][4][2][2][0]"))
            {
                System.out.println();
            }
            ArrayList<Solution> temp = SolutionFactory.getNextSolution(solu);
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
            }
            Logger.debug("=============");
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
        Logger.debug(solution.toString());
        // ��ȡ�������ߵ��߷��б�
        ArrayList<Solution> nextSolution =
            SolutionFactory.getNextSolution(solution);
            
        // ÿ��һ������ȡ��һ�����߷��б�����ѭ��
        int level = 1;
        while (!nextSolution.isEmpty())
        {
            
            Logger.info("��ʼ�ߵ�" + level + "���֧�����߷���" + nextSolution.size());
            
            // ʵʩ�߷�
            ArrayList<Solution> needSub =
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
        return null;
    }
    
    /**
     * ��ÿһ���߷��������ߣ������߳ɹ����߷�
     * 
     * @param solutions
     * @return
     */
    public static ArrayList<Solution> getSoultionNeedSub(
        ArrayList<Solution> solutions)
    {
        ArrayList<Solution> needSub = new ArrayList<Solution>();
        
        for (Solution solu : solutions)
        {
            solu.play();
            if (solu.getResult() == Result.success)
            {
                needSub.clear();
                needSub.add(solu);
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
