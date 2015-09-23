package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.Util;
import com.jaxer.www.enums.AspectEnum;

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
    /**
     * @return ���� key
     */
    public String getKey()
    {
        return key;
    }
    
    // �㷨�ı��
    String key = "0";
    
    private Solution lastSolution;
    
    public int level;
    
    private int statue_x;
    
    private int statue_y;
    
    private AspectEnum step;
    
    private Result result;
    
    /**
     * @return ���� result
     */
    public Result getResult()
    {
        return result;
    }
    
    /**
     * ֻ��root����ֵ������Ϊnull
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
     * @return ���� lastSolution
     */
    public Solution getLastSolution()
    {
        return lastSolution;
    }
    
    /**
     * ��ȡ�����ĵ�ͼ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
        // �ƶ�����վ��ԭ�������λ��
        cloneMap[statue_x][statue_y].setPlayer();
        return cloneMap;
    }
    
    /**
     * ��ȡ���߷�ǰ�ĵ�ͼ 
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
     * ���Ƿ����е�����Ŀ�����
     * 
     * @param nextMap
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
        
        // �ж��Ƿ����
        boolean allFinish = isMapFinish(sokoMap);
        
        if (allFinish)
        {
            this.result = Result.success;
            return;
        }
        
        this.result = Result.needsub;
    }
    
    /**
     * ��ȡ�ò������ĵ�ͼ�� �������ߣ���������������ѭ��������null�������սᡣ
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public Cell[][] stepToMap(Cell[][] sokoMap)
    {
        
        Cell statue = sokoMap[statue_x][statue_y];
        if (statue.move(step, sokoMap))
        {
            String mapStr = Util.descMap(sokoMap);
            if (!Util.putIfAb(mapStr, key))
            {
                Logger.debug("����" + key + "����ظ����������Ž�");
                return null;
            }
            if (Logger.isDebugEnable())
            {
                Logger.debug("�����");
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
        StringBuilder builder = new StringBuilder("��");
        builder.append(level);
        builder.append("����");
        builder.append("[");
        builder.append(statue_x);
        builder.append(",");
        builder.append(statue_y);
        builder.append("]");
        builder.append(step == null ? "" : step.getDesc());
        
        return builder.toString();
    }
    
}
