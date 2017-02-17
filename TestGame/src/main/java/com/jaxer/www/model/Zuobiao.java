package com.jaxer.www.model;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.interfaces.Moveable;

public class Zuobiao implements Moveable, Comparable<Zuobiao>
{
    int x;
    
    int y;
    
    public Zuobiao()
    {
    }
    
    public Zuobiao(int x, int y)
    {
        this.x = x;
        this.y = y;
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
        return x * y;
    }
    
    /** {@inheritDoc} */
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Zuobiao)
        {
            Zuobiao obj2 = (Zuobiao)obj;
            return this.x == obj2.x && this.y == obj2.y;
        }
        return false;
    }
    
    @Override
    public void moveByAspect(AspectEnum aspect)
    {
        switch (aspect)
        {
            case up:
                moveUp();
                break;
                
            case down:
                moveDown();
                break;
                
            case left:
                moveLeft();
                break;
                
            default:
                moveRight();
        }
        
    }
    
    /**
     * 回退，回复moveByAspect
     * 
     * @param aspect
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void backByAspect(AspectEnum aspect)
    {
        switch (aspect)
        {
            case up:
                moveDown();
                break;
                
            case down:
                moveUp();
                break;
                
            case left:
                moveRight();
                break;
                
            default:
                moveLeft();
        }
        
    }
    
    @Override
    public void moveDown()
    {
        y++;
    }
    
    @Override
    public void moveLeft()
    {
        x--;
    }
    
    @Override
    public void moveRight()
    {
        x++;
        
    }
    
    @Override
    public void moveUp()
    {
        y--;
        
    }
    
    public Zuobiao myClone()
    {
        
        return new Zuobiao(x, y);
        
    }
    
    /**
     *  对x进行赋值
     */
    public void setX(int x)
    {
        this.x = x;
    }
    
    /**
     *  对y进行赋值
     */
    public void setY(int y)
    {
        this.y = y;
    }
    
    /**
     * @return
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(x);
        builder.append(",");
        builder.append(y);
        builder.append(")");
        return builder.toString();
    }
    
    @Override
    public int compareTo(Zuobiao other)
    {
        if (this.x == other.x)
        {
            return this.y - other.y;
        }
        return this.x - other.x;
    }
    
    public Zuobiao getDown()
    {
        return new Zuobiao(x, y + 1);
    }
    
    public Zuobiao getLeft()
    {
        return new Zuobiao(x - 1, y);
    }
    
    public Zuobiao getRight()
    {
        return new Zuobiao(x + 1, y);
    }
    
    public Zuobiao getUp()
    {
        return new Zuobiao(x, y - 1);
    }
}
