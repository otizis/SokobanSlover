package com.jaxer.www.Util;

import java.util.HashSet;

import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.DieCell;
import com.jaxer.www.model.Solution;

public class DeadPoitUtil
{
    private static boolean smart = true;
    
    static HashSet<String> deadSet = new HashSet<String>();
    
    public static void loadDeadSet(Cell[][] curMap)
    {
        Logger.info("==��ʼ��������==");
        Logger.turnOff();
        Long begin = System.currentTimeMillis();
        
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
                
                if (smart && smartDeadPoint(curMap, x, y))
                {
                    deadSet.add(getKey(x, y));
                    
                    continue;
                }
            }
        }
        Long end = System.currentTimeMillis();
        Logger.turnOn();
        Logger.info("==������������,��ʱ��" + (end - begin));
        Cell[][] cloneMap = Util.cloneMap(curMap);
        
        for (String point : deadSet)
        {
            String[] xy = point.split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            cloneMap[x][y] = new DieCell();
        }
        Logger.info(Util.mapToString(cloneMap));
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
     * �����ƶϣ��Ƴ����е���ֻ��һ���������ƶϵĵ㣬���Ƿ����ƶ�����һ��Ŀ��㡣
     * ������ܣ��ٲ�����λ���Ƿ��������������������������ҵ�λ�ã��ٴν����ƶϡ�
     * 
     * @param curMap
     * @param y �����y
     * @param x �����x
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean smartDeadPoint(Cell[][] curMap, int x, int y)
    {
        // ����¼��������ʱ�ĵ�ͼ����ֵ��ÿ�ζ�����
        Util.resetMapSet();
        
        Cell[][] cloneObject = Util.cloneMapClearStatue(curMap);
        cloneObject[x][y].setItem(ItemType.statue);
        
        Solution solu = new Solution(cloneObject);
        
        // ��ȡ��ģʽ����Ϊ��ʼֵ���ܷ��ƶ�����һĿ���
        Solution runByLevel = SolutionFactory.runByLevel(solu);
        if (null != runByLevel)
        {
            return false;
        }
        
        // ������ɹ�����ȡ�ó�ʼ����λ�õĵȼ�λ��
        HashSet<Cell> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(cloneObject);
            
        // ������������ڵ�����������ң��Ҳ������ϵȼ������λ�ã��ٴ��ƶ�
        if (y > 0)
        {
            
            int player_x = x;
            int player_y = y - 1;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // �����ȡ���ⷨ����ʾ��������
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        if (y < (cloneObject[0].length - 1))
        {
            int player_x = x;
            int player_y = y + 1;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // �����ȡ���ⷨ����ʾ��������
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        if (x > 0)
        {
            int player_x = x - 1;
            int player_y = y;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // �����ȡ���ⷨ����ʾ��������
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        if (x < (cloneObject.length - 1))
        {
            int player_x = x + 1;
            int player_y = y;
            
            Solution checkSwitchPlayer = checkSwitchPlayer(x,
                y,
                cloneObject,
                allPlayerCanGoCells,
                player_x,
                player_y);
            // �����ȡ���ⷨ����ʾ��������
            if (checkSwitchPlayer != null)
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * ������λ���Ƿ��������������������������ҵ�λ�ã��ٴν����ƶϡ�
     * 
     * @param x ����λ��
     * @param y ����λ��
     * @param cloneObject
     * @param allPlayerCanGoCells ��һ�������վλ�õ�ͬ��λ��
     * @param player_x �������λ��
     * @param player_y �������λ��
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private static Solution checkSwitchPlayer(int x, int y,
        Cell[][] cloneObject, HashSet<Cell> allPlayerCanGoCells, int player_x,
        int player_y)
    {
        if (cloneObject[player_x][player_y].getItem() == ItemType.empty
            && !allPlayerCanGoCells.contains(cloneObject[player_x][player_y]))
        {
            // վ�˵�λ�ò��������㣬��Ϊ������£���Ϊ�ǳ�ʼֵ������ҲΪ�ǳ�ʼֵ��
            // �������ˣ��϶����ƶ���������վ�����㣬�����һ���ƵĶ��������������㣬���ܳ���
            if (!isPointNeedGo(player_x, player_y))
            {
                Cell[][] cloneMapClearPlayer =
                    Util.cloneMapClearPlayerAndStatue(cloneObject);
                cloneMapClearPlayer[x][y].setItem(ItemType.statue);
                cloneMapClearPlayer[player_x][player_y].setPlayer();
                
                // ��ȡ�������ߵ��߷��б�
                Solution run = SolutionFactory
                    .runByLevel(new Solution(cloneMapClearPlayer));
                return run;
                
            }
        }
        return null;
    }
}
