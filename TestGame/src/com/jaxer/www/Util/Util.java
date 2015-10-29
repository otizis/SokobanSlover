package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.jaxer.www.enums.CellType;
import com.jaxer.www.manager.TimeStamps;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class Util
{
    private final static boolean useSet = true;
    
    /**
     * ��ͼ����ֵ��������ֹ��ͻ��¼�� ����ظ�����ʾ���ֹ������߳���������߷�������Ϊ�����ŵ��߷�
     */
    private static HashMap<String, ArrayList<String>> mapMap =
        useSet ? null : new HashMap<String, ArrayList<String>>(1024);
        
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
    public static StringBuilder descZuobiaoList(Collection<Zuobiao> coll)
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
        return buid;
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
    public static boolean putIfAb(StringBuilder boxsStr, StringBuilder manStr,
        String keys)
    {
        if (useSet)
        {
            String sy = boxsStr.append(manStr).toString();
            if (mapSet.contains(sy))
            {
                return false;
            }
            mapSet.add(sy);
            return true;
        }
        else
        {
            
            // if (mapMap.containsKey(boxsStr))
            // {
            // ArrayList<String> arrayList = mapMap.get(boxsStr);
            // if (arrayList.contains(manStr))
            // {
            // return false;
            // }
            // else
            // {
            // arrayList.add(manStr);
            // return true;
            // }
            // }
            // ArrayList<String> arrayList = new ArrayList<String>();
            // arrayList.add(manStr);
            // mapMap.put(boxsStr, arrayList);
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
    
    /**
     * ���е����Ӷ�վ��Ŀ�����
     * 
     * @param boxList
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isAllBoxGoal(ArrayList<Zuobiao> boxList)
    {
        for (Zuobiao zuobiao : boxList)
        {
            if (!SokoMap.getCell(zuobiao).check(CellType.gole))
            {
                
                return false;
            }
        }
        return true;
    }
    
    /**
     * ���е�Ŀ��㶼�����Ӹ���
     * 
     * @param boxList
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isAllGoalCover(ArrayList<Zuobiao> boxList)
    {
        for (Zuobiao zb : SokoMap.goleList)
        {
            if (!boxList.contains(zb))
            {
                return false;
            }
        }
        return true;
    }
}
