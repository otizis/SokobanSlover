package com.jaxer.www.model;

import java.util.Collection;

import com.jaxer.www.myexception.MyException;

/**
 * ��������Ƿ���ڣ���Ƶ�һά����set���ϣ�������ж������Ƿ������һ�������С�
 * 
 * @version [�汾��, 2015��11��12��]
 */
public class FastSet
{
    public FastSet(int x, int y)
    {
        mapBytes = new boolean[(x + 1) * (y + 1)];
        len = mapBytes.length;
        map_x = x;
    }
    
    boolean[] mapBytes;
    
    public boolean[] getMapBytes()
    {
        return mapBytes;
    }
    
    int len;
    
    int map_x;
    
    /**
     * ת����ά����Ϊһά��������
     * 
     * @param b
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private int getLen(Zuobiao b)
    {
        int index = b.getX() + (b.getY() * (map_x + 1));
        if (index >= len)
        {
            throw new MyException(b + ",������");
        }
        
        return index;
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
        for (Zuobiao zuobiao : paramCollection)
        {
            int index = getLen(zuobiao);
            mapBytes[index] = false;
        }
    }
    
    public void addAll(Collection<Zuobiao> paramCollection)
    {
        for (Zuobiao zuobiao : paramCollection)
        {
            int index = getLen(zuobiao);
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
}
