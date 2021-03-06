package com.jaxer.www.model;

import com.jaxer.www.myexception.MyException;

/**
 * 地图模型
 * 
 */
public class IndexMap
{
    private short[] mapBytes;
    
    private int max_x;
    
    /**
     * <默认构造函数>
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
     * @return 返回 max_x
     */
    public int getMax_x()
    {
        return max_x;
    }
    
    /**
     * 对max_x进行赋值
     */
    public void setMax_x(int max_x)
    {
        this.max_x = max_x;
    }
    
    /**
     * @return 返回 mapBytes
     */
    public short[] getMapBytes()
    {
        return mapBytes;
    }
    
    /**
     *  对mapBytes进行赋值
     */
    public void setMapBytes(short[] mapBytes)
    {
        this.mapBytes = mapBytes;
    }
    
    /**
     * 两个坐标一个索引，表示在同一区域
     * 
     * @param z
     * @param b
     * @return [参数说明]
     *         
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isEq(Zuobiao z, Zuobiao b)
    {
        if (mapBytes == null)
        {
            throw new MyException("未初始化");
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
