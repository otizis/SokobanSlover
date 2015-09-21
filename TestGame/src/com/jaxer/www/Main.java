package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.Util.DeadPoitUtil;
import com.jaxer.www.Util.Logger;
import com.jaxer.www.Util.SolutionFactory;
import com.jaxer.www.model.Cell;
import com.jaxer.www.model.Solution;
import com.jaxer.www.model.myexception.MyException;

/**
 * 主体类
 * 
 * @author yejiangtao
 * @version [版本号, 2015年9月21日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Main
{
    
    public static void main(String[] args)
    {
        StringBuilder line = new StringBuilder();
        line.append("MMMMMSSMSSM;");
        line.append("MMMMMSSYYSM;");
        line.append("MMMMMSYMSSM;");
        line.append("GGGMMMSMSSM;");
        line.append("GSSMSSYSMSS;");
        line.append("GSSSSYSYSYS;");
        line.append("GSSMSSYSMSS;");
        line.append("GGGMMMSMSSM;");
        line.append("MMMMMPYSSSM;");
        line.append("MMMMMSSMSSM");
        
        Cell[][] map = initMap(line.toString());
        
        run(map);
        
        // Util.printMapSet();
    }
    
    private static Cell[][] initMap(String line)
    {
        if (!line.contains("P"))
        {
            throw new MyException("没有放置玩家位置。");
        }
        Cell[][] map = null;
        String[] lns = line.split(";");
        for (int i = 0; i < lns.length; i++)
        {
            for (int j = 0; j < lns[i].length(); j++)
            {
                if (null == map)
                {
                    map = new Cell[lns[i].length()][lns.length];
                }
                char type = lns[i].charAt(j);
                map[j][i] = new Cell(j, i, type);
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
        
        Long begin = System.currentTimeMillis();
        DeadPoitUtil.loadDeadSet(map);
        
        Solution solution = new Solution(map);
        Logger.info(solution.toString());
        Solution lastOne = SolutionFactory.runByLevel(solution);
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
