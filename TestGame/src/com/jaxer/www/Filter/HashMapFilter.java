package com.jaxer.www.Filter;

import java.util.ArrayList;
import java.util.HashMap;

import com.jaxer.www.api.MapFliter;

public class HashMapFilter implements MapFliter
{
    
    /**
     * ��ͼ����ֵ��������ֹ��ͻ��¼�� ����ظ�����ʾ���ֹ������߳���������߷�������Ϊ�����ŵ��߷�
     */
    private static HashMap<String, ArrayList<String>> mapMap =
        new HashMap<String, ArrayList<String>>(1024);
        
    @Override
    public boolean isExist(byte[] str)
    {
        
        return false;
    }
    
    @Override
    public void clear()
    {
        mapMap.clear();
        
    }
    
}