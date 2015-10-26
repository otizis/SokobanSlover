package com.jaxer.www.api;

import com.jaxer.www.enums.AspectEnum;

public interface Moveable
{
    public void moveUp();
    
    public void moveDown();
    
    public void moveLeft();
    
    public void moveRight();
    
    public void moveByAspect(AspectEnum aspect);
    
}
