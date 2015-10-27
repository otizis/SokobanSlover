package com.jaxer.www;

import java.util.ArrayList;

import com.jaxer.www.Util.TimeStamps;
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
     * ������
     * 
     * @param lastOne
     *            
     * @see [�ࡢ��#��������#��Ա]
     */
    private static void outputResult(Solution lastOne)
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
        TimeStamps.printMap();
        
    }
    
}
