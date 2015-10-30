package com.jaxer.www.model;

import com.jaxer.www.enums.CellType;

public class Cell extends Zuobiao
{
    
    private CellType item = CellType.empty;
    
    public Cell(int x, int y, CellType item)
    {
        super(x, y);
        this.item = item;
        this.str = String.valueOf(x).concat(",").concat(String.valueOf(y));
    }
    
    private String str;
    
    public boolean check(CellType item)
    {
        return this.item == item;
        
    }
    
    public String draw()
    {
        switch (item)
        {
            case empty:
                return " ";
            case wall:
                return "#";
            case gole:
                return "O";
            default:
                return "��";
        }
    }
    
    public String getStr()
    {
        return this.str;
    }
    
}
