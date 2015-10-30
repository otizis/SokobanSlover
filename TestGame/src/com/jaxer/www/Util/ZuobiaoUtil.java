package com.jaxer.www.Util;

import com.jaxer.www.enums.AspectEnum;
import com.jaxer.www.model.SokoMap;
import com.jaxer.www.model.Zuobiao;

public class ZuobiaoUtil
{
    
    /**
     * 根据方向移动坐标,返回一个new对象，不修改入参。
     * 
     * @param moveItem 坐标
     * @param asp 方向
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Zuobiao getMove(Zuobiao moveItem, AspectEnum asp)
    {
        Zuobiao result = null;
        switch (asp)
        {
            case up:
                result = getUp(moveItem);
                break;
            case down:
                result = getDown(moveItem);
                break;
            case left:
                result = getLeft(moveItem);
                break;
            default:
                result = getRight(moveItem);
                break;
        }
        if (out(result))
        {
            return null;
        }
        return result;
    }
    
    /**
     * 获取推动箱子时，人站的坐标。不修改入参。
     * 
     * @param s 箱子坐标
     * @param asp 推箱子方向
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Zuobiao getMovePlayer(Zuobiao s, AspectEnum asp)
    {
        switch (asp)
        {
            case up:
                return getDown(s);
            case down:
                return getUp(s);
            case left:
                return getRight(s);
            default:
                return getLeft(s);
        }
    }
    
    private static Zuobiao getUp(Zuobiao s)
    {
        return new Zuobiao(s.getX(), s.getY() - 1);
    }
    
    private static Zuobiao getDown(Zuobiao s)
    {
        return new Zuobiao(s.getX(), s.getY() + 1);
    }
    
    private static Zuobiao getLeft(Zuobiao s)
    {
        return new Zuobiao(s.getX() - 1, s.getY());
    }
    
    private static Zuobiao getRight(Zuobiao s)
    {
        return new Zuobiao(s.getX() + 1, s.getY());
    }
    
    /**
     * 判断box是否出界
     * 
     * @param box
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean out(Zuobiao box)
    {
        if (box.getX() < 0 || box.getX() > SokoMap.max_x)
        {
            return true;
        }
        if (box.getY() < 0 || box.getY() > SokoMap.max_y)
        {
            return true;
        }
        return false;
    }

}
