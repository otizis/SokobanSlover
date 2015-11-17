package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.jaxer.www.myexception.MyException;

/**
 * 针对坐标是否存在，设计的一维数组set集合，更快的判断坐标是否存在在一个集合中。
 * 
 * @version [版本号, 2015年11月12日]
 */
public class FastSet
{
    public FastSet(int x, int y)
    {
        mapBytes = new boolean[(x + 1) * (y + 1)];
        len = mapBytes.length;
        map_x = x;
    }
    
    /**
     * true表示存在于集合中
     */
    boolean[] mapBytes;
    
    public boolean[] getMapBytes()
    {
        return mapBytes;
    }
    
    int len;
    
    int map_x;
    
    /**
     * 转化二维坐标为一维数组的序号
     * 
     * @param b
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int getLen(Zuobiao b)
    {
        int index = b.getX() + (b.getY() * (map_x + 1));
        if (index >= len)
        {
            throw new MyException(b + ",不存在");
        }
        
        return index;
    }
    
    /**
     * 转化一维数组序号为二维的坐标
     * 
     * @param b
     * @return
     * @see [类、类#方法、类#成员]
     */
    private Zuobiao getZuobiao(int index)
    {
        int y = index / (map_x + 1);
        
        int x = index - y * (map_x + 1);
        
        return new Zuobiao(x, y);
    }
    
    public void clear()
    {
        int i = 0;
        while (i < len)
        {
            mapBytes[i++] = false;
        }
    }
    
    public void removeAll(Collection<Zuobiao> paramCollection)
    {
        Iterator<Zuobiao> iterator = paramCollection.iterator();
        while (iterator.hasNext())
        {
            Zuobiao next = iterator.next();
            int index = getLen(next);
            mapBytes[index] = false;
        }
    }
    
    public void addAll(Collection<Zuobiao> paramCollection)
    {
        Iterator<Zuobiao> iterator = paramCollection.iterator();
        while (iterator.hasNext())
        {
            Zuobiao next = iterator.next();
            int index = getLen(next);
            mapBytes[index] = true;
        }
    }
    
    public void remove(Zuobiao zb)
    {
        int index = getLen(zb);
        mapBytes[index] = false;
    }
    
    public boolean contains(Zuobiao zb)
    {
        if (null == zb)
        {
            return false;
        }
        int index = getLen(zb);
        return mapBytes[index];
    }
    
    public ArrayList<Zuobiao> getList()
    {
        ArrayList<Zuobiao> boxList = new ArrayList<Zuobiao>();
        for (int i = 0; i < len; i++)
        {
            if (mapBytes[i])
            {
                boxList.add(getZuobiao(i));
            }
            
        }
        return boxList;
    }
    
}
