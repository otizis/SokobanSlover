package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jaxer.www.Logger;
import com.jaxer.www.SolutionFactory;
import com.jaxer.www.Util;
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
    public static void sort(ArrayList<Solution> nextSolutions2)
    {
        Collections.sort(nextSolutions2, new Comparator<Solution>()
        {
            
            @Override
            public int compare(Solution o1, Solution o2)
            {
                int o1int =
                    o1.thisStepMap[o1.moveCellx][o1.moveCelly].isGole() ? 1 : 0;
                int o2int =
                    o2.thisStepMap[o2.moveCellx][o2.moveCelly].isGole() ? 1 : 0;
                return o1int - o2int;
            }
            
        });
        
    }
    
    private Solution lastSolution;
    
    public int level;
    
    private int moveCellx;
    
    private int moveCelly;
    
    private AspectEnum step;
    
    private Result result;
    
    /**
     * @return 返回 result
     */
    public Result getResult()
    {
        return result;
    }
    
    private Cell[][] thisStepMap;
    
    public Solution(Cell[][] thisStepMap)
    {
        super();
        this.thisStepMap = thisStepMap;
    }
    
    public Solution(Cell[][] thisStepMap, AspectEnum step, int moveCellx,
        int moveCelly, Solution lastSolution)
    {
        super();
        this.thisStepMap = thisStepMap;
        this.step = step;
        this.moveCellx = moveCellx;
        this.moveCelly = moveCelly;
        this.lastSolution = lastSolution;
        if (lastSolution != null)
        {
            this.level = lastSolution.level + 1;
        }
    }
    
    /**
     * @return 返回 lastSolution
     */
    public Solution getLastSolution()
    {
        return lastSolution;
    }
    
    /**
     * @return 返回 thisStepMap
     */
    public Cell[][] getThisStepMap()
    {
        return thisStepMap;
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
    
    public void play()
    {
        Logger.debug(this.toString());
        
        thisStepMap = stepToMap();
        
        if (null == thisStepMap)
        {
            this.result = Result.failue;
            return;
        }
        
        if (SolutionFactory.isdead(this))
        {
            this.result = Result.failue;
            return;
        }
        // 判断是否完成
        boolean allFinish = isMapFinish(thisStepMap);
        
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
    public Cell[][] stepToMap()
    {
        
        Cell statue = thisStepMap[moveCellx][moveCelly];
        if (statue.move(step, thisStepMap))
        {
            String mapStr = Util.descMap(thisStepMap);
            if (!Util.putIfAb(mapStr))
            {
                Logger.debug("以上结果重复，或不是最优解其他");
                return null;
            }
            if (Logger.isDebugEnable())
            {
                Logger.debug("走完后：");
                Logger.debug(this.toString());
            }
            return thisStepMap;
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
        builder.append(moveCellx);
        builder.append(",");
        builder.append(moveCelly);
        builder.append("]");
        builder.append(step == null ? "" : step.getDesc());
        
        builder.append("\n");
        for (int i = 0; i < thisStepMap[0].length; i++)
        {
            for (int j = 0; j < thisStepMap.length; j++)
            {
                if (level != 0 && j == moveCellx && i == moveCelly)
                {
                    builder.append(step == null ? "　" : step.getDesc());
                }
                else
                {
                    builder.append(thisStepMap[j][i].draw());
                }
            }
            builder.append("\n");
        }
        builder.append("----------------------");
        return builder.toString();
    }
    
}
