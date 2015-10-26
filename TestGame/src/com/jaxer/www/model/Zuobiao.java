package com.jaxer.www.model;

import com.jaxer.www.api.Moveable;
import com.jaxer.www.enums.AspectEnum;

public class Zuobiao implements Moveable
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
     * @param 对x进行赋值
     */
    public void setX(int x)
    {
        this.x = x;
    }
    
    /**
     * @param 对y进行赋值
     */
    public void setY(int y)
    {
        this.y = y;
    }
    
    /** {@inheritDoc} */
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append("]");
        return builder.toString();
    }
    
}
