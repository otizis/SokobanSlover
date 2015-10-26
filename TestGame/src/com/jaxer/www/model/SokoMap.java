package com.jaxer.www.model;

import java.util.ArrayList;

import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.SolutionFactory;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.myexception.MyException;

public class SokoMap
{
    public static Cell[][] thisStepMap;
    
    public static int max_x;
    
    public static int max_y;
    
    public static ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>();
    
    public static Zuobiao man;
    
    public static Cell getCell(Zuobiao zb)
    {
        return thisStepMap[zb.x][zb.y];
    }
    
    public static final char wall = 'M';
    
    public static final char box = 'Y';
    
    public static final char goal = 'G';
    
    public static final char player = 'P';
    
    public static final char empty = 'S';
    
    public SokoMap(String mapStr)
    {
        if (!mapStr.contains("P"))
        {
            throw new MyException("没有放置玩家位置。");
        }
        Cell[][] map = null;
        String[] lns = mapStr.split(";");
        for (int i = 0; i < lns.length; i++)
        {
            for (int j = 0; j < lns[i].length(); j++)
            {
                if (null == map)
                {
                    map = new Cell[lns[i].length()][lns.length];
                }
                char type = lns[i].charAt(j);
                CellType ctype = null;
                switch (type)
                {
                    case empty:
                        ctype = CellType.empty;
                        break;
                    case player:
                        ctype = CellType.empty;
                        man = new Zuobiao(j, i);
                        break;
                    case box:
                        ctype = CellType.empty;
                        boxList.add(new Zuobiao(j, i));
                        break;
                    case goal:
                        ctype = CellType.gole;
                        break;
                    case wall:
                        ctype = CellType.wall;
                        break;
                        
                }
                map[j][i] = new Cell(j, i, ctype);
            }
        }
        thisStepMap = map;
        max_x = map.length - 1;
        max_y = map[0].length - 1;
    }
    
    /**
     * 运行算法
     * 
     * @param map
     * @see [类、类#方法、类#成员]
     */
    public Solution run()
    {
        
        Long begin = System.currentTimeMillis();
        
        DeadPoitUtil.loadDeadSet(thisStepMap);
        
        Solution solution = new Solution();
        
        if (Logger.isInfo)
        {
            solution.drawBefo();
        }
        
        Solution lastOne = SolutionFactory.runByLevel(solution);
        
        Long end = System.currentTimeMillis();
        
        System.out.println("耗时：" + (end - begin));
        return lastOne;
        
    }
}
