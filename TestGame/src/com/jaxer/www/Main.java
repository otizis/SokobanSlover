package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Solution;

/**
 * ������
 * 
 * @author yejiangtao
 * @version [�汾��, 2015��9��21��]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class Main
{
    
    public static void main(String[] args)
    {
        StringBuilder line = new StringBuilder();
        line.append("M, , , , , ,M,M").append(";");
        line.append("M,Y,M,M,M, , , ").append(";");
        line.append("P, , ,Y, , ,Y, ").append(";");
        line.append(" ,G,G,M, ,Y, ,M").append(";");
        line.append("M,G,G,M, , , ,M");
        
        Cell[][] map = initMap(line.toString());
        
        run(map);
        
        // Util.printMapSet();
    }
    
    private static Cell[][] initMap(String line)
    {
        Cell[][] map = null;
        
        String[] lns = line.split(";");
        for (int i = 0; i < lns.length; i++)
        {
            String[] desc = lns[i].split(",");
            for (int j = 0; j < desc.length; j++)
            {
                if (null == map)
                {
                    map = new Cell[desc.length][lns.length];
                }
                map[j][i] = new Cell(j, i, desc[j]);
            }
        }
        return map;
    }
    
    /**
     * �����㷨
     * 
     * @param map
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void run(Cell[][] map)
    {
        
        Long begin = System.currentTimeMillis();
        DeadPoitUtil.loadDeadSet(map);
        
        Solution solution = new Solution(map);
        Logger.info(solution.toString());
        Solution lastOne = SolutionFactory.runByLevel(solution);
        Long end = System.currentTimeMillis();
        
        outputResult(lastOne);
        System.out.println("��ʱ��" + (end - begin));
        
    }
    
    /**
     * ������
     * 
     * @param lastOne
     *            
     * @see [�ࡢ��#��������#��Ա]
     */
    private static void outputResult(Solution lastOne)
    {
        if (lastOne == null)
        {
            System.out.println("�޽�");
        }
        else
        {
            ArrayList<String> gonglv = new ArrayList<String>();
            
            Solution solut = lastOne;
            do
            {
                gonglv.add(0, solut.toString());
                solut = solut.getLastSolution();
            } while (solut != null);
            
            for (String string : gonglv)
            {
                System.out.println(string);
            }
            
        }
        
    }
    
}
