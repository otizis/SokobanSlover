package com.jaxer.www.model;

import java.util.ArrayList;

import com.jaxer.www.Util.Util;
import com.jaxer.www.Util.ZuobiaoUtil;
import com.jaxer.www.enums.AspectEnum;
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
    
    private Solution lastSolution;
    
    public int level;
    
    private AspectEnum step;
    
    public Solution()
    {
        super();
    }
    
    public Solution(AspectEnum step, int boxIndex, Solution lastSolution)
    {
        super();
        this.step = step;
        this.boxIndex = boxIndex;
        this.lastSolution = lastSolution;
        if (lastSolution != null)
        {
            this.level = lastSolution.level + 1;
        }
    }
    
    /**
     * ���ݴ�������λ�ã�������λ�û�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public String draw(Zuobiao man, ArrayList<Zuobiao> boxList)
    {
        StringBuilder builder = Util.mapStr();
        Util.replaceZuobiao(builder, man, step == null ? "a" : step.getDesc());
        
        for (Zuobiao box : boxList)
        {
            Util.replaceZuobiao(builder, box, "B");
        }
        
        return Util.drawMap(builder);
    }
    
    /**
     * ���ز����ƶ�ǰ�ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public String drawAfter()
    {
        return draw(getManAfterStep(), getBoxListAfter());
    }
    
    /**
     * ���ز����ƶ�ǰ�ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public String drawBefore()
    {
        return draw(getManBeforeStep(), getBoxListBefore());
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
        Solution root = lastSolution;
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
     * ��ȡ�����б���ˣ��˵�λ�÷���0
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    /**
     * <һ�仰���ܼ���> <������ϸ����>
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public ArrayList<Zuobiao> getBoxAndManAfter()
    {
        ArrayList<Zuobiao> cloneBoxList = Util.cloneBoxList(SokoMap.boxList);
        
        // ���Ǹ��ڵ㣬ֱ�ӷ���
        Solution root = lastSolution;
        if (null == root)
        {
            cloneBoxList.add(0, SokoMap.man);
            return cloneBoxList;
        }
        
        ArrayList<Solution> solutList = new ArrayList<Solution>();
        
        solutList.add(this);
        
        while (root.lastSolution != null)
        {
            solutList.add(0, root);
            root = root.lastSolution;
        }
        
        Zuobiao man = null;
        int size = solutList.size();
        for (int i = 0; i < size; i++)
        {
            Solution solution = solutList.get(i);
            int moveIndex = solution.getBoxIndex();
            Zuobiao moveBox = cloneBoxList.get(moveIndex);
            moveBox.moveByAspect(solution.step);
            if ((i + 1) == size)
            {
                man = ZuobiaoUtil.getMovePlayer(moveBox, solution.step);
            }
        }
        
        cloneBoxList.add(0, man);
        return cloneBoxList;
        
    }
    
    /**
     * ��ȡ���߷�ǰ�ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public ArrayList<Zuobiao> getBoxListBefore()
    {
        if (null == lastSolution)
        {
            return Util.cloneBoxList(SokoMap.boxList);
        }
        return lastSolution.getBoxListAfter();
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
    public Zuobiao getManAfterStep()
    {
        if (lastSolution == null)
        {
            // ��ʼλ��
            return SokoMap.man;
        }
        // �������ӵ�λ�ã�����������˵�λ��
        return getBoxListBefore().get(boxIndex);
    }
    
    /**
     * @return
     */
    public Zuobiao getManBeforeStep()
    {
        if (lastSolution == null)
        {
            return SokoMap.man;
        }
        Zuobiao manAfter = getManAfterStep();
        return ZuobiaoUtil.getMovePlayer(manAfter, step);
    }
    
    public AspectEnum getStep()
    {
        return this.step;
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
        builder.append(getBoxListBefore().get(boxIndex));
        builder.append(", level=");
        builder.append(level);
        builder.append(", step=");
        builder.append(step);
        builder.append("]");
        return builder.toString();
    }
    
}
