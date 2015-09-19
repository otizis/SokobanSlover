package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Result;
import com.jaxer.www.model.Solution;

public class Test
{
    
    /**
     * 批量获取走法的下一步走法。
     * 
     * @param needSub
     * @return
     * @see [类、类#方法、类#成员]
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
     * 对每一种走法进行行走，返回走成功的走法
     * 
     * @param solutions
     * @return
     * @see [类、类#方法、类#成员]
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
     * 放置玩家初始位置
     * 
     * @param map
     *            
     * @see [类、类#方法、类#成员]
     */
    private static void initPlayer(Cell[][] map)
    {
        // 放置玩家位置
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
        int level = 0;
        while (!nextSolution.isEmpty())
        {
            
            Util.info("开始走第" + level + "步，有走法：" + nextSolution.size());
            
            // 实施走法
            ArrayList<Solution> needSub = getSoultionNeedSub(nextSolution);
            
            // 有成功, 中断向下一级循环
            if (!Util.result.isEmpty())
            {
                break;
            }
            
            // 下一步的走法列表
            nextSolution = getNextSolutionsBatch(needSub);
            
            level++;
        }
    }
}
