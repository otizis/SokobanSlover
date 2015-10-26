package com.jaxer.www.model;

import com.jaxer.www.enums.CellType;

public class Cell extends Zuobiao
{
    public Cell()
    {
    
    }
    private CellType item = CellType.empty;
    
    public Cell(int x, int y, CellType item)
    {
        super(x, y);
        this.item = item;
    }
    
    public boolean check(CellType item)
    {
        return this.item == item;
        
    }
    
  
     public Cell myClone()
    {
        Cell b = new Cell(x, y, item);
        
        return b;
        
    }
 
    public String draw()
    {
//        if (item == CellType.player)
//        {
//            return "Œ‚";
//        }
//        if (isGoal)
//        {
//            if (item == CellType.empty)
//            {
//                return "£Ô";
//            }
//            if (item == CellType.gole)
//            {
//                return "÷–";
//            }
//            
//        }
        if (item == CellType.empty)
        {
            return " ";
        }
        if (item == CellType.wall)
        {
            return "#";
        }
        if (item == CellType.gole)
        {
            return "O";
        }
        
        return "¥Ì";
    }
    
}
