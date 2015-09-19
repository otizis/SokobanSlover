package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Solution;

public class Test
{
    
    public static void main(String[] args)
    {
        StringBuilder line = new StringBuilder();
        line.append("wall,wall,wall,play,wall,wall,wall,wall").append(";");
        line.append("wall,wall,empt,empt,empt,empt,wall,wall").append(";");
        line.append("empt,empt,stat,stat,empt,stat,empt,empt").append(";");
        line.append("wall,goal,goal,goal,goal,goal,goal,wall").append(";");
        line.append("empt,empt,stat,empt,stat,stat,empt,empt").append(";");
        line.append("wall,wall,wall,empt,empt,wall,wall,wall");
        
        Cell[][] map = initMap(line.toString());
        
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
     * 运行算法
     * 
     * @param map
     * @see [类、类#方法、类#成员]
     */
    public static void run(Cell[][] map)
    {
        
        DeadPoitUtil.loadDeadSet(map);
        
        Long begin = System.currentTimeMillis();
        Solution lastOne = SolutionFactory.runByLevel(new Solution(map));
        Long end = System.currentTimeMillis();
        
        outputResult(lastOne);
        System.out.println("耗时：" + (end - begin));
    }
    
    /**
     * 输出结果
     * 
     * @param lastOne
     *            
     * @see [类、类#方法、类#成员]
     */
    private static void outputResult(Solution lastOne)
    {
        if (lastOne == null)
        {
            System.out.println("无解");
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
