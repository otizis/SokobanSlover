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
 * �߷�
 * 
 * @author yejiangtao
 * @version [�汾��, 2015��9��19��]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class Solution
{
    // �ƶ���box�����
    private int boxIndex = -1;
    
    // �㷨�ı��
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
     * ��ȡ����������λ���б�
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public ArrayList<Zuobiao> getBoxListAfter()
    {
        // ���Ǹ��ڵ㣬ֱ�ӷ���
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
     * ��ȡ���߷�ǰ�ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
     * @return ���� key
     */
    public String getKey()
    {
        return key;
    }
    
    /**
     * @return ���� lastSolution
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
            // ��ʼλ��
            return SokoMap.man;
        }
        // �������ӵ�λ�ã�����������˵�λ��
        return getBoxListBefor().get(boxIndex);
    }
    
    /**
     * @return ���� result
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
        
        // �ж��Ƿ����
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
     * ��ȡ�ò�������box�б� �������ߣ���������������ѭ��������null�������սᡣ
     * 
     * @param boxList
     *            
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
            Logger.debug("����" + key + "����ظ����������Ž�");
            return null;
        }
        if (Logger.isDebugEnable())
        {
            Logger.debug("�����");
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
