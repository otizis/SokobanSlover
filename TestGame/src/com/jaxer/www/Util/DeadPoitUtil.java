package com.jaxer.www.Util;

import java.util.HashSet;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class DeadPoitUtil
{
    private static boolean smart = false;
    
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
                
                if (smart
                // && smartDeadPoint(cell)
                )
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
    
    // /**
    // * 死点推断，移除所有雕像，只放一个雕像在推断的点，看是否能移动到任一个目标点。 如果不能，再测试人位置是否可以有其他情况，历遍上下左右的位置，再次进行推断。
    // *
    // * @param zb 箱子的坐标
    // * @see [类、类#方法、类#成员]
    // */
    // public static boolean smartDeadPoint(Zuobiao zb)
    // {
    // // 不记录计算死点时的地图特征值，每次都重置
    // Util.resetMapSet();
    //
    // ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(SokoMap.boxList);
    // Zuobiao cloneMan = SokoMap.man;
    //
    // ArrayList<Zuobiao> deadPointList = new ArrayList<Zuobiao>();
    // deadPointList.add(zb);
    // SokoMap.boxList = deadPointList;
    //
    // Solution solu = new Solution();
    //
    // // 获取该模式，人为初始值，能否推动到任一目标点
    // Solution runByLevel = SolutionManager.runByLevel(solu);
    // if (null != runByLevel)
    // {
    // // 复原
    // SokoMap.boxList = cloneBoxList;
    // SokoMap.man = cloneMan;
    // return false;
    // }
    //
    // // 如果不成功，获取该初始化人位置的等价位置
    // ArrayList<Zuobiao> allPlayerCanGoCells = SolutionFactory.getPlayerCanGoCells(solu);
    //
    // // 四种情况，人在箱子的上下左右，且不是以上等价情况的位置，再次推断
    // for (AspectEnum aspect : AspectEnum.values())
    // {
    //
    // Zuobiao manOnEdge = ZuobiaoUtil.getMove(zb, aspect);
    // if (manOnEdge == null)
    // {
    // continue;
    // }
    //
    // if (SokoMap.getCell(manOnEdge).check(CellType.wall))
    // {
    // continue;
    // }
    // if (isPointDie(manOnEdge))
    // {
    // continue;
    // }
    // if (allPlayerCanGoCells.contains(manOnEdge))
    // {
    // continue;
    // }
    //
    // SokoMap.man = manOnEdge;
    // Solution run = SolutionManager.runByLevel(new Solution());
    // if (run != null)
    // {
    // // 复原
    // SokoMap.boxList = cloneBoxList;
    // SokoMap.man = cloneMan;
    // return false;
    // }
    // }
    //
    // // 复原
    // SokoMap.boxList = cloneBoxList;
    // SokoMap.man = cloneMan;
    // return true;
    // }
    //
}
