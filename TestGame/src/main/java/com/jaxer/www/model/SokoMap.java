package com.jaxer.www.model;

import com.jaxer.www.Filter.BloomFliter;
import com.jaxer.www.Filter.TurnOffFliter;
import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.gif.AnimatedGifEncoder;
import com.jaxer.www.interfaces.MapFliter;
import com.jaxer.www.interfaces.MapLib;
import com.jaxer.www.manager.SolutionManager;
import com.jaxer.www.myexception.MyException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

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
        // 克隆的单箱子小图，可以取基数小一点的数
        fliter = new BloomFliter(100 * 10000);
    }

    /**
     * 带参数，使用小的fliter
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
     * 根据输入构造地图
     */
    public SokoMap(MapLib map)
    {
        String mapStr = map.getStandMapStr();
        if (!mapStr.contains("P"))
        {
            throw new MyException("没有放置玩家位置。");
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
     * 解法中，地图坐标为空，且不是box其中之一，返回true
     *
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
     * @return
     * @see [类、类#方法、类#成员]
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
     * @return 返回 deadSet
     */
    public HashSet<Zuobiao> getDeadSet()
    {
        return deadSet;
    }

    /**
     * 获取一帧图片
     *
     * @param stepNum  总步数
     * @param fontSize 字体大小
     * @param w        图片宽度
     * @param h        图片高度
     * @param i        该步数
     * @param str      图形字符串
     * @return
     * @see [类、类#方法、类#成员]
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
     * 根据方向移动坐标,返回一个new对象，不修改入参。 <br/>
     * <b>请勿修改返回值的坐标。</b>
     *
     * @param moveItem 坐标
     * @param asp      方向
     * @return
     * @see [类、类#方法、类#成员]
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
     * 获取推动箱子时，人站的坐标。不修改入参。
     *
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
     * @return 返回 nextSolutionList
     */
    public LinkedList<Solution> getNextSolutionList()
    {
        return nextSolutionList;
    }

    public void removeManCango(Zuobiao zb)
    {
        manCanGoFastSet.remove(zb);
        manCanGoCells.remove(zb);
        deadSet.remove(zb);
    }

    private FastSet manCanGoFastSet;

    /**
     * 获取当前状态下，地图的分块图
     * 同编号的认为是一块地，这块地上人可以到达，其他块不能到达。
     */
    public IndexMap getIndexMap(List<Zuobiao> boxList, Zuobiao man)
    {
        // 最后一位留给玩家
        short[] mapBytes = new short[(max_x + 1) * (max_y + 1) + 1];
        // 空地为0，有墙或箱子为-1
        Arrays.fill(mapBytes, (short)-1);

        int size = manCanGoCells.size();
        for (int i = 0; i < size; i++)
        {
            Zuobiao next = manCanGoCells.get(i);
            int index = getLen(next);
            mapBytes[index] = 0;
        }

        size = boxList.size();
        for (int i = 0; i < size; i++)
        {
            Zuobiao next = boxList.get(i);
            int index = getLen(next);
            mapBytes[index] = -1;
        }

        int len = mapBytes.length - 1;
        for (short i = 0; i < len; i++)
        {
            if (mapBytes[i] == -1)
            {
                continue;
            }
            short max_x_1 = (short)(max_x + 1);
            if (i % max_x_1 == 0)
            {

                mapBytes[i] = i;
                continue;
            }

            if (mapBytes[i] == 0)
            {
                if (i - max_x_1 < 0 || mapBytes[i - max_x_1] == -1)
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
                else
                {
                    short root = mapBytes[i - max_x_1];
                    while (root != mapBytes[root])
                    {
                        root = mapBytes[root];
                    }

                    if (mapBytes[i - 1] == -1)
                    {
                        mapBytes[i] = root;
                    }
                    else
                    {
                        short index = mapBytes[i - 1];
                        while (index != mapBytes[index])
                        {
                            index = mapBytes[index];
                        }
                        if (index == root)
                        {

                            mapBytes[i] = mapBytes[i - 1];
                        }
                        else if (index > root)
                        {
                            mapBytes[i] = root;
                            mapBytes[index] = root;
                        }
                        else
                        {
                            mapBytes[i] = index;
                            mapBytes[root] = index;
                        }
                    }

                }
            }

        }
        return new IndexMap(mapBytes, max_x, man);
    }

    /**
     * 获取玩家能移动到的位置
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public FastSet getPlayerCanGoCells(ArrayList<Zuobiao> boxList, Zuobiao man)
    {
        if (manCanGoFastSet == null)
        {
            manCanGoFastSet = new FastSet(this.max_x, this.max_y);
            // 地图中可以站人的坐标
            manCanGoFastSet.addAll(this.manCanGoCells);
        }
        FastSet meightGo = manCanGoFastSet.clone();
        FastSet result = new FastSet(this.max_x, this.max_y);
        // 先移出箱子列表的位置
        meightGo.removeAll(boxList);

        ArrayList<Zuobiao> canGoList = new ArrayList<Zuobiao>();
        // 把玩家现在的位置放入可以去的set中
        canGoList.add(man);
        result.add(man);
        meightGo.remove(man);

        // 开始的坐标
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
     * 获取死点列表
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<Zuobiao> getPointDieList()
    {
        return new ArrayList<Zuobiao>(deadSet);
    }

    /**
     * @return 返回 success
     */
    public Solution getSuccess()
    {
        return success;
    }

    /**
     * @return 返回 thisStepMap
     */
    public Cell[][] getThisStepMap()
    {
        return mapCells;
    }

    /**
     * 是否在之前出现过该状态的地图
     */
    public boolean isExist(ArrayList<Zuobiao> boxs, Zuobiao man)
    {
        // 移动箱子，得到移动后的列表
        IndexMap indexMap = getIndexMap(boxs, man);
        short[] mapShort = indexMap.getMapBytes();

        byte[] manStr = new byte[mapShort.length << 1];

        for (int i = 0; i < mapShort.length; i++)
        {
            manStr[(i << 1) + 1] = (byte)(mapShort[i] >> 8);
            manStr[i << 1] = (byte)(mapShort[i] >> 0);

        }
        return fliter.isExist(manStr);
    }

    /**
     * 是否在之前出现过该状态的地图
     */
    public boolean isExist(IndexMap indexMap)
    {
        // 移动箱子，得到移动后的列表
        short[] mapShort = indexMap.getMapBytes();

        byte[] manStr = new byte[mapShort.length << 1];

        for (int i = 0; i < mapShort.length; i++)
        {
            manStr[(i << 1) + 1] = (byte)(mapShort[i] >> 8);
            manStr[i << 1] = (byte)(mapShort[i] >> 0);

        }
        return fliter.isExist(manStr);
    }

    /**
     * 是否在之前出现过该状态的地图
     *
     * @param boxs
     * @return boolean [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isExist2(ArrayList<Zuobiao> boxs, Zuobiao man)
    {
        // 移动箱子，得到移动后的列表
        IndexMap indexMap = getIndexMap(boxs, man);
        short[] mapShort = indexMap.getMapBytes();

        byte[] manStr = new byte[mapShort.length];
        for (int i = 0; i < mapShort.length; i++)
        {
            manStr[i] = (byte)mapShort[i];
        }
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
     * @param y   行数
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
     * 输出结果
     *
     * @see [类、类#方法、类#成员]
     */
    public void outputResult(String gifName)
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

                // 生成推动后的
                str = SolutionManager.drawAfter(gonglv.get(i), this);
                g_oImage = getGifFrame(stepNum, fontSize, width, height, i, str);
                e.addFrame(g_oImage);

            }
            e.finish();
        }

    }

    /**
     * 运行算法
     *
     * @see [类、类#方法、类#成员]
     */
    public void run(String gifName)
    {

        Long begin = System.currentTimeMillis();

        // 单点死点推算
        this.deadSet = DeadPoitUtil.loadDeadSet(this);

        Solution solution = new Solution();
        Logger.info(SolutionManager.drawBefore(solution, this));
        // 等效于墙的空点
        DeadPoitUtil.convertWall(this);

        Logger.info(SolutionManager.drawBefore(solution, this));

        // 多点死点推算
        // DeadPoitUtil.roundDeadPoitSet(this);

        SolutionManager.runByLevel(this);

        while (nextSolutionList != null)
        {
            SolutionManager.runLeft(this);
        }
        Long end = System.currentTimeMillis();

        outputResult(gifName + (end - begin));

        System.out.println("耗时：" + (end - begin) + "ms");

    }

    /**
     */
    public void setNextSolutionList(LinkedList<Solution> nextSolutionList)
    {
        this.nextSolutionList = nextSolutionList;
    }

    /**
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
