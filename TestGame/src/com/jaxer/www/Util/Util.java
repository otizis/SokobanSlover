package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class Util
{
    
    /**
     * 地图特征，和步骤的map
     */
    private static HashMap<String, Integer> keySet =
        new HashMap<String, Integer>();
        
    /**
     * 地图特征值，如果出现过就会记录。 如果重复，表示出现过其他走出这个特征走法，本次为非最优的走法
     */
    private static HashMap<String, String> mapSet =
        new HashMap<String, String>();
        
    public static ArrayList<Zuobiao> cloneBoxList(ArrayList<Zuobiao> boxList)
    {
        ArrayList<Zuobiao> cloneBoxList =
            new ArrayList<Zuobiao>(boxList.size());
            
        for (Zuobiao box : boxList)
        {
            cloneBoxList.add(box.myClone());
        }
        return cloneBoxList;
    }
    
    /**
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
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
    
    public static void drawMap(StringBuilder mapStr)
    {
        int width = SokoMap.max_x + 1;
        for (int i = 0; i < mapStr.length(); i++)
        {
            if (i % width == 0)
            {
                System.out.println();
            }
            System.out.print(mapStr.charAt(i));
            System.out.print(" ");
        }
        System.out.println();
    }
    
    /**
     * 同步获取key，按前后顺序
     * 
     * @param key
     * @see [类、类#方法、类#成员]
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
    
    /**
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
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
        Set<String> mapStr = mapSet.keySet();
        for (String string : mapStr)
        {
            System.out.println(string + " : " + mapSet.get(string));
        }
    }
    
    /**
     * 是否成功放入set，表示不存在重复
     * 
     * @param mapStr
     * @return
     * @see [类、类#方法、类#成员]
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
     * 生成地图的特征字符串，即全盘描述
     * 
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static StringBuilder replaceZuobiao(StringBuilder a, Zuobiao zb,
        String str)
    {
        int indxe = zb.getY() * (SokoMap.max_x + 1) + zb.getX();
        
        a.replace(indxe, indxe + 1, str);
        return a;
    }
    
    /**
     * 清空记录
     * 
     * @param mapStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static void resetMapSet()
    {
        mapSet.clear();
        
    }
}
