package com.jaxer.www;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
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
    public static ArrayList<Solution> getNextSolution(Solution lastSolu)
    {
        
        if (lastSolu == null || lastSolu.getThisStepMap() == null)
        {
            return null;
        }
        Cell[][] curMap = lastSolu.getThisStepMap();
        
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
            if ((y >= 1 && (y + 1) < curMap[x].length))
            {
                if (curMap[x][y - 1].getItem() == ItemType.empty
                    && playerCanGoCells.contains(curMap[x][y - 1]))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x][y - 1].setPlayer();
                    solutions.add(new Solution(cloneMap, AspectEnum.down, x, y,
                        lastSolu));
                        
                }
                if (curMap[x][y + 1].getItem() == ItemType.empty
                    && playerCanGoCells.contains(curMap[x][y + 1]))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x][y + 1].setPlayer();
                    solutions.add(
                        new Solution(cloneMap, AspectEnum.up, x, y, lastSolu));
                }
            }
            
            if (x >= 1 && (x + 1) < curMap.length)
            {
                
                if (curMap[x - 1][y].getItem() == ItemType.empty
                    && playerCanGoCells.contains(curMap[x - 1][y]))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x - 1][y].setPlayer();
                    solutions.add(new Solution(cloneMap, AspectEnum.right, x, y,
                        lastSolu));
                        
                }
                if (curMap[x + 1][y].getItem() == ItemType.empty
                    && playerCanGoCells.contains(curMap[x + 1][y]))
                {
                    Cell[][] cloneMap = Util.cloneObject(curMap);
                    cloneMap[x + 1][y].setPlayer();
                    solutions.add(new Solution(cloneMap, AspectEnum.left, x, y,
                        lastSolu));
                        
                }
                
            }
            
        }
        
        Solution.sort(solutions);
        if (Util.isDebugEnable())
        {
            Util.debug("�������ķ�֧��");
            int a = 1;
            for (Solution solution : solutions)
            {
                Util.debug("��ţ�" + a++);
                Util.debug(solution.toString());
            }
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
        HashSet<Cell> cells = new HashSet<Cell>();
        for (int i = 0; i < curMap.length; i++)
        {
            for (int j = 0; j < curMap[0].length; j++)
            {
                if (curMap[i][j].getItem() == ItemType.player)
                {
                    curMap[i][j].setItem(ItemType.empty);
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
     * ��ȡ�ı��Ǳ߽��ǽ������ <������ϸ����>
     * 
     * @param curMap
     * @param x
     * @param y
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private static int getStayNum(Cell[][] curMap, int x, int y)
    {
        int cantStayNum = 0;
        if (x == 0 || x == (curMap.length - 1))
        {
            cantStayNum++;
        }
        else
        {
            if (curMap[x - 1][y].getItem() == ItemType.wall)
            {
                cantStayNum++;
                
            }
            if (curMap[x + 1][y].getItem() == ItemType.wall)
            {
                cantStayNum++;
                
            }
        }
        if (y == 0 || y == (curMap[0].length - 1))
        {
            cantStayNum++;
        }
        else
        {
            if (curMap[x][y - 1].getItem() == ItemType.wall)
            {
                cantStayNum++;
                
            }
            if (curMap[x][y + 1].getItem() == ItemType.wall)
            {
                cantStayNum++;
                
            }
        }
        return cantStayNum;
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
    
    public static boolean isdead(Solution solution)
    {
        if (solution == null || solution.getThisStepMap() == null)
        {
            return true;
        }
        Cell[][] curMap = solution.getThisStepMap();
        
        ArrayList<Cell> statues = getAllStatues(curMap);
        for (int i = 0; i < statues.size(); i++)
        {
            Cell statue = statues.get(i);
            int x = statue.getX();
            int y = statue.getY();
            if (x == 0 || (x + 1) == curMap.length || y == 0
                || (y + 1) == curMap[0].length)
            {
                return true;
            }
            
            // ��ǽ���ж�
            int stayNum = getStayNum(curMap, x, y);
            if (stayNum >= 2)
            {
                return true;
            }
            
        }
        
        return false;
    }
}
