package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.Util.MapStrUtil;
import com.jaxer.www.manager.MapLib;
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
        // line.append("MMMMMSSBBSM;");
        // line.append("MMMMMSBMSSM;");
        // line.append("GGGMMMSMSSM;");
        // line.append("GSSMSSBSMSS;");
        // line.append("GSSSSBSBSBS;");
        // line.append("GSSMSSBSMSS;");
        // line.append("GGGMMMSMSSM;");
        // line.append("MMMMMPBSSSM;");
        // line.append("MMMMMSSMSSM");
        
        line.append("MMMMMSSSMMMMMMMMMM;");
        line.append("MMMMMBSSMMMMMMMMMM;");
        line.append("MMMMMSSBMMMMMMMMMM;");
        line.append("MMMSSBSBSMMMMMMMMM;");
        line.append("MMMSMSMMSMMMMMMMMM;");
        line.append("MSSSMSMMSMMMMMSSGG;");
        line.append("MSBSSBSSSSSSSSSSGG;");
        line.append("MMMMMSMMMSMPMMSSGG;");
        line.append("MMMMMSSSSSMMMMMMMM");
        SokoMap sokoMap = new SokoMap(line.toString());
        
        // String convert = MapStrUtil.convert(MapLib.test2);
        // SokoMap sokoMap = new SokoMap(convert);
        
        Solution lastOne = sokoMap.run();
        
        
    }
    
    /**
     * 输出结果
     * 
     * @param lastOne
     *            
     * @see [类、类#方法、类#成员]
     */
    public static void outputResult(Solution lastOne)
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
        
    }
    
}
