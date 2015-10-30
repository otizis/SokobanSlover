package com.jaxer.www.api;

public interface MapFliter<T>
{
    public boolean isExist(T t);
    
    public void clear();
    
}
