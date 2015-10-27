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
    
    public static ArrayList<Zuobiao> manCanGoCells = new ArrayList<Zuobiao>();
    
    public static int max_x;
    
    public static int max_y;
    
    public static ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>();
    
    public static Zuobiao man;
    
    public static Cell getCell(Zuobiao zb)
    {
        return thisStepMap[zb.x][zb.y];
    }
    
    public static final char wall = 'M';
    
    public static final char box = 'B';
    
    public static final char goal = 'G';
    
    public static final char player = 'P';
    
    public static final char empty = 'S';
    
    /**
     * 根据输入构造地图
     */
    public SokoMap(String mapStr)
    {
        if (!mapStr.contains("P"))
        {
            throw new MyException("没有放置玩家位置。");
        }
        String[] lns = mapStr.split(";");
        for (int y = 0; y < lns.length; y++)
        {
            loadLine(lns, y);
        }
        max_x = thisStepMap.length - 1;
        max_y = thisStepMap[0].length - 1;
    }
    
    /**
     * 解析行
     * 
     * @param lns
     * @param y 行数
     * @see [类、类#方法、类#成员]
     */
    private void loadLine(String[] lns, int y)
    {
        for (int x = 0; x < lns[y].length(); x++)
        {
            loadCell(lns, y, x);
        }
    }
    
    /**
     * 加载一个格子的类型
     * 
     * @param lns
     * @param y
     * @param x
     * @see [类、类#方法、类#成员]
     */
    private void loadCell(String[] lns, int y, int x)
    {
        if (null == thisStepMap)
        {
            thisStepMap = new Cell[lns[y].length()][lns.length];
        }
        
        char type = lns[y].charAt(x);
        CellType ctype = null;
        
        switch (type)
        {
            case empty:
                ctype = CellType.empty;
                manCanGoCells.add(new Zuobiao(x, y));
                break;
                
            case player:
                ctype = CellType.empty;
                man = new Zuobiao(x, y);
                manCanGoCells.add(new Zuobiao(x, y));
                break;
                
            case box:
                ctype = CellType.empty;
                boxList.add(new Zuobiao(x, y));
                manCanGoCells.add(new Zuobiao(x, y));
                break;
                
            case goal:
                ctype = CellType.gole;
                manCanGoCells.add(new Zuobiao(x, y));
                break;
                
            case wall:
                ctype = CellType.wall;
                break;
        }
        thisStepMap[x][y] = new Cell(x, y, ctype);
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
        
        DeadPoitUtil.loadDeadSet();
        
        Solution solution = new Solution();
        
        Logger.info(solution.drawBefore());
        
        Solution lastOne = SolutionFactory.runByLevel(solution);
        
        Long end = System.currentTimeMillis();
        
        System.out.println("耗时：" + (end - begin));
        return lastOne;
        
    }
}
