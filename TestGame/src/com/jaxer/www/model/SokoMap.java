package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.jaxer.www.Main;
import com.jaxer.www.Filter.BloomFliter;
import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.api.MapFliter;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.manager.SolutionManager;
import com.jaxer.www.myexception.MyException;

public class SokoMap
{
    private Cell[][] thisStepMap;
    
    HashSet<Zuobiao> deadSet = null;
    
    private ArrayList<Zuobiao> manCanGoCells = new ArrayList<Zuobiao>();
    
    private ArrayList<Zuobiao> goleList = new ArrayList<Zuobiao>();
    
    private int max_x;
    
    private int max_y;
    
    private ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>();
    
    private Zuobiao man;
    
    public Cell getCell(Zuobiao zb)
    {
        
        return thisStepMap[zb.x][zb.y];
    }
    
    /**
     * @return ���� thisStepMap
     */
    public Cell[][] getThisStepMap()
    {
        return thisStepMap;
    }
    
    /**
     * @return ���� manCanGoCells
     */
    public ArrayList<Zuobiao> getManCanGoCells()
    {
        return manCanGoCells;
    }
    
    /**
     * @return ���� goleList
     */
    public ArrayList<Zuobiao> getGoleList()
    {
        return goleList;
    }
    
    /**
     * @return ���� max_x
     */
    public int getMax_x()
    {
        return max_x;
    }
    
    /**
     * @return ���� max_y
     */
    public int getMax_y()
    {
        return max_y;
    }
    
    /**
     * @return ���� boxList
     */
    public ArrayList<Zuobiao> getBoxList()
    {
        return boxList;
    }
    
    /**
     * @return ���� man
     */
    public Zuobiao getMan()
    {
        return man;
    }
    
    public static final char wall = 'M';
    
    public static final char box = 'B';
    
    public static final char goal = 'G';
    
    public static final char player = 'P';
    
    public static final char empty = 'S';
    
    public static final char boxOnGoal = 'R';
    
    public static final char playerOnGoal = 'L';
    
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
                if (man != null)
                {
                    throw new MyException("�Ѿ��������λ�ã�" + man);
                }
                man = new Zuobiao(x, y);
                manCanGoCells.add(new Zuobiao(x, y));
                break;
                
            case box:
                ctype = CellType.empty;
                boxList.add(new Zuobiao(x, y));
                manCanGoCells.add(new Zuobiao(x, y));
                break;
                
            case boxOnGoal:
                ctype = CellType.gole;
                boxList.add(new Zuobiao(x, y));
                manCanGoCells.add(new Zuobiao(x, y));
                break;
                
            case playerOnGoal:
                ctype = CellType.gole;
                if (man != null)
                {
                    throw new MyException("�Ѿ��������λ�ã�" + man);
                }
                man = new Zuobiao(x, y);
                manCanGoCells.add(new Zuobiao(x, y));
                goleList.add(new Zuobiao(x, y));
                break;
                
            case goal:
                ctype = CellType.gole;
                manCanGoCells.add(new Zuobiao(x, y));
                goleList.add(new Zuobiao(x, y));
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
        
        this.deadSet = DeadPoitUtil.loadDeadSet(this);
        
        Solution solution = new Solution();
        
        Logger.info(SolutionManager.drawBefore(solution, this));
        
        Solution lastOne = SolutionManager.runByLevel(solution, this);
        
        Long end = System.currentTimeMillis();
        
        Main.outputResult(lastOne);
        
        System.out.println("��ʱ��" + (end - begin));
        
        return lastOne;
        
    }
    
    /**
     * �õ�λ�Ƿ�������,ǽ��
     * 
     * @param zb
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean isPointDie(Zuobiao zb)
    {
        return deadSet.contains(zb);
        
    }
    
    /**
     * �ⷨ�У���ͼ����Ϊ�գ��Ҳ���box����֮һ������true
     * 
     * 
     * @param solu
     * @param gotoCell
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean canGo(ArrayList<Zuobiao> boxList, Zuobiao gotoZuobiao)
    {
        if (gotoZuobiao == null)
        {
            return false;
        }
        // Ŀ��λ��������ǽ
        if (getCell(gotoZuobiao).check(CellType.wall))
        {
            return false;
            
        }
        // Ŀ��λ��������
        if (boxList.contains(gotoZuobiao))
        {
            return false;
        }
        
        return true;
    }
    
    public boolean isExist(ArrayList<Zuobiao> boxs, Zuobiao box, AspectEnum aspect)
    {
        Zuobiao man = box.myClone();
        // �ƶ����ӣ��õ��ƶ�����б������ִ���ԭ
        box.moveByAspect(aspect);
        byte[] boxsStr = coverBox(boxs);
        
        ArrayList<Zuobiao> canGoCellsAfter = getPlayerCanGoCells(boxs, man);
        byte[] manStr = coverMan(canGoCellsAfter, boxsStr);
        
        // �ƶ����ӣ��õ��ƶ�����б������ִ���ԭ
        box.backByAspect(aspect);
        
        return isExist(manStr);
    }
    
    private MapFliter fliter = new BloomFliter();
    
    /**
     * �Ƿ�ɹ�����set����ʾ�������ظ�
     * 
     * @param mapStr
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean isExist(byte[] all)
    {
        return fliter.isExist(all);
    }
    
    
    /**
     * ���������׵����ĵ�ͼ�������С�
     * 
     * @param mans
     * @param mapByte
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public byte[] coverMan(ArrayList<Zuobiao> mans, byte[] mapByte)
    {
        for (Zuobiao b : mans)
        {
            int index = getLen(b);
            if (mapByte[index] == 1)
            {
                throw new MyException("�������ظ���");
            }
            mapByte[index] = 2;
        }
        if (Logger.isdebug)
        {
            Logger.debug(Arrays.toString(mapByte));
        }
        return mapByte;
    }
    
    // ��ͼ�п���վ�˵�����
    static HashSet<Zuobiao> emptyList = new HashSet<Zuobiao>();
    
    /**
     * ��ȡ������ƶ�����λ��
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public ArrayList<Zuobiao> getPlayerCanGoCells(ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        // ��ͼ�п���վ�˵�����
        emptyList.addAll(this.manCanGoCells);
        
        // ���Ƴ������б��λ��
        emptyList.removeAll(boxList);
        
        ArrayList<Zuobiao> canGoList = new ArrayList<Zuobiao>();
        // ��������ڵ�λ�÷������ȥ��set��
        canGoList.add(man);
        emptyList.remove(man);
        
        // ������������
        int count = 1;
        // ��ʼ������
        int start = 0;
        while (count > 0)
        {
            int addTemp = 0;
            // ѭ����һ��ѭ�����ӵ����꣬�����Ƿ��ǿ�
            while (count-- > 0)
            {
                Zuobiao canGo = canGoList.get(start++);
                for (AspectEnum asp : AspectEnum.values())
                {
                    Zuobiao manEdge = getMove(canGo, asp);
                    
                    if (emptyList.contains(manEdge))
                    {
                        emptyList.remove(manEdge);
                        canGoList.add(manEdge);
                        addTemp++;
                    }
                }
            }
            count = addTemp;
        }
        
        if (Logger.isdebug)
        {
            StringBuilder mapStr = mapStr();
            for (Zuobiao zuobiao : boxList)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "B", max_x);
            }
            for (Zuobiao zuobiao : canGoList)
            {
                Util.replaceZuobiao(mapStr, zuobiao, "a", max_x);
            }
            
            Logger.info(Util.drawMap(mapStr, max_x));
        }
        emptyList.clear();
        return canGoList;
    }
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public byte[] coverBox(ArrayList<Zuobiao> coll)
    {
        byte[] desc = new byte[(max_x + 1) * (max_y + 1)];
        for (Zuobiao b : coll)
        {
            int i = getLen(b);
            desc[i] = 1;
        }
        if (Logger.isdebug)
        {
            Logger.debug(Arrays.toString(desc));
        }
        return desc;
    }
    
    /**
     * ת����ά����Ϊһά��������
     * 
     * @param b
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private int getLen(Zuobiao b)
    {
        int index = b.getX() + (b.getY() * (max_x + 1));
        return index;
    }
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public StringBuilder mapStr()
    {
        Cell[][] map = thisStepMap;
        
        StringBuilder buid = new StringBuilder();
        for (int j = 0; j < map[0].length; j++)
        {
            for (int i = 0; i < map.length; i++)
            {
                buid.append(map[i][j].draw());
            }
        }
        return buid;
    }
    
    /**
     * ���ݷ����ƶ�����,����һ��new���󣬲��޸���Ρ�
     * 
     * @param moveItem ����
     * @param asp ����
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public Zuobiao getMove(Zuobiao moveItem, AspectEnum asp)
    {
        Zuobiao result = null;
        switch (asp)
        {
            case up:
                result = getUp(moveItem);
                break;
            case down:
                result = getDown(moveItem);
                break;
            case left:
                result = getLeft(moveItem);
                break;
            default:
                result = getRight(moveItem);
                break;
        }
        if (out(result))
        {
            return null;
        }
        return result;
    }
    
    /**
     * ��ȡ�ƶ�����ʱ����վ�����ꡣ���޸���Ρ�
     * 
     * @param s ��������
     * @param asp �����ӷ���
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public Zuobiao getMovePlayer(Zuobiao s, AspectEnum asp)
    {
        switch (asp)
        {
            case up:
                return getDown(s);
            case down:
                return getUp(s);
            case left:
                return getRight(s);
            default:
                return getLeft(s);
        }
    }
    
    private static Zuobiao getUp(Zuobiao s)
    {
        return new Zuobiao(s.getX(), s.getY() - 1);
    }
    
    private static Zuobiao getDown(Zuobiao s)
    {
        return new Zuobiao(s.getX(), s.getY() + 1);
    }
    
    private static Zuobiao getLeft(Zuobiao s)
    {
        return new Zuobiao(s.getX() - 1, s.getY());
    }
    
    private static Zuobiao getRight(Zuobiao s)
    {
        return new Zuobiao(s.getX() + 1, s.getY());
    }
    
    /**
     * �ж�box�Ƿ����
     * 
     * @param box
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean out(Zuobiao box)
    {
        if (box.getX() < 0 || box.getX() > this.max_x)
        {
            return true;
        }
        if (box.getY() < 0 || box.getY() > this.max_y)
        {
            return true;
        }
        return false;
    }
    
}
