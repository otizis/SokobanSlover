package com.jaxer.www.Util;

import java.util.ArrayList;
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
        Logger.info("==开始死点推算==");
        Long begin = System.currentTimeMillis();
        for (Cell[] cells : sokoMap.getThisStepMap())
        {
            for (Cell cell : cells)
            {
                
                // 现在放着箱子的可跳过
                if (sokoMap.getBoxList().contains(cell))
                {
                    if (Logger.isdebug)
                    {
                        
                        Logger.debug("跳过" + cell + ",放着箱子不会是死点");
                    }
                    continue;
                }
                if (cell.check(CellType.gole) || cell.check(CellType.wall))
                {
                    if (Logger.isdebug)
                    {
                        Logger.debug("跳过" + cell + ",墙和目标点不会是死点");
                        
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
        Logger.info("==结束死点推算,耗时：" + (end - begin));
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
     * 是否是靠墙的死点
     * 
     * @param curMap
     * @param x
     * @param y
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static boolean checkPoint(SokoMap sokomap, Zuobiao zb)
    {
        
        // 上 右 下 左 为固定的位置，标为1
        AspectEnum[] UpRiDoLe =
            {AspectEnum.up, AspectEnum.left, AspectEnum.down, AspectEnum.right};
            
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
     * 死点推断，移除所有雕像，只放一个雕像在推断的点，看是否能移动到任一个目标点。
     * 如果不能，再测试人位置是否可以有其他情况，历遍上下左右的位置，再次进行推断。
     *
     * @param zb 箱子的坐标
     * @see [类、类#方法、类#成员]
     */
    public static boolean smartDeadPoint(SokoMap sokoMap, Zuobiao zb)
    {
        
        ArrayList<Zuobiao> deadPointList = new ArrayList<Zuobiao>();
        deadPointList.add(zb);
        SokoMap clone = new SokoMap(sokoMap, deadPointList, sokoMap.getMan());
        
        // 获取该模式，人为初始值，能否推动到任一目标点
        SolutionManager.runByLevel(clone);
        if (null != clone.getSuccess())
        {
            return false;
        }
        
        // 如果不成功，获取该初始化人位置的等价位置
        
        FastSet allPlayerCanGoCells =
            clone.getPlayerCanGoCells(deadPointList, sokoMap.getMan());
            
        // 四种情况，人在箱子的上下左右，且不是以上等价情况的位置，再次推断
        for (AspectEnum aspect : AspectEnum.values())
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
     * 获取多个箱子围成的死点集合<br/>
     * 两个或两个以上的箱子在特定的点位后，造成这几个箱子只能推到死点的情况
     * 
     * @param sokoMap
     * @see [类、类#方法、类#成员]
     */
    public static void roundDeadPoitSet(SokoMap sokoMap)
    {
        // 获取还是空位的列表,
        ArrayList<Zuobiao> manCanGoCells = sokoMap.getManCanGoCells();
        manCanGoCells.removeAll(sokoMap.getDeadSet());
        manCanGoCells.removeAll(sokoMap.getGoleList());
        
        Zuobiao man = sokoMap.getGoleList().get(0);
        
        ArrayList<Zuobiao[]> roundDeadPoint = new ArrayList<Zuobiao[]>();
        for (int i = 0; i < manCanGoCells.size(); i++)
        {
            for (int j = 0; j < manCanGoCells.size(); j++)
            {
                if (i == j)
                {
                    continue;
                }
                Zuobiao a = manCanGoCells.get(i);
                Zuobiao b = manCanGoCells.get(j);
                ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>(2);
                boxList.add(a);
                boxList.add(b);
                SokoMap sokoMap2 = new SokoMap(sokoMap, boxList, man);
                
                LinkedList<Solution> nextSolution =
                    SolutionFactory.getNextSolution(new Solution(), sokoMap2);
                if (sokoMap2.getSuccess() == null && nextSolution.isEmpty())
                {
                    // 说明这个boxList时已经没有推动的机会了
                    roundDeadPoint.add(boxList.toArray(new Zuobiao[0]));
                }
                
            }
        }
        
        sokoMap.setRoundDeadPoint(roundDeadPoint);
    }
    
    
}
