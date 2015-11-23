package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.manager.SolutionFactory;
import com.jaxer.www.manager.SolutionManager;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.FastSet;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Solution;
import com.jaxer.www.model.Zuobiao;

public class DeadPoitUtil
{
    private static boolean smart = true;
    
    public static HashSet<Zuobiao> loadDeadSet(SokoMap sokoMap)
    {
        HashSet<Zuobiao> deadSet = new HashSet<Zuobiao>();
        
        if (!Logger.isprintDeadInfo)
        {
            Logger.turnOff();
            
        }
        Logger.info("==��ʼ��������==");
        Long begin = System.currentTimeMillis();
        for (Cell[] cells : sokoMap.getThisStepMap())
        {
            for (Cell cell : cells)
            {
                
                // ���ڷ������ӵĿ�����
                if (sokoMap.getBoxList().contains(cell))
                {
                    if (Logger.isdebug)
                    {
                        Logger.debug("����" + cell + ",�������Ӳ���������");
                    }
                    continue;
                }
                if (cell.check(CellType.gole) || cell.check(CellType.wall))
                {
                    if (Logger.isdebug)
                    {
                        Logger.debug("����" + cell + ",ǽ��Ŀ��㲻��������");
                        
                    }
                    continue;
                }
                
                if (checkPoint(sokoMap, cell))
                {
                    deadSet.add(cell);
                    continue;
                }
                
                if (smart && smartDeadPoint(sokoMap, cell))
                {
                    deadSet.add(cell);
                    
                    continue;
                }
            }
        }
        Long end = System.currentTimeMillis();
        Logger.info("==������������,��ʱ��" + (end - begin));
        Logger.turnOn();
        if (Logger.isInfo)
        {
            StringBuilder mapStr = sokoMap.mapStr();
            for (Zuobiao point : deadSet)
            {
                Util.replaceZuobiao(mapStr, point, "X", sokoMap.getMax_x());
            }
            Logger.info(Util.drawMap(mapStr, sokoMap.getMax_x()));
        }
        return deadSet;
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
    private static boolean checkPoint(SokoMap sokomap, Zuobiao zb)
    {
        
        // �� �� �� �� Ϊ�̶���λ�ã���Ϊ1
        AspectEnum[] UpRiDoLe = {AspectEnum.up, AspectEnum.left, AspectEnum.down, AspectEnum.right};
        
        boolean[] isWall = {false, false, false, false};
        
        for (int i = 0; i < UpRiDoLe.length; i++)
        {
            Zuobiao edge = sokomap.getMove(zb, UpRiDoLe[i]);
            if (edge == null || sokomap.getCell(edge).check(CellType.wall))
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
     * �����ƶϣ��Ƴ����е���ֻ��һ���������ƶϵĵ㣬���Ƿ����ƶ�����һ��Ŀ��㡣 ������ܣ��ٲ�����λ���Ƿ��������������������������ҵ�λ�ã��ٴν����ƶϡ�
     *
     * @param zb ���ӵ�����
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean smartDeadPoint(SokoMap sokoMap, Zuobiao zb)
    {
        
        ArrayList<Zuobiao> deadPointList = new ArrayList<Zuobiao>();
        deadPointList.add(zb);
        SokoMap clone = new SokoMap(sokoMap, deadPointList, sokoMap.getMan());
        
        // ��ȡ��ģʽ����Ϊ��ʼֵ���ܷ��ƶ�����һĿ���
        SolutionManager.runByLevel(clone);
        if (null != clone.getSuccess())
        {
            return false;
        }
        
        // ������ɹ�����ȡ�ó�ʼ����λ�õĵȼ�λ��
        
        FastSet allPlayerCanGoCells = clone.getPlayerCanGoCells(deadPointList, sokoMap.getMan());
        
        // ����������������ӵ��������ң��Ҳ������ϵȼ������λ�ã��ٴ��ƶ�
        for (AspectEnum aspect : AspectEnum.getAllEnum())
        {
            
            Zuobiao manOnEdge = clone.getMove(zb, aspect);
            if (manOnEdge == null)
            {
                continue;
            }
            
            if (clone.getCell(manOnEdge).check(CellType.wall))
            {
                continue;
            }
            if (clone.isPointDie(manOnEdge))
            {
                continue;
            }
            if (allPlayerCanGoCells.contains(manOnEdge))
            {
                continue;
            }
            
            SokoMap cloneOther = new SokoMap(sokoMap, deadPointList, manOnEdge);
            SolutionManager.runByLevel(cloneOther);
            if (cloneOther.getSuccess() != null)
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * ��ȡ�������Χ�ɵ����㼯��<br/>
     * �������������ϵ��������ض��ĵ�λ������⼸������ֻ���Ƶ���������
     * 
     * @param sokoMap
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void roundDeadPoitSet(SokoMap sokoMap)
    {
        
        Logger.info("==��ʼ��Χ����==");
        // ��ȡ���ǿ�λ���б�,
        ArrayList<Zuobiao> manCanGoCells = Util.cloneBoxList(sokoMap.getManCanGoCells());
        manCanGoCells.removeAll(sokoMap.getDeadSet());
        manCanGoCells.removeAll(sokoMap.getGoleList());
        
        Zuobiao man = sokoMap.getGoleList().get(0);
        
        ArrayList<Zuobiao[]> roundDeadPoint = new ArrayList<Zuobiao[]>();
        int num = 2;
        while (num < 6)
        {
            Logger.info("==��Χ����==" + num + "/" + sokoMap.getBoxList().size() + "==");
            
            char[] firstUnit = Util.getFirstUnit(manCanGoCells.size(), num);
            
            ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>(num);
            
            do
            {
                boxList.clear();
                for (int j = 0; j < firstUnit.length; j++)
                {
                    if (firstUnit[j] == '1')
                    {
                        boxList.add(manCanGoCells.get(j));
                    }
                }
                // һ��3�������2������Χ״̬�������ٲ��ԡ�
                if (Util.checkRound(boxList, sokoMap))
                {
                    continue;
                }
                
                SokoMap sokoMap2 = new SokoMap(sokoMap, boxList, man, true);
                LinkedList<Solution> nextSolution = SolutionFactory.getNextSolution(new Solution(), sokoMap2);
                if (sokoMap2.getSuccess() == null && nextSolution.isEmpty())
                {
                    // ˵�����boxListʱ�Ѿ�û���ƶ��Ļ�����
                    roundDeadPoint.add(boxList.toArray(new Zuobiao[0]));
                    if (Logger.isdebug)
                    {
                        Logger.debug("==��Χ���==" + Arrays.toString(firstUnit) + "==");
                    }
                }
            } while (Util.getNextUnit(firstUnit));
            num++;
        }
        
        sokoMap.setRoundDeadPoint(roundDeadPoint);
        Logger.info("==������Χ����==");
    }
    
    /**
     * �˴Ӵ˵���������ܵ���յ�(�����㣬��gole��)
     * 
     * @param sokoMap
     * @param boxList
     * @param man
     * @return [����˵��]
     *         
     * @return boolean [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isManGoEmpty(SokoMap sokoMap, ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        if (boxList == null)
        {
            boxList = new ArrayList<Zuobiao>();
        }
        FastSet playerCanGoCells = sokoMap.getPlayerCanGoCells(boxList, man);
        ArrayList<Zuobiao> pcgc = playerCanGoCells.getList();
        
        for (Zuobiao zuobiao : pcgc)
        {
            if (!sokoMap.isPointDie(zuobiao))
            {
                // ����߼���Ϊ���������㣬Ҳ�ܵ���
                return false;
            }
        }
        return true;
    }
    
    /**
     * ����Ч��ǽ�����㣬��Ϊǽ
     * 
     * @param sokoMap [����˵��]
     *            
     * @return void [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void convertWall(SokoMap sokoMap)
    {
        ArrayList<Zuobiao> pointDieList = sokoMap.getPointDieList();
        for (Zuobiao zuobiao : pointDieList)
        {
            if (isManGoEmpty(sokoMap, null, zuobiao))
            {
                Cell[][] thisStepMap = sokoMap.getThisStepMap();
                thisStepMap[zuobiao.getX()][zuobiao.getY()].setItem(CellType.wall);
                sokoMap.removeManCango(zuobiao);
            }
        }
    }
    
}
