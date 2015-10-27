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
    
    public static void loadDeadSet()
    {
        Logger.info("==��ʼ��������==");
        Logger.turnOff();
        Long begin = System.currentTimeMillis();
        
        for (Cell[] cells : SokoMap.thisStepMap)
        {
            for (Cell cell : cells)
            {
                
                // ���ڷ������ӵĿ�����
                if (SokoMap.boxList.contains(cell))
                {
                    Logger.debug("����" + cell + ",�������Ӳ���������");
                    continue;
                }
                if (cell.check(CellType.gole) || cell.check(CellType.wall))
                {
                    Logger.debug("����" + cell + ",ǽ��Ŀ��㲻��������");
                    continue;
                }
                
                if (isPointDead(cell))
                {
                    deadSet.add(cell);
                    continue;
                }
                
                if (smart && smartDeadPoint(cell))
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
        Logger.info(Util.drawMap(mapStr));
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
    private static boolean isPointDead(Zuobiao zb)
    {
        
        // �� �� �� �� Ϊ�̶���λ�ã���Ϊ1
        AspectEnum[] UpRiDoLe =
            {AspectEnum.up, AspectEnum.left, AspectEnum.down, AspectEnum.right};
            
        boolean[] isWall = {false, false, false, false};
        
        for (int i = 0; i < UpRiDoLe.length; i++)
        {
            Zuobiao edge = ZuobiaoUtil.getMove(zb, UpRiDoLe[i]);
            if (edge == null || SokoMap.getCell(edge).check(CellType.wall))
            {
                isWall[i] = true;
            }
        }
        
        for (int i = 0; i < 4; i++)
        {
            int j = i + 1;
            if (j == 4)
            {
                j = 0;
            }
            if (isWall[i] && isWall[j])
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
     * @param zb ���ӵ�����
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean smartDeadPoint(Zuobiao zb)
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
        ArrayList<Zuobiao> allPlayerCanGoCells =
            SolutionFactory.getPlayerCanGoCells(solu);
            
        // ������������ڵ�����������ң��Ҳ������ϵȼ������λ�ã��ٴ��ƶ�
        for (AspectEnum aspect : AspectEnum.values())
        {
            
            Zuobiao zbMan = ZuobiaoUtil.getMove(zb, aspect);
            if (zbMan == null)
            {
                continue;
            }
            if (!SokoMap.getCell(zbMan).check(CellType.empty))
            {
                continue;
            }
            if (!isPointNeedGo(zbMan))
            {
                continue;
            }
            
            if (allPlayerCanGoCells.contains(zbMan))
            {
                continue;
            }
            SokoMap.man = zbMan;
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
