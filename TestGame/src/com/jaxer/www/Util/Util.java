package com.jaxer.www.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.jaxer.www.enums.ItemType;
import com.jaxer.www.model.Cell;

public class Util
{
    
    /**
     * ��ͼ�������Ͳ����map
     */
    private static HashMap<String, Integer> keySet =
        new HashMap<String, Integer>();
        
    /**
     * ��ͼ����ֵ��������ֹ��ͻ��¼�� ����ظ�����ʾ���ֹ������߳���������߷�������Ϊ�����ŵ��߷�
     */
    private static HashMap<String, String> mapSet =
        new HashMap<String, String>();
        
    /**
     * ��¡��ͼ
     * 
     * @param a
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static Cell[][] cloneMap(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
            }
        }
        return b;
    }
    
    /**
     * ��¡��ͼ�����Ƴ���ұ��
     * 
     * @param a
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static Cell[][] cloneMapClearPlayer(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
                if (b[i][j].getItem() == ItemType.player)
                {
                    b[i][j].setItem(ItemType.empty);
                }
            }
        }
        return b;
    }
    
    /**
     * ��¡��ͼ�����Ƴ���ұ�ǣ�������
     * 
     * @param a
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static Cell[][] cloneMapClearPlayerAndStatue(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
                if (b[i][j].getItem() == ItemType.player
                    || b[i][j].getItem() == ItemType.statue)
                {
                    b[i][j].setItem(ItemType.empty);
                }
            }
        }
        return b;
    }
    
    /**
     * ��¡��ͼ�����Ƴ���ұ��
     * 
     * @param a
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static Cell[][] cloneMapClearStatue(Cell[][] a)
    {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                b[i][j] = a[i][j].myClone();
                if (b[i][j].getItem() == ItemType.statue)
                {
                    b[i][j].setItem(ItemType.empty);
                }
            }
        }
        return b;
    }
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static String descMap(Cell[][] map)
    {
        Cell[][] cloneObject = cloneMap(map);
        
        HashSet<Cell> allPlayerCanGoCells =
            SolutionFactory.getAllPlayerCanGoCells(cloneObject);
            
        Iterator<Cell> iterator = allPlayerCanGoCells.iterator();
        
        while (iterator.hasNext())
        {
            Cell cell = (Cell)iterator.next();
            cell.setPlayer();
        }
        
        StringBuilder buid = new StringBuilder();
        for (int j = 0; j < cloneObject[0].length; j++)
        {
            for (int i = 0; i < cloneObject.length; i++)
            {
                buid.append(cloneObject[i][j].draw());
            }
        }
        return buid.toString();
    }
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static String mapToString(Cell[][] map)
    {
        
        StringBuilder buid = new StringBuilder();
        for (int j = 0; j < map[0].length; j++)
        {
            for (int i = 0; i < map.length; i++)
            {
                buid.append(map[i][j].draw());
            }
            buid.append("\n");
        }
        return buid.toString();
    }
    
    /**
     * ͬ����ȡkey����ǰ��˳��
     * 
     * @param key
     * @see [�ࡢ��#��������#��Ա]
     */
    public static String getSolutionKey(String key)
    {
        if (keySet.containsKey(key))
        {
            int num = keySet.get(key);
            keySet.put(key, ++num);
            return key + ":" + num;
        }
        keySet.put(key, 0);
        return key + ":0";
    }
    
    public static void printMapSet()
    {
        Set<String> mapStr = mapSet.keySet();
        for (String string : mapStr)
        {
            System.out.println(string + " : " + mapSet.get(string));
        }
    }
    
    /**
     * �Ƿ�ɹ�����set����ʾ�������ظ�
     * 
     * @param mapStr
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean putIfAb(String mapStr, String keys)
    {
        if (mapSet.containsKey(mapStr))
        {
            mapSet.put(mapStr, keys + "," + mapSet.get(mapStr));
            return false;
        }
        mapSet.put(mapStr, keys);
        return true;
    }
    
    /**
     * ��ռ�¼
     * 
     * @param mapStr
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void resetMapSet()
    {
        mapSet.clear();
        
    }
}
