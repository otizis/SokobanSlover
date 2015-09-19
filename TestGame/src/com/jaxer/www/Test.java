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
        
        // 放置玩家位置
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
     * 运行算法
     * @param map
     * @see [类、类#方法、类#成员]
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
     * 输出结果
     * 
     * @param time
     *            
     * @see [类、类#方法、类#成员]
     */
    private static void outputResult(long time)
    {
        if (Util.result.isEmpty())
        {
            System.out.println("无解");
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
        System.out.println("耗时：" + time);
    }
    
    /**
     * 一层一层历遍根据走法衍生的走法
     * 
     * @see [类、类#方法、类#成员]
     */
    private static void runByLevel(Solution solution)
    {
        // 获取现在能走的走法列表
        ArrayList<Solution> nextSolution =
            SolutionFactory.getNextSolution(solution);
            
        // 每走一步，获取下一步的走法列表，不断循环
        int level = 1;
        while (!nextSolution.isEmpty())
        {
            
            Util.info("开始走第" + level + "层分支，有走法：" + nextSolution.size());
            
            // 实施走法
            ArrayList<Solution> needSub = SolutionFactory.getSoultionNeedSub(nextSolution);
            
            // 有成功, 中断向下一级循环
            if (!Util.result.isEmpty())
            {
                break;
            }
            
            // 下一步的走法列表
            nextSolution = SolutionFactory.getNextSolutionsBatch(needSub);
            
            level++;
        }
    }
}
