package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.Util.MapStrUtil;
import com.jaxer.www.manager.MapLib;
import com.jaxer.www.model.SokoMap;
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
     * ������
     * 
     * @param lastOne
     *            
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void outputResult(Solution lastOne)
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
                gonglv.add(0, solut.drawAfter());
                solut = solut.getLastSolution();
            } while (solut != null);
            
            for (int i = 0; i < gonglv.size(); i++)
            {
                System.out.println("��" + i + "��");
                System.out.println(gonglv.get(i));
            }
        }
        
    }
    
}
