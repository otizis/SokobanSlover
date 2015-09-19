package com.jaxer.www;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Solution;

public class Util
{
    static boolean isdebug = false;
    
    static boolean isInfo = true;
    
    private static HashSet<String> mapSet = new HashSet<String>();
    
    public static Cell[][] cloneObject(Cell[][] a)
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
    
    public static void debug(String str)
    {
        if (isdebug)
        {
            System.out.println(str);
        }
    }
    
    public static void info(String str)
    {
        if (isInfo)
        {
            System.out.println(str);
        }
    }
    
    public static String descMap(Cell[][] map)
    {
        Cell[][] cloneObject = cloneObject(map);
        
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
    
    public static void drawMap(Cell[][] thisStepMap)
    {
        System.out.println("------------------");
        for (int j = 0; j < thisStepMap[0].length; j++)
        {
            for (int i = 0; i < thisStepMap.length; i++)
            {
                System.out.print(thisStepMap[i][j].draw());
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static boolean equle(Cell[][] a, Cell[][] b)
    {
        for (int i = 0; i < b.length; i++)
        {
            for (int j = 0; j < b[i].length; j++)
            {
                if (!a[i][j].equals(b[i][j]))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isDebugEnable()
    {
        return isdebug;
    }
    
    public static void main(String[] args)
    {
        //
    }
    
    /**
     * 是否成功放入set，表示不存在重复
     * 
     * @param mapStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean putIfAb(String mapStr)
    {
        
        if (mapSet.contains(mapStr))
        {
            return false;
        }
        mapSet.add(mapStr);
        return true;
        
    }
    
    static ArrayList<Solution> result = new ArrayList<Solution>();
    
    public static void addResult(Solution solution)
    {
        result.add(solution);
        
    }
}
