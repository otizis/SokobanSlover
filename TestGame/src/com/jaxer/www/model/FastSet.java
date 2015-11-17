package com.jaxer.www.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    
    /**
     * true��ʾ�����ڼ�����
     */
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
    
    /**
     * ת��һά�������Ϊ��ά������
     * 
     * @param b
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
