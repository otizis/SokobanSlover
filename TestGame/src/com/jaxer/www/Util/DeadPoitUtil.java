package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.HashSet;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Solution;
import com.jaxer.www.model.Zuobiao;

public class DeadPoitUtil
{
    private static boolean smart = true;
    
    static HashSet<Zuobiao> deadSet = new HashSet<Zuobiao>();
    
    public static void loadDeadSet(Cell[][] curMap)
    {
        Logger.info("==��ʼ��������==");
        Logger.turnOff();
        Long begin = System.currentTimeMillis();
        
        for (Cell[] cells : curMap)
        {
            for (Cell cell : cells)
            {
                if (cell.check(CellType.gole) || cell.check(CellType.wall))
                {
                    // TODO ���ڷ������ӵ�Ҳ������
                    continue;
                }
                int x = cell.getX();
                int y = cell.getY();
                
                if (isPointDead(curMap, x, y))
                {
                    deadSet.add(cell);
                    continue;
                }
                
                if (smart && smartDeadPoint(curMap, cell))
                {
                    deadSet.add(cell);
                    
                    continue;
                }
            }
        }
        Long end = System.currentTimeMillis();
        Logger.turnOn();
        Logger.info("==������������,��ʱ��" + (end - begin));
        StringBuilder mapStr = Util.mapStr();
        
        for (Zuobiao point : deadSet)
        {
            Util.replaceZuobiao(mapStr, point, "X");
        }
        if (Logger.isInfo)
        {
            Util.drawMap(mapStr);
        }
    }
    
    /**
     * �õ�λ�Ƿ�������,ǽ��
     * 
     * @param zb
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isPointNeedGo(Zuobiao zb)
    {
        return !deadSet.contains(zb);
        
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
        if (x == 0 || curMap[x - 1][y].check(CellType.wall))
        {
            UpRiDoLe[3] = 1;
        }
        else if (x == (curMap.length - 1)
            || curMap[x + 1][y].check(CellType.wall))
        {
            UpRiDoLe[1] = 1;
        }
        
        if (y == 0 || curMap[x][y - 1].check(CellType.wall))
        {
            UpRiDoLe[0] = 1;
        }
        else if (y == (curMap[0].length - 1)
            || curMap[x][y + 1].check(CellType.wall))
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
    public static boolean smartDeadPoint(Cell[][] curMap, Zuobiao zb)
    {
        // ����¼��������ʱ�ĵ�ͼ����ֵ��ÿ�ζ�����
        Util.resetMapSet();
        
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(SokoMap.boxList);
        Zuobiao cloneMan = SokoMap.man;
        
        ArrayList<Zuobiao> deadPointList = new ArrayList<Zuobiao>();
        deadPointList.add(zb);
        SokoMap.boxList = deadPointList;
        
        Solution solu = new Solution();
        
        // ��ȡ��ģʽ����Ϊ��ʼֵ���ܷ��ƶ�����һĿ���
        Solution runByLevel = SolutionFactory.runByLevel(solu);
        if (null != runByLevel)
        {
            // ��ԭ
            SokoMap.boxList = cloneBoxList;
            SokoMap.man = cloneMan;
            return false;
        }
        
        // ������ɹ�����ȡ�ó�ʼ����λ�õĵȼ�λ��
        HashSet<Zuobiao> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(solu);
            
        // ������������ڵ�����������ң��Ҳ������ϵȼ������λ�ã��ٴ��ƶ�
        for (AspectEnum aspect : AspectEnum.values())
        {
            
            Zuobiao move = ZuobiaoUtil.getMove(zb, aspect);
            if (isPointNeedGo(move))
            {
                continue;
            }
            if (allPlayerCanGoCells.contains(move))
            {
                continue;
            }
            SokoMap.man = move;
            Solution run = SolutionFactory.runByLevel(new Solution());
            if (run != null)
            {
                // ��ԭ
                SokoMap.boxList = cloneBoxList;
                SokoMap.man = cloneMan;
                return false;
            }
        }
        
        // ��ԭ
        SokoMap.boxList = cloneBoxList;
        SokoMap.man = cloneMan;
        return true;
    }
    
}
