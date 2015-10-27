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
     * �������빹���ͼ
     */
    public SokoMap(String mapStr)
    {
        if (!mapStr.contains("P"))
        {
            throw new MyException("û�з������λ�á�");
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
     * ������
     * 
     * @param lns
     * @param y ����
     * @see [�ࡢ��#��������#��Ա]
     */
    private void loadLine(String[] lns, int y)
    {
        for (int x = 0; x < lns[y].length(); x++)
        {
            loadCell(lns, y, x);
        }
    }
    
    /**
     * ����һ�����ӵ�����
     * 
     * @param lns
     * @param y
     * @param x
     * @see [�ࡢ��#��������#��Ա]
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
     * �����㷨
     * 
     * @param map
     * @see [�ࡢ��#��������#��Ա]
     */
    public Solution run()
    {
        
        Long begin = System.currentTimeMillis();
        
        DeadPoitUtil.loadDeadSet();
        
        Solution solution = new Solution();
        
        Logger.info(solution.drawBefore());
        
        Solution lastOne = SolutionFactory.runByLevel(solution);
        
        Long end = System.currentTimeMillis();
        
        System.out.println("��ʱ��" + (end - begin));
        return lastOne;
        
    }
}
