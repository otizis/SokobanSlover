package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Result;
import com.jaxer.www.model.Solution;

public class Test
{
    
    /**
     * ������ȡ�߷�����һ���߷���
     * 
     * @param needSub
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private static ArrayList<Solution> getNextSolutionsBatch(
        ArrayList<Solution> needSub)
    {
        ArrayList<Solution> nextSolutionList = new ArrayList<Solution>();
        for (Solution solu : needSub)
        {
            ArrayList<Solution> temp = SolutionFactory.getNextSolution(solu);
            if (null != temp)
            {
                nextSolutionList.addAll(temp);
                
            }
        }
        return nextSolutionList;
    }
    
    /**
     * ��ÿһ���߷��������ߣ������߳ɹ����߷�
     * 
     * @param solutions
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    private static ArrayList<Solution> getSoultionNeedSub(
        ArrayList<Solution> solutions)
    {
        ArrayList<Solution> needSub = new ArrayList<Solution>();
        
        for (Solution solu : solutions)
        {
            solu.play();
            if (solu.getResult() == Result.success)
            {
                Util.result.add(solu);
                break;
            }
            if (solu.getResult() == Result.needsub)
            {
                needSub.add(solu);
                
            }
            
        }
        return needSub;
    }
    
    private static Cell[][] initMap()
    {
        Cell[][] map = null;
        
        StringBuilder line = new StringBuilder();
        line.append("wall,wall,wall,empt,wall,wall,wall,wall").append(";");
        line.append("wall,wall,empt,empt,empt,empt,wall,wall").append(";");
        line.append("empt,empt,stat,stat,empt,stat,empt,empt").append(";");
        line.append("wall,goal,goal,goal,goal,goal,goal,wall").append(";");
        line.append("empt,empt,stat,empt,stat,stat,empt,empt").append(";");
        line.append("wall,wall,wall,empt,empt,wall,wall,wall");
        
        // line.append("wall,wall,wall,empt,wall,wall,wall,wall").append(";");
        // line.append("wall,wall,empt,empt,empt,empt,wall,wall").append(";");
        // line.append("empt,empt,stat,empt,empt,empt,empt,empt").append(";");
        // line.append("wall,empt,empt,goal,empt,empt,empt,wall").append(";");
        // line.append("empt,empt,empt,empt,empt,empt,empt,empt").append(";");
        // line.append("wall,wall,wall,empt,empt,wall,wall,wall");
        
        String[] lns = line.toString().split(";");
        for (int i = 0; i < lns.length; i++)
        {
            String[] cell = lns[i].split(",");
            for (int j = 0; j < cell.length; j++)
            {
                if (null == map)
                {
                    map = new Cell[cell.length][lns.length];
                }
                map[j][i] = new Cell(j, i, cell[j]);
            }
        }
        return map;
    }
    
    /**
     * ������ҳ�ʼλ��
     * 
     * @param map
     *            
     * @see [�ࡢ��#��������#��Ա]
     */
    private static void initPlayer(Cell[][] map)
    {
        // �������λ��
        map[3][0].setPlayer();
        
    }
    
    public static void main(String[] args)
        throws Exception
    {
        Cell[][] map = initMap();
        
        initPlayer(map);
        
        Util.drawMap(map);
        
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
        int level = 0;
        while (!nextSolution.isEmpty())
        {
            
            Util.info("��ʼ�ߵ�" + level + "�������߷���" + nextSolution.size());
            
            // ʵʩ�߷�
            ArrayList<Solution> needSub = getSoultionNeedSub(nextSolution);
            
            // �гɹ�, �ж�����һ��ѭ��
            if (!Util.result.isEmpty())
            {
                break;
            }
            
            // ��һ�����߷��б�
            nextSolution = getNextSolutionsBatch(needSub);
            
            level++;
        }
    }
}
