package com.jaxer.www.model;

import com.jaxer.www.enums.CellType;

public class Cell extends Zuobiao
{
    
    private CellType item = CellType.empty;
    
    public Cell(int x, int y, CellType item)
    {
        super(x, y);
        this.item = item;
    }
    
    public void setItem(CellType type)
    {
        this.item = type;
    }
    
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
                return "W";
            case gole:
                return ".";
            default:
                return "´í";
        }
    }
    
}