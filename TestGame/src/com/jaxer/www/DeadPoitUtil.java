package com.jaxer.www;

import java.util.HashSet;

import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Solution;

public class DeadPoitUtil
{
    static boolean load = true;
    
    public static boolean isNeedLoadDeadSet()
    {
        return load;
    }
    
    static HashSet<String> deadSet = new HashSet<String>();
    
    public static void loadDeadSet(Cell[][] curMap)
    {
        Logger.info("==��ʼ��������==");
        load = false;
        for (Cell[] cells : curMap)
        {
            for (Cell cell : cells)
            {
                if (cell.isGole() || cell.getItem() == ItemType.wall
                    || cell.getItem() == ItemType.statue)
                {
                    continue;
                }
                int x = cell.getX();
                int y = cell.getY();
                
                if (isPointDead(curMap, x, y))
                {
                    deadSet.add(getKey(x, y));
                    continue;
                }
                
                if (smartDeadPoint(curMap, x, y))
                {
                    deadSet.add(getKey(x, y));
                    continue;
                }
            }
        }
        Logger.info("==������������==");
    }
    
    private static String getKey(int x, int y)
    {
        return x + "," + y;
    }
    
    /**
     * �õ�λ�Ƿ�������,ǽ��
     * 
     * @param x
     * @param y
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isPointNeedGo(int x, int y)
    {
        return !deadSet.contains(getKey(x, y));
        
    }
    
    /**
     * �Ƿ��ǿ�ǽ������
     * 
     * @param curMap
     * @param x
     * @param y
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private static boolean isPointDead(Cell[][] curMap, int x, int y)
    {
        
        // �� �� �� �� Ϊ�̶���λ�ã���Ϊ1
        int[] UpRiDoLe = {0, 0, 0, 0};
        if (x == 0 || curMap[x - 1][y].getItem() == ItemType.wall)
        {
            UpRiDoLe[3] = 1;
        }
        else if (x == (curMap.length - 1)
            || curMap[x + 1][y].getItem() == ItemType.wall)
        {
            UpRiDoLe[1] = 1;
        }
        
        if (y == 0 || curMap[x][y - 1].getItem() == ItemType.wall)
        {
            UpRiDoLe[0] = 1;
        }
        else if (y == (curMap[0].length - 1)
            || curMap[x][y + 1].getItem() == ItemType.wall)
        {
            UpRiDoLe[2] = 1;
        }
        
        for (int i = 0; i < 4; i++)
        {
            int j = i + 1;
            if (j == 4)
            {
                j = 0;
            }
            if (UpRiDoLe[i] + UpRiDoLe[j] == 2)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * �����ƶ�
     * 
     * @param curMap
     * @param y �����y
     * @param x �����x
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean smartDeadPoint(Cell[][] curMap, int x, int y)
    {

        Cell[][] cloneObject = Util.cloneMapClearStatue(curMap);
        cloneObject[x][y].setItem(ItemType.statue);
        
        Solution solu = new Solution(cloneObject);
        
        // ��ȡ�������ߵ��߷��б�
        Solution runByLevel = SolutionFactory.runByLevel(solu);
        if (null != runByLevel)
        {
            return false;
        }
        
        // ���һ�������ɹ�����ʼ����λ��Ϊδ�����ĵط�
        HashSet<Cell> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(cloneObject);
            
        // ������������ڵ������������
        if (y > 0 && !allPlayerCanGoCells.contains(cloneObject[x][y - 1]))
        {
            // վ�˵�λ�ò��������㣬��Ϊ������£���Ϊ�ǳ�ʼֵ������ҲΪ�ǳ�ʼֵ��
            // �������ˣ��϶����ƶ���������վ�����㣬�����һ���ƵĶ��������������㣬���ܳ���
            if (cloneObject[x][y - 1].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x, y - 1))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x][y - 1].setPlayer();
                    
                    // ��ȡ�������ߵ��߷��б�
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        if (y < (cloneObject[0].length - 1)
            && !allPlayerCanGoCells.contains(cloneObject[x][y + 1]))
        {
            if (cloneObject[x][y + 1].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x, y + 1))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x][y + 1].setPlayer();
                    
                    // ��ȡ�������ߵ��߷��б�
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        if (x > 0 && !allPlayerCanGoCells.contains(cloneObject[x - 1][y]))
        {
            if (cloneObject[x - 1][y].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x - 1, y))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x - 1][y].setPlayer();
                    
                    // ��ȡ�������ߵ��߷��б�
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        if (x < (cloneObject.length - 1)
            && !allPlayerCanGoCells.contains(cloneObject[x + 1][y]))
        {
            if (cloneObject[x + 1][y].getItem() == ItemType.empty)
            {
                if (!isPointNeedGo(x + 1, y))
                {
                    Cell[][] cloneMapClearPlayer =
                        Util.cloneMapClearPlayerAndStatue(curMap);
                    cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                    cloneMapClearPlayer[x + 1][y].setPlayer();
                    
                    // ��ȡ�������ߵ��߷��б�
                    Solution run = SolutionFactory
                        .runByLevel(new Solution(cloneMapClearPlayer));
                    if (null != run)
                    {
                        return false;
                    }
                    
                }
            }
        }
        
        return true;
    }
}
