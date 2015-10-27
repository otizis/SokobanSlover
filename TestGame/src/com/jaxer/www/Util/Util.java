package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class Util
{
    private final static boolean useSet = true;
    
    /**
     * ��ͼ����ֵ��������ֹ��ͻ��¼�� ����ظ�����ʾ���ֹ������߳���������߷�������Ϊ�����ŵ��߷�
     */
    private static HashMap<String, String> mapMap =
        useSet ? null : new HashMap<String, String>(1024);
        
    private static HashSet<String> mapSet =
        !useSet ? null : new HashSet<String>(1024);
        
    public static ArrayList<Zuobiao> cloneBoxList(ArrayList<Zuobiao> boxList)
    {
        long begine = System.currentTimeMillis();
        ArrayList<Zuobiao> cloneBoxList =
            new ArrayList<Zuobiao>(boxList.size());
            
        for (Zuobiao box : boxList)
        {
            cloneBoxList.add(box.myClone());
        }
        TimeStamps.addTime("cloneBoxList", System.currentTimeMillis() - begine);
        return cloneBoxList;
    }
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static String descZuobiaoList(Collection<Zuobiao> coll)
    {
        Zuobiao[] array = coll.toArray(new Zuobiao[0]);
        Arrays.sort(array, new Comparator<Zuobiao>()
        {
            
            @Override
            public int compare(Zuobiao o1, Zuobiao o2)
            {
                if (o1.getX() == o2.getX())
                {
                    return o2.getY() - o1.getY();
                }
                return o2.getX() - o1.getX();
            }
            
        });
        
        StringBuilder buid = new StringBuilder();
        for (int j = 0; j < array.length; j++)
        {
            buid.append(array[j].getX());
            buid.append(array[j].getY());
            buid.append(",");
        }
        return buid.toString();
    }
    
    public static String drawMap(StringBuilder mapStr)
    {
        StringBuilder result = new StringBuilder();
        int width = SokoMap.max_x + 1;
        for (int i = 0; i < mapStr.length(); i++)
        {
            if (i % width == 0)
            {
                result.append("\n");
            }
            result.append(mapStr.charAt(i));
            result.append(" ");
        }
        result.append("\n");
        return result.toString();
    }
    
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static StringBuilder mapStr()
    {
        Cell[][] map = SokoMap.thisStepMap;
        
        StringBuilder buid = new StringBuilder();
        for (int j = 0; j < map[0].length; j++)
        {
            for (int i = 0; i < map.length; i++)
            {
                buid.append(map[i][j].draw());
            }
        }
        return buid;
    }
    
    public static void printMapSet()
    {
        if (useSet)
        {
            return;
        }
        Set<String> mapStr = mapMap.keySet();
        for (String string : mapStr)
        {
            System.out.println(string + " : " + mapMap.get(string));
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
        if (useSet)
        {
            if (mapSet.contains(mapStr))
            {
                return false;
            }
            mapSet.add(mapStr);
            return true;
        }
        else
        {
            
            if (mapMap.containsKey(mapStr))
            {
                mapMap.put(mapStr, keys + "," + mapMap.get(mapStr));
                return false;
            }
            mapMap.put(mapStr, keys);
            return true;
        }
    }
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static StringBuilder replaceZuobiao(StringBuilder a, Zuobiao zb,
        String str)
    {
        int indxe = zb.getY() * (SokoMap.max_x + 1) + zb.getX();
        
        a.replace(indxe, indxe + 1, str);
        return a;
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
        if (useSet)
        {
            mapSet.clear();
        }
        else
        {
            
            mapMap.clear();
        }
        
    }
}
