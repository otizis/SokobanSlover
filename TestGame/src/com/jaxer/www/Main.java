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

        String mapStr = MapStrUtil.convert(MapLib.test3);
        SokoMap sokoMap = new SokoMap(mapStr);
        sokoMap.run();
        
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
                gonglv.add(0, solut.toString());
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
