package com.jaxer.www.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.jaxer.www.Filter.BloomFliter;
import com.jaxer.www.Filter.TurnOffFliter;
import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.api.MapFliter;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.gif.AnimatedGifEncoder;
import com.jaxer.www.manager.SolutionManager;
import com.jaxer.www.myexception.MyException;

public class SokoMap
{
    public static final char box = 'B';
    
    public static final char boxOnGoal = 'R';
    
    public static final char empty = 'S';
    
    public static final char goal = 'G';
    
    public static final char player = 'P';
    
    public static final char playerOnGoal = 'L';
    
    public static final char wall = 'M';
    
    private ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>();
    
    HashSet<Zuobiao> deadSet = null;
    
    private MapFliter fliter;
    
    private ArrayList<Zuobiao> goleList = new ArrayList<Zuobiao>();
    
    private Zuobiao man;
    
    private ArrayList<Zuobiao> manCanGoCells = new ArrayList<Zuobiao>();
    
    private Cell[][] mapCells;
    
    private int max_x;
    
    private int max_y;
    
    private int mapByteLen;
    
    private LinkedList<Solution> nextSolutionList;
    
    private ArrayList<Zuobiao[]> roundDeadPoint;
    
    private Solution success;
    
    public SokoMap(SokoMap source, ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        this.mapCells = source.mapCells;
        this.boxList = boxList;
        this.man = man;
        this.deadSet = new HashSet<Zuobiao>();
        this.goleList = Util.cloneBoxList(source.goleList);
        this.manCanGoCells = Util.cloneBoxList(source.manCanGoCells);
        this.max_x = source.max_x;
        this.max_y = source.max_y;
        this.mapByteLen = source.mapByteLen;
        // ��¡�ĵ�����Сͼ������ȡ����Сһ�����
        fliter = new BloomFliter(100 * 10000);
    }
    
    /**
     * ��������ʹ��С��fliter
     */
    public SokoMap(SokoMap source, ArrayList<Zuobiao> boxList2, Zuobiao man2, boolean b)
    {
        this.mapCells = source.mapCells;
        this.boxList = boxList2;
        this.man = man2;
        this.deadSet = new HashSet<Zuobiao>();
        this.goleList = Util.cloneBoxList(source.goleList);
        this.manCanGoCells = Util.cloneBoxList(source.manCanGoCells);
        this.max_x = source.max_x;
        this.max_y = source.max_y;
        this.mapByteLen = source.mapByteLen;
        fliter = new TurnOffFliter();
    }
    
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
        fliter = new BloomFliter();
        max_x = mapCells.length - 1;
        max_y = mapCells[0].length - 1;
        mapByteLen = (max_x + 1) * (max_y + 1);
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
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public byte[] coverBox(ArrayList<Zuobiao> coll)
    {
        byte[] desc = new byte[mapByteLen];
        for (Zuobiao b : coll)
        {
            desc[getLen(b)] = 1;
        }
        if (Logger.isdebug)
        {
            Logger.debug(Arrays.toString(desc));
        }
        return desc;
    }
    
    /**
     * ���������׵����ĵ�ͼ�������С�
     * 
     * @param mans
     * @param mapByte
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public byte[] coverMan(FastSet mans, byte[] mapByte)
    {
        boolean[] mapBytes = mans.getMapBytes();
        
        for (int i = 0; i < mapBytes.length; i++)
        {
            if (mapBytes[i])
            {
                if (mapByte[i] == 1)
                {
                    throw new MyException("�������ظ���");
                }
                mapByte[i] = 2;
            }
        }
        if (Logger.isdebug)
        {
            Logger.debug(Arrays.toString(mapByte));
        }
        return mapByte;
    }
    
    /**
     * @return ���� boxList
     */
    public ArrayList<Zuobiao> getBoxList()
    {
        return boxList;
    }
    
    public Cell getCell(Zuobiao zb)
    {
        
        return mapCells[zb.x][zb.y];
    }
    
    /**
     * @return ���� deadSet
     */
    public HashSet<Zuobiao> getDeadSet()
    {
        return deadSet;
    }
    
    /**
     * ��ȡһ֡ͼƬ
     * 
     * @param stepNum �ܲ���
     * @param fontSize �����С
     * @param w ͼƬ���
     * @param h ͼƬ�߶�
     * @param i �ò���
     * @param str ͼ���ַ���
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private BufferedImage getGifFrame(int stepNum, int fontSize, int w, int h, int i, String str)
    {
        BufferedImage g_oImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics = g_oImage.createGraphics();
        
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, w, h);
        
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        graphics.drawString("step:" + i + "/" + stepNum, 2, fontSize);
        int y = fontSize;
        for (String line : str.split("\n"))
        {
            graphics.drawString(line, 2, fontSize += y);
        }
        graphics.dispose();
        return g_oImage;
    }
    
    /**
     * @return ���� goleList
     */
    public ArrayList<Zuobiao> getGoleList()
    {
        return goleList;
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
     * @return ���� man
     */
    public Zuobiao getMan()
    {
        return man;
    }
    
    /**
     * @return ���� manCanGoCells
     */
    public ArrayList<Zuobiao> getManCanGoCells()
    {
        return manCanGoCells;
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
     * ���ݷ����ƶ�����,����һ��new���󣬲��޸���Ρ� <br/>
     * <b>�����޸ķ���ֵ�����ꡣ</b>
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
                if (moveItem.y == 0)
                {
                    return null;
                }
                result = mapCells[moveItem.x][moveItem.y - 1];
                break;
            case down:
                if (moveItem.y == max_y)
                {
                    return null;
                }
                result = mapCells[moveItem.x][moveItem.y + 1];
                break;
            case left:
                if (moveItem.x == 0)
                {
                    return null;
                }
                result = mapCells[moveItem.x - 1][moveItem.y];
                break;
            default:
                if (moveItem.x == max_x)
                {
                    return null;
                }
                result = mapCells[moveItem.x + 1][moveItem.y];
                break;
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
    public Zuobiao getMovePlayer(Zuobiao moveItem, AspectEnum asp)
    {
        Zuobiao result = null;
        switch (asp)
        {
            case up:
                result = moveItem.getDown();
                break;
            case down:
                result = moveItem.getUp();
                break;
            case left:
                result = moveItem.getRight();
                break;
            default:
                result = moveItem.getLeft();
                break;
        }
        if (out(result))
        {
            return null;
        }
        return result;
    }
    
    /**
     * @return ���� nextSolutionList
     */
    public LinkedList<Solution> getNextSolutionList()
    {
        return nextSolutionList;
    }
    
    private FastSet manCanGoFastSet;
    
    public void removeManCango(Zuobiao zb)
    {
        manCanGoFastSet.remove(zb);
        manCanGoCells.remove(zb);
        deadSet.remove(zb);
    }
    
    /**
     * ��ȡ������ƶ�����λ��
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public FastSet getPlayerCanGoCells2(ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        int[] mapBytes = new int[(max_x + 1) * (max_y + 1)];
        Arrays.fill(mapBytes, -1);
        
        Iterator<Zuobiao> iterator = manCanGoCells.iterator();
        // �յ�Ϊ0����ǽ������Ϊ-1
        while (iterator.hasNext())
        {
            Zuobiao next = iterator.next();
            int index = getLen(next);
            mapBytes[index] = 0;
        }
        
        iterator = boxList.iterator();
        while (iterator.hasNext())
        {
            Zuobiao next = iterator.next();
            int index = getLen(next);
            mapBytes[index] = -1;
        }
        
        // ����
        for (int i = 0; i < mapBytes.length; i++)
        {
            if (i % max_x == 0)
            {
                mapBytes[i] = i;
                continue;
            }
            if (mapBytes[i] == 0)
            {
                if (mapBytes[i - 1] == -1)
                {
                    mapBytes[i] = i;
                }
                else
                {
                    mapBytes[i] = mapBytes[i - 1];
                }
            }
            
        }
        // ����
        for (int i = max_x + 1; i < mapBytes.length; i++)
        {
            
            if (mapBytes[i - max_x - 1] != -1)
            {
                // ��ǰ���ӵ�����
                int index = mapBytes[i];
                if (index == -1)
                {
                    continue;
                }
                // �ϲ�������
                int root = i - max_x - 1;
                while (root != mapBytes[root])
                {
                    root = mapBytes[root];
                }
                mapBytes[index] = root;
                
            }
            
        }
        int manIndex = mapBytes[mapBytes[getLen(man)]];
        FastSet result = new FastSet(this.max_x, this.max_y);
        for (int i = 0; i < mapBytes.length; i++)
        {
            if (mapBytes[i] == -1)
            {
                continue;
            }
            if (mapBytes[mapBytes[i]] == manIndex)
            {
                result.add(i);
            }
            
        }
        return result;
    }
    
    /**
     * ��ȡ������ƶ�����λ��
     * 
     * @param curMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public FastSet getPlayerCanGoCells(ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        if (manCanGoFastSet == null)
        {
            manCanGoFastSet = new FastSet(this.max_x, this.max_y);
            // ��ͼ�п���վ�˵�����
            manCanGoFastSet.addAll(this.manCanGoCells);
        }
        FastSet meightGo = manCanGoFastSet.clone();
        FastSet result = new FastSet(this.max_x, this.max_y);
        // ���Ƴ������б��λ��
        meightGo.removeAll(boxList);
        
        ArrayList<Zuobiao> canGoList = new ArrayList<Zuobiao>();
        // ��������ڵ�λ�÷������ȥ��set��
        canGoList.add(man);
        result.add(man);
        meightGo.remove(man);
        
        // ��ʼ������
        int start = 0;
        while (start < canGoList.size())
        {
            Zuobiao canGo = canGoList.get(start++);
            for (AspectEnum asp : AspectEnum.getAllEnum())
            {
                Zuobiao manEdge = getMove(canGo, asp);
                
                if (meightGo.contains(manEdge))
                {
                    meightGo.remove(manEdge);
                    canGoList.add(manEdge);
                    result.add(manEdge);
                }
            }
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
        return result;
    }
    
    /**
     * ��ȡ�����б�
     * 
     * @param zb
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public ArrayList<Zuobiao> getPointDieList()
    {
        return new ArrayList<Zuobiao>(deadSet);
    }
    
    /**
     * @return ���� success
     */
    public Solution getSuccess()
    {
        return success;
    }
    
    /**
     * @return ���� thisStepMap
     */
    public Cell[][] getThisStepMap()
    {
        return mapCells;
    }
    
    /**
     * �Ƿ���֮ǰ���ֹ���״̬�ĵ�ͼ
     * 
     * @param boxs
     * @return [����˵��]
     *         
     * @return boolean [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean isExist(ArrayList<Zuobiao> boxs, Zuobiao man)
    {
        // �ƶ����ӣ��õ��ƶ�����б�
        byte[] boxsStr = coverBox(boxs);
        
        FastSet canGoCellsAfter = getPlayerCanGoCells(boxs, man);
        byte[] manStr = coverMan(canGoCellsAfter, boxsStr);
        
        return fliter.isExist(manStr);
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
     * ����һ�����ӵ�����
     * 
     * @param lns
     * @param y
     * @param x
     * @see [�ࡢ��#��������#��Ա]
     */
    private void loadCell(String[] lns, int y, int x)
    {
        if (null == mapCells)
        {
            mapCells = new Cell[lns[y].length()][lns.length];
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
        mapCells[x][y] = new Cell(x, y, ctype);
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
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public StringBuilder mapStr()
    {
        Cell[][] map = mapCells;
        
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
    
    /**
     * ������
     * 
     * @param lastOne
     *            
     * @see [�ࡢ��#��������#��Ա]
     */
    public void outputResult(String gifName)
    {
        if (success == null)
        {
            System.out.println("�޽�");
        }
        else
        {
            ArrayList<Solution> gonglv = new ArrayList<Solution>();
            Solution solut = success;
            do
            {
                gonglv.add(0, solut);
                solut = solut.getLastSolution();
            } while (solut != null);
            
            int stepNum = gonglv.size() - 1;
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setFrameRate(2);
            e.setRepeat(0);
            e.start(gifName + "_" + stepNum + ".gif");
            
            int fontSize = 15;
            int width = (int)((max_x + 1) * 2 * (8 / 15d) * fontSize) + 1;
            int height = (max_y + 3) * fontSize + 3;
            for (int i = 0; i < gonglv.size(); i++)
            {
                System.out.println(i);
                String str = SolutionManager.drawBefore(gonglv.get(i), this);
                System.out.println(str);
                
                BufferedImage g_oImage = getGifFrame(stepNum, fontSize, width, height, i, str);
                e.addFrame(g_oImage);
                
                // �����ƶ����
                str = SolutionManager.drawAfter(gonglv.get(i), this);
                g_oImage = getGifFrame(stepNum, fontSize, width, height, i, str);
                e.addFrame(g_oImage);
                
            }
            e.finish();
        }
        
    }
    
    /**
     * �����㷨
     * 
     * @param map
     * @see [�ࡢ��#��������#��Ա]
     */
    public void run(String gifName)
    {
        
        Long begin = System.currentTimeMillis();
        
        // ������������
        this.deadSet = DeadPoitUtil.loadDeadSet(this);
        
        Solution solution = new Solution();
        Logger.info(SolutionManager.drawBefore(solution, this));
        // ��Ч��ǽ�Ŀյ�
        DeadPoitUtil.convertWall(this);
        
        Logger.info(SolutionManager.drawBefore(solution, this));
        
        // �����������
        // DeadPoitUtil.roundDeadPoitSet(this);
        
        SolutionManager.runByLevel(this);
        
        while (nextSolutionList != null)
        {
            SolutionManager.runLeft(this);
        }
        Long end = System.currentTimeMillis();
        
        outputResult(gifName + (end - begin));
        
        System.out.println("��ʱ��" + (end - begin) + "ms");
        
    }
    
    /**
     * @param ��nextSolutionList���и�ֵ
     */
    public void setNextSolutionList(LinkedList<Solution> nextSolutionList)
    {
        this.nextSolutionList = nextSolutionList;
    }
    
    /**
     * @param ��success���и�ֵ
     */
    public void setSuccess(Solution success)
    {
        this.success = success;
    }
    
    public void setRoundDeadPoint(ArrayList<Zuobiao[]> roundDeadPoint)
    {
        this.roundDeadPoint = roundDeadPoint;
    }
    
    public ArrayList<Zuobiao[]> getRoundDeadPoint()
    {
        return roundDeadPoint;
    }
}
