package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Solution;

public class Test
{
    
    public static void main(String[] args)
    {
        StringBuilder line = new StringBuilder();
        line.append("wall,wall,wall,empt,wall,wall,wall,wall").append(";");
        line.append("wall,wall,empt,empt,empt,empt,wall,wall").append(";");
        line.append("empt,empt,stat,stat,empt,stat,empt,empt").append(";");
        line.append("wall,goal,goal,goal,goal,goal,goal,wall").append(";");
        line.append("empt,empt,stat,empt,stat,stat,empt,empt").append(";");
        line.append("wall,wall,wall,empt,empt,wall,wall,wall");
        
        Cell[][] map = initMap(line.toString());
        
        // �������λ��
        map[5][3].setPlayer();
        
        run(map);
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
     * @param map
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void run(Cell[][] map)
    {
        
        Util.drawMap(map);
        
        DeadPoitUtil.loadDeadSet(map);
        
        Long begin = System.currentTimeMillis();
        runByLevel(new Solution(map));
        Long end = System.currentTimeMillis();
        
        outputResult(end - begin);
    }
    
    /**
     * ������
     * 
     * @param time
     *            
     * @see [�ࡢ��#��������#��Ա]
     */
    private static void outputResult(long time)
    {
        if (Util.result.isEmpty())
        {
            System.out.println("�޽�");
        }
        else
        {
            ArrayList<String> gonglv = new ArrayList<String>();
            
            Solution solut = Util.result.get(0);
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
        System.out.println("��ʱ��" + time);
    }
    
    /**
     * һ��һ����������߷��������߷�
     * 
     * @see [�ࡢ��#��������#��Ա]
     */
    private static void runByLevel(Solution solution)
    {
        // ��ȡ�������ߵ��߷��б�
        ArrayList<Solution> nextSolution =
            SolutionFactory.getNextSolution(solution);
            
        // ÿ��һ������ȡ��һ�����߷��б�����ѭ��
        int level = 1;
        while (!nextSolution.isEmpty())
        {
            
            Util.info("��ʼ�ߵ�" + level + "���֧�����߷���" + nextSolution.size());
            
            // ʵʩ�߷�
            ArrayList<Solution> needSub = SolutionFactory.getSoultionNeedSub(nextSolution);
            
            // �гɹ�, �ж�����һ��ѭ��
            if (!Util.result.isEmpty())
            {
                break;
            }
            
            // ��һ�����߷��б�
            nextSolution = SolutionFactory.getNextSolutionsBatch(needSub);
            
            level++;
        }
    }
}
