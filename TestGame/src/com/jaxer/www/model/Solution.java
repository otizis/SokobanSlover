package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.enums.AspectEnum;

/**
 * 走法
 * 
 * @author yejiangtao
 * @version [版本号, 2015年9月19日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Solution
{
    /**
     * @return 返回 key
     */
    public String getKey()
    {
        return key;
    }
    
    // 算法的编号
    String key = "0";
    
    private Solution lastSolution;
    
    public int level;
    
    private int statue_x;
    
    private int statue_y;
    
    private AspectEnum step;
    
    private Result result;
    
    /**
     * @return 返回 result
     */
    public Result getResult()
    {
        return result;
    }
    
    /**
     * 只有root的有值，其他为null
     */
    private Cell[][] sokoMap;
    
    public Solution(Cell[][] thisStepMap)
    {
        super();
        this.sokoMap = thisStepMap;
    }
    
    public Solution(AspectEnum step, int moveCellx, int moveCelly,
        Solution lastSolution)
    {
        super();
        this.step = step;
        this.statue_x = moveCellx;
        this.statue_y = moveCelly;
        this.lastSolution = lastSolution;
        if (lastSolution != null)
        {
            this.level = lastSolution.level + 1;
        }
        this.key = Util.getSolutionKey(lastSolution.key);
    }
    
    /**
     * @return 返回 lastSolution
     */
    public Solution getLastSolution()
    {
        return lastSolution;
    }
    
    /**
     * 获取步骤后的地图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Cell[][] getSokoMapAfter()
    {
        Solution root = this.lastSolution;
        if (null == root)
        {
            return Util.cloneMap(this.sokoMap);
        }
        ArrayList<Solution> list = new ArrayList<Solution>();
        
        list.add(this);
        
        while (root.lastSolution != null)
        {
            list.add(0, root);
            root = root.lastSolution;
            
        }
        Cell[][] cloneMap = Util.cloneMapClearPlayer(root.sokoMap);
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).work(cloneMap);
        }
        // 推动后，人站在原来雕像的位置
        cloneMap[statue_x][statue_y].setPlayer();
        return cloneMap;
    }
    
    /**
     * 获取该走法前的地图 
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Cell[][] getSokoMapBefor()
    {
        if(null == this.lastSolution){
            return sokoMap;
        }
        return this.lastSolution.getSokoMapAfter();
    }
    
    private void work(Cell[][] cloneMap)
    {
        cloneMap[statue_x][statue_y].move(step, cloneMap);
        
    }
    
    /**
     * 看是否所有雕像都在目标点上
     * 
     * @param nextMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean isMapFinish(Cell[][] nextMap)
    {
        for (Cell[] cells : nextMap)
        {
            for (Cell cell : cells)
            {
                if (cell.isStatue())
                {
                    if (!cell.isGole())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void play(Cell[][] sokoMap)
    {
        Logger.debug(this.toString());
        
        sokoMap = stepToMap(sokoMap);
        
        if (null == sokoMap)
        {
            this.result = Result.failue;
            return;
        }
        
        // 判断是否完成
        boolean allFinish = isMapFinish(sokoMap);
        
        if (allFinish)
        {
            this.result = Result.success;
            return;
        }
        
        this.result = Result.needsub;
    }
    
    /**
     * 获取该步走完后的地图。 若不能走，或者走完属于死循环，返回null，方案终结。
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Cell[][] stepToMap(Cell[][] sokoMap)
    {
        
        Cell statue = sokoMap[statue_x][statue_y];
        if (statue.move(step, sokoMap))
        {
            String mapStr = Util.descMap(sokoMap);
            if (!Util.putIfAb(mapStr, key))
            {
                Logger.debug("以上" + key + "结果重复，或不是最优解");
                return null;
            }
            if (Logger.isDebugEnable())
            {
                Logger.debug("走完后：");
                Logger.debug(this.toString());
            }
            return sokoMap;
        }
        return null;
        
    }
    
    /** {@inheritDoc} */
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("第");
        builder.append(level);
        builder.append("步：");
        builder.append("[");
        builder.append(statue_x);
        builder.append(",");
        builder.append(statue_y);
        builder.append("]");
        builder.append(step == null ? "" : step.getDesc());
        
        return builder.toString();
    }
    
}
