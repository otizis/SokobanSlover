package com.jaxer.www.model;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.myexception.MyException;

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
            throw new MyException("不能设置玩家在非空位置。");
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
    
    public static final char wall = 'M';
    
    public static final char statue = 'Y';
    
    public static final char goal = 'G';
    
    public static final char player = 'P';
    
    public static final char empty = 'S';
    
    public Cell(int x, int y, char b)
    {
        this.x = x;
        this.y = y;
        switch (b)
        {
            case wall:
                this.item = ItemType.wall;
                break;
            case statue:
                this.item = ItemType.statue;
                break;
            case goal:
                this.isGoal = true;
                break;
            case player:
                this.item = ItemType.player;
                break;
        }
    }
    
    /**
     * 判断该点是否可以移入 空可以，墙或雕像不可以
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean canMoveIn()
    {
        switch (item)
        {
            case empty:
            case player:
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
            return (this.item == b.item && this.x == b.x && this.y == b.y);
        }
        return false;
    }
    
    /**
     * @return 返回 item
     */
    public ItemType getItem()
    {
        return item;
    }
    
    /**
     * @return 返回 x
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * @return 返回 y
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
     * @return 返回 isGole
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
     * 如果能移动，则移动返回true。 如果不能移动，则不移送返回false。
     * 
     * @param aspect
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
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
            this.moveTo(target);
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
            return "吴";
        }
        if (isGoal)
        {
            if (item == ItemType.empty)
            {
                return "ｏ";
            }
            if (item == ItemType.statue)
            {
                return "中";
            }
            
        }
        if (item == ItemType.empty)
        {
            return "　";
        }
        if (item == ItemType.wall)
        {
            return "墙";
        }
        if (item == ItemType.statue)
        {
            return "￥";
        }
        
        return "错";
    }
    
}
