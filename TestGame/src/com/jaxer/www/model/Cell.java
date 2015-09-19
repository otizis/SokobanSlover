package com.jaxer.www.model;

import com.jaxer.www.MyException;
import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.ItemType;

public class Cell
{
    private boolean isGoal = false;
    
    private ItemType item = ItemType.empty;
    
    private int x;
    
    private int y;
    
    public Cell()
    {
        super();
    }
    
    public void setPlayer()
    {
        if (this.item == ItemType.player)
        {
            return;
        }
        if (this.item != ItemType.empty)
        {
            throw new MyException("������������ڷǿ�λ�á�");
        }
        this.item = ItemType.player;
    }
    
    public void setItem(ItemType itemType)
    {
        this.item = itemType;
    }
    
    public Cell(int x, int y, boolean isGole, ItemType item)
    {
        super();
        this.x = x;
        this.y = y;
        this.isGoal = isGole;
        this.item = item;
    }
    
    public Cell(int x, int y, String string)
    {
        this.x = x;
        this.y = y;
        if ("wall".equals(string))
        {
            this.item = ItemType.wall;
        }
        else if ("stat".equals(string))
        {
            this.item = ItemType.statue;
        }
        else if ("goal".equals(string))
        {
            this.isGoal = true;
        }
    }
    
    /**
     * �жϸõ��Ƿ�������� �տ��ԣ�ǽ����񲻿���
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private boolean canMoveIn()
    {
        switch (item)
        {
            case empty:
                return true;
            case wall:
            case statue:
                return false;
            default:
                break;
        }
        return true;
    }
    
    /** {@inheritDoc} */
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Cell)
        {
            Cell b = (Cell)obj;
            return (this.item == b.getItem() && this.x == b.getX()
                && this.y == b.getY());
        }
        return false;
    }
    
    /**
     * @return ���� item
     */
    public ItemType getItem()
    {
        return item;
    }
    
    /**
     * @return ���� x
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * @return ���� y
     */
    public int getY()
    {
        return y;
    }
    
    /** {@inheritDoc} */
    
    @Override
    public int hashCode()
    {
        
        return 100;
    }
    
    /**
     * �Ƿ����
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean isFinish()
    {
        if (isGoal)
        {
            return item == ItemType.statue;
        }
        return true;
    }
    
    /**
     * @return ���� isGole
     */
    public boolean isGole()
    {
        return isGoal;
    }
    
    public boolean isStatue()
    {
        return item == ItemType.statue;
    }
    
    /**
     * ������ƶ������ƶ�����true�� ��������ƶ��������ͷ���false��
     * 
     * @param aspect
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean move(AspectEnum aspect, Cell[][] map)
    {
        if (item != ItemType.statue)
        {
            return false;
        }
        Cell target = null;
        switch (aspect)
        {
            case up:
                if (y == 0)
                {
                    return false;
                }
                target = map[x][y - 1];
                break;
                
            case down:
                if ((y + 1) == map[0].length)
                {
                    return false;
                }
                target = map[x][y + 1];
                break;
                
            case left:
                if (x == 0)
                {
                    return false;
                }
                target = map[x - 1][y];
                break;
                
            case right:
                if ((x + 1) == map.length)
                {
                    return false;
                }
                target = map[x + 1][y];
                break;
        }
        if (target.canMoveIn())
        {
            moveTo(target);
            return true;
        }
        return false;
    }
    
    private void moveTo(Cell target)
    {
        this.item = ItemType.empty;
        target.item = ItemType.statue;
    }
    
    public Cell myClone()
    {
        Cell b = new Cell(x, y, isGoal, item);
        
        return b;
        
    }
    
    /** {@inheritDoc} */
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Cell [x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append("]");
        return builder.toString();
    }
    
    public String draw()
    {
        if (item == ItemType.player)
        {
            return "��";
        }
        if (isGoal)
        {
            if (item == ItemType.empty)
            {
                return "��";
            }
            if (item == ItemType.statue)
            {
                return "��";
            }
            
        }
        if (item == ItemType.empty)
        {
            return "��";
        }
        if (item == ItemType.wall)
        {
            return "ǽ";
        }
        if (item == ItemType.statue)
        {
            return "��";
        }
        
        return "��";
    }
    
}
