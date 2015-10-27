package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.Util.TimeStamps;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Solution;

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
        // line.append("MMMMMSSMSSM;");
        // line.append("MMMMMSSYYSM;");
        // line.append("MMMMMSYMSSM;");
        // line.append("GGGMMMSMSSM;");
        // line.append("GSSMSSYSMSS;");
        // line.append("GSSSSYSYSYS;");
        // line.append("GSSMSSYSMSS;");
        // line.append("GGGMMMSMSSM;");
        // line.append("MMMMMPYSSSM;");
        // line.append("MMMMMSSMSSM");
        
        line.append("MMMPMMMM;");
        line.append("MMSSSSMM;");
        line.append("SSYYSYSS;");
        line.append("MGGGGGGM;");
        line.append("SSYSYYSS;");
        line.append("MMMSSMMM");
        
        SokoMap sokoMap = new SokoMap(line.toString());
        
        Solution lastOne = sokoMap.run();
        
        outputResult(lastOne);
        
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
                gonglv.add(0, solut.drawAfter());
                solut = solut.getLastSolution();
            } while (solut != null);
            
            for (int i = 0; i < gonglv.size(); i++)
            {
                System.out.println("第" + i + "步");
                System.out.println(gonglv.get(i));
            }
        }
        TimeStamps.printMap();
        
    }
    
}
