/*
 * �� �� ��:  IndexMap.java
 * ��    Ȩ:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * ��    ��:  <����>
 * �� �� ��:  yejiangtao
 * �޸�ʱ��:  2015��11��24��
 * ���ٵ���:  <���ٵ���>
 * �޸ĵ���:  <�޸ĵ���>
 * �޸�����:  <�޸�����>
 */
package com.jaxer.www.model;

import com.jaxer.www.myexception.MyException;

/**
 * <һ�仰���ܼ���> <������ϸ����>
 * 
 * @author yejiangtao
 * @version [�汾��, 2015��11��24��]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class IndexMap
{
    private short[] mapBytes;
    
    private int max_x;
    
    /**
     * <Ĭ�Ϲ��캯��>
     * 
     * @param man
     */
    public IndexMap(short[] mapBytes, int max_x, Zuobiao man)
    {
        super();
        this.mapBytes = mapBytes;
        this.max_x = max_x;
        setManIndex(man);
    }
    
    /**
     * @return ���� max_x
     */
    public int getMax_x()
    {
        return max_x;
    }
    
    /**
     * @param ��max_x���и�ֵ
     */
    public void setMax_x(int max_x)
    {
        this.max_x = max_x;
    }
    
    /**
     * @return ���� mapBytes
     */
    public short[] getMapBytes()
    {
        return mapBytes;
    }
    
    /**
     * @param ��mapBytes���и�ֵ
     */
    public void setMapBytes(short[] mapBytes)
    {
        this.mapBytes = mapBytes;
    }
    
    /**
     * ��������һ����������ʾ��ͬһ����
     * 
     * @param z
     * @param b
     * @return [����˵��]
     *         
     * @return boolean [��������˵��]
     * @exception throws [Υ������] [Υ��˵��]
     * @see [�ࡢ��#��������#��Ա]
     */
    public boolean isEq(Zuobiao z, Zuobiao b)
    {
        if (mapBytes == null)
        {
            throw new MyException("δ��ʼ��");
        }
        if (z == null || b == null)
        {
            return false;
        }
        return getRootIndex(b) == getRootIndex(z);
    }
    
    private int getRootIndex(Zuobiao b)
    {
        int index = b.getX() + (b.getY() * (max_x + 1));
        int i = mapBytes[index];
        if (i == -1)
        {
            return -1;
        }
        while (i != mapBytes[i])
        {
            i = mapBytes[i];
        }
        return i;
    }
    
    private void setManIndex(Zuobiao b)
    {
        mapBytes[mapBytes.length - 1] = (short)getRootIndex(b);
    }
}
