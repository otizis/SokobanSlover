package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
    private Solution success;
    
    /**
     * @return 返回 success
     */
    public Solution getSuccess()
    {
        return success;
    }
    
    /**
     * @param 对success进行赋值
     */
    public void setSuccess(Solution success)
    {
        this.success = success;
    }
    
    public static final char box = 'B';
    
    public static final char boxOnGoal = 'R';
    
    public static final char empty = 'S';
    
    public static final char goal = 'G';
    
    public static final char player = 'P';
    
    public static final char playerOnGoal = 'L';
    
    public static final char wall = 'M';
    
    private ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>();
    
    HashSet<Zuobiao> deadSet = null;
    
    private MapFliter fliter = new BloomFliter();
    
    private ArrayList<Zuobiao> goleList = new ArrayList<Zuobiao>();
    
    private Zuobiao man;
    
    private ArrayList<Zuobiao> manCanGoCells = new ArrayList<Zuobiao>();
    
    private int max_x;
    
    private int max_y;
    
    private Cell[][] mapCells;
    
    public SokoMap(SokoMap soure, ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        this.mapCells = soure.mapCells;
        this.boxList = boxList;
        this.man = man;
        this.deadSet = new HashSet<Zuobiao>();
        this.goleList = Util.cloneBoxList(soure.goleList);
        this.manCanGoCells = Util.cloneBoxList(soure.manCanGoCells);
        this.max_x = soure.max_x;
        this.max_y = soure.max_y;
    }
    
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
        max_x = mapCells.length - 1;
        max_y = mapCells[0].length - 1;
    }
    
    /**
     * 解法中，地图坐标为空，且不是box其中之一，返回true
     * 
     * 
     * @param solu
     * @param gotoCell
     * @see [类、类#方法、类#成员]
     */
    public boolean canGo(ArrayList<Zuobiao> boxList, Zuobiao gotoZuobiao)
    {
        if (gotoZuobiao == null)
        {
            return false;
        }
        // 目标位，不能是墙
        if (getCell(gotoZuobiao).check(CellType.wall))
        {
            return false;
            
        }
        // 目标位不是箱子
        if (boxList.contains(gotoZuobiao))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
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
     * 将人坐标打孔到给的地图特征码中。
     * 
     * @param mans
     * @param mapByte
     * @return
     * @see [类、类#方法、类#成员]
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
                    throw new MyException("描述有重复。");
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
     * @return 返回 boxList
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
     * @return 返回 goleList
     */
    public ArrayList<Zuobiao> getGoleList()
    {
        return goleList;
    }
    
    /**
     * 转化二维坐标为一维数组的序号
     * 
     * @param b
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int getLen(Zuobiao b)
    {
        int index = b.getX() + (b.getY() * (max_x + 1));
        return index;
    }
    
    /**
     * @return 返回 man
     */
    public Zuobiao getMan()
    {
        return man;
    }
    
    /**
     * @return 返回 manCanGoCells
     */
    public ArrayList<Zuobiao> getManCanGoCells()
    {
        return manCanGoCells;
    }
    
    /**
     * @return 返回 max_x
     */
    public int getMax_x()
    {
        return max_x;
    }
    
    /**
     * @return 返回 max_y
     */
    public int getMax_y()
    {
        return max_y;
    }
    
    /**
     * 根据方向移动坐标,返回一个new对象，不修改入参。
     * 
     * @param moveItem 坐标
     * @param asp 方向
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Zuobiao getMove(Zuobiao moveItem, AspectEnum asp)
    {
        Zuobiao result = null;
        switch (asp)
        {
            case up:
                result = moveItem.getUp();
                break;
            case down:
                result = moveItem.getDown();
                break;
            case left:
                result = moveItem.getLeft();
                break;
            default:
                result = moveItem.getRight();
                break;
        }
        if (out(result))
        {
            return null;
        }
        return result;
    }
    
    /**
     * 获取推动箱子时，人站的坐标。不修改入参。
     * 
     * @param s 箱子坐标
     * @param asp 推箱子方向
     * @return
     * @see [类、类#方法、类#成员]
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
     * 获取玩家能移动到的位置
     * 
     * @param curMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    public FastSet getPlayerCanGoCells(ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        FastSet fastSet = new FastSet(this.max_x, this.max_y);
        // 地图中可以站人的坐标
        fastSet.addAll(this.manCanGoCells);
        
        // 先移出箱子列表的位置
        fastSet.removeAll(boxList);
        
        ArrayList<Zuobiao> canGoList = new ArrayList<Zuobiao>();
        // 把玩家现在的位置放入可以去的set中
        canGoList.add(man);
        fastSet.remove(man);
        
        // 新增的坐标数
        int count = 1;
        // 开始的坐标
        int start = 0;
        while (count > 0)
        {
            int addTemp = 0;
            // 循环上一次循环增加的坐标，四周是否是空
            while (count-- > 0)
            {
                Zuobiao canGo = canGoList.get(start++);
                for (AspectEnum asp : AspectEnum.values())
                {
                    Zuobiao manEdge = getMove(canGo, asp);
                    
                    if (manEdge != null && fastSet.contains(manEdge))
                    {
                        fastSet.remove(manEdge);
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
        fastSet.clear();
        fastSet.addAll(canGoList);
        return fastSet;
    }
    
    /**
     * @return 返回 thisStepMap
     */
    public Cell[][] getThisStepMap()
    {
        return mapCells;
    }
    
    public boolean isExist(ArrayList<Zuobiao> boxs, Zuobiao box, AspectEnum aspect)
    {
        Zuobiao man = box.myClone();
        // 移动箱子，得到移动后的列表，生成字串后复原
        box.moveByAspect(aspect);
        byte[] boxsStr = coverBox(boxs);
        
        FastSet canGoCellsAfter = getPlayerCanGoCells(boxs, man);
        byte[] manStr = coverMan(canGoCellsAfter, boxsStr);
        
        // 移动箱子，得到移动后的列表，生成字串后复原
        box.backByAspect(aspect);
        
        return fliter.isExist(manStr);
    }
    
    /**
     * 该点位是否是死点,墙角
     * 
     * @param zb
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isPointDie(Zuobiao zb)
    {
        return deadSet.contains(zb);
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
                    throw new MyException("已经存在玩家位置，" + man);
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
                    throw new MyException("已经存在玩家位置，" + man);
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
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
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
     * 判断box是否出界
     * 
     * @param box
     * @return
     * @see [类、类#方法、类#成员]
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
     * 运行算法
     * 
     * @param map
     * @see [类、类#方法、类#成员]
     */
    public void run()
    {
        
        Long begin = System.currentTimeMillis();
        
        this.deadSet = DeadPoitUtil.loadDeadSet(this);
        
        Solution solution = new Solution();
        
        Logger.info(SolutionManager.drawBefore(solution, this));
        
        SolutionManager.runByLevel(this);
        
        Long end = System.currentTimeMillis();
        
        outputResult();
        
        System.out.println("耗时：" + (end - begin));
        
    }
    
    /**
     * 输出结果
     * 
     * @param lastOne
     *            
     * @see [类、类#方法、类#成员]
     */
    public void outputResult()
    {
        if (success == null)
        {
            System.out.println("无解");
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
            
            for (int i = 0; i < gonglv.size(); i++)
            {
                System.out.println(i);
                System.out.println(SolutionManager.drawBefore(gonglv.get(i), this));
            }
        }
        
    }
    
}
