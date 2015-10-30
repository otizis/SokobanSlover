package com.jaxer.www.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.jaxer.www.Filter.BloomFliter;
import com.jaxer.www.api.MapFliter;
import com.jaxer.www.enums.CellType;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;
import com.jaxer.www.myexception.MyException;

public class Util
{
    
    private static MapFliter<byte[]> fliter = new BloomFliter();
    // private static MapFliter fliter = new HashSetFilter();
    
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
    
    public static HashSet<Zuobiao> coverter(ArrayList<Zuobiao> boxList)
    {
        HashSet<Zuobiao> boxsSet = new HashSet<Zuobiao>();
        for (Zuobiao box : boxList)
        {
            boxsSet.add(box.myClone());
        }
        return boxsSet;
    }
    
    /**
     * ���ɵ�ͼ�������ַ�������ȫ������
     * 
     * @param map
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static byte[] coverBox(ArrayList<Zuobiao> coll)
    {
        byte[] desc = new byte[(SokoMap.max_x + 1) * (SokoMap.max_y + 1)];
        for (Zuobiao b : coll)
        {
            desc[b.getX() + (b.getY() * SokoMap.max_x)] = 1;
        }
        return desc;
    }
    
    /**
     * ���������׵����ĵ�ͼ�������С� <������ϸ����>
     * 
     * @param mans
     * @param mapByte
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static byte[] coverMan(ArrayList<Zuobiao> mans, byte[] mapByte)
    {
        for (Zuobiao b : mans)
        {
            int index = b.getX() + (b.getY() * SokoMap.max_x);
            if (mapByte[index] == 1)
            {
                throw new MyException("�������ظ���");
            }
            mapByte[index] = 2;
        }
        return mapByte;
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
    
    /**
     * �Ƿ�ɹ�����set����ʾ�������ظ�
     * 
     * @param mapStr
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isExist(byte[] all, String keys)
    {
        return fliter.isExist(all);
        
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
        fliter.clear();
        
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
