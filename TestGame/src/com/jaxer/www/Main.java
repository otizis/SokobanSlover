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

        String mapStr = MapStrUtil.convert(MapLib.test3);
        SokoMap sokoMap = new SokoMap(mapStr);
        sokoMap.run();
        
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
                gonglv.add(0, solut.toString());
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
