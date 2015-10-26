package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.HashSet;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.SolutionFactory;
import com.jaxer.www.Util.Util;
import com.jaxer.www.Util.ZuobiaoUtil;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.enums.Result;

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
    // 移动的box的序号
    private int boxIndex = -1;
    
    // 算法的编号
    String key = "0";
    
    private Solution lastSolution;
    
    public int level;
    
    private Result result;
    
    private AspectEnum step;
    
    public Solution(AspectEnum step, int boxIndex, Solution lastSolution)
    {
        super();
        this.step = step;
        this.boxIndex = boxIndex;
        this.lastSolution = lastSolution;
        if (lastSolution != null)
        {
            this.level = lastSolution.level + 1;
            this.key = Util.getSolutionKey(lastSolution.key);
        }
    }
    
    public Solution()
    {
        super();
    }
    
    public int getBoxIndex()
    {
        return boxIndex;
    }
    
    /**
     * 获取步骤后的箱子位置列表
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<Zuobiao> getBoxListAfter()
    {
        // 若是根节点，直接返回
        Solution root = this.lastSolution;
        if (null == root)
        {
            return Util.cloneBoxList(SokoMap.boxList);
        }
        
        ArrayList<Solution> solutList = new ArrayList<Solution>();
        
        solutList.add(this);
        
        while (root.lastSolution != null)
        {
            solutList.add(0, root);
            root = root.lastSolution;
            
        }
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(SokoMap.boxList);
        
        for (int i = 0; i < solutList.size(); i++)
        {
            int moveIndex = solutList.get(i).getBoxIndex();
            Zuobiao moveBox = cloneBoxList.get(moveIndex);
            moveBox.moveByAspect(solutList.get(i).step);
        }
        return cloneBoxList;
    }
    
    /**
     * 获取该走法前的地图
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<Zuobiao> getBoxListBefor()
    {
        if (null == this.lastSolution)
        {
            return Util.cloneBoxList(SokoMap.boxList);
        }
        return this.lastSolution.getBoxListAfter();
    }
    
    /**
     * @return 返回 key
     */
    public String getKey()
    {
        return key;
    }
    
    /**
     * @return 返回 lastSolution
     */
    public Solution getLastSolution()
    {
        return lastSolution;
    }
    
    /**
     * @return
     */
    public Zuobiao getManBeforStep()
    {
        if (lastSolution == null)
        {
            return SokoMap.man;
        }
        Zuobiao manAfter = getManAfterStep();
        return ZuobiaoUtil.getMovePlayer(manAfter, step);
    }
    
    /**
     * @return
     */
    public Zuobiao getManAfterStep()
    {
        if (lastSolution == null)
        {
            // 初始位置
            return SokoMap.man;
        }
        // 现在箱子的位置，就是走完后人的位置
        return getBoxListBefor().get(boxIndex);
    }
    
    /**
     * @return 返回 result
     */
    public Result getResult()
    {
        return result;
    }
    
    public void play()
    {
        Logger.debug(this.toString());
        
        ArrayList<Zuobiao> boxListAfterStep = stepToBox(getBoxListBefor());
        
        if (null == boxListAfterStep)
        {
            this.result = Result.failue;
            return;
        }
        
        // 判断是否完成
        boolean allFinish = isAllBoxGoal(boxListAfterStep);
        
        if (allFinish)
        {
            this.result = Result.success;
            return;
        }
        
        this.result = Result.needsub;
    }
    
    private boolean isAllBoxGoal(ArrayList<Zuobiao> boxListAfterStep)
    {
        for (Zuobiao zuobiao : boxListAfterStep)
        {
            if (!SokoMap.getCell(zuobiao).check(CellType.gole))
            {
                
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取该步走完后的box列表。 若不能走，或者走完属于死循环，返回null，方案终结。
     * 
     * @param boxList
     *            
     * @return
     * @see [类、类#方法、类#成员]
     */
    public ArrayList<Zuobiao> stepToBox(ArrayList<Zuobiao> boxList)
    {
        
        Zuobiao box = boxList.get(boxIndex);
        box.moveByAspect(step);
        if (ZuobiaoUtil.out(box))
        {
            return null;
        }
        
        String boxsStr = Util.descZuobiaoList(boxList);
        
        HashSet<Zuobiao> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(this);
            
        String manStr = Util.descZuobiaoList(allPlayerCanGoCells);
        
        if (!Util.putIfAb(boxsStr + "|" + manStr, key))
        {
            Logger.debug("以上" + key + "结果重复，或不是最优解");
            return null;
        }
        if (Logger.isDebugEnable())
        {
            Logger.debug("走完后：");
            this.drawAfter();
        }
        return boxList;
        
    }
    
    public void drawBefo()
    {
        draw(getManBeforStep(), getBoxListBefor());
    }
    
    public void drawAfter()
    {
        draw(getManAfterStep(), getBoxListAfter());
    }
    
    public void draw(Zuobiao man, ArrayList<Zuobiao> boxList)
    {
        StringBuilder builder = Util.mapStr();
        Util.replaceZuobiao(builder, man, "a");
        
        for (Zuobiao box : boxList)
        {
            Util.replaceZuobiao(builder, box, "B");
        }
        
        Util.drawMap(builder);
    }
    
    /** {@inheritDoc} */
    
    @Override
    public String toString()
    {
        if (lastSolution == null)
        {
            return "init one";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getBoxListBefor().get(boxIndex));
        // builder.append(", key=");
        // builder.append(key);
        builder.append(", level=");
        builder.append(level);
        builder.append(", step=");
        builder.append(step);
        builder.append("]");
        return builder.toString();
    }
    
}
