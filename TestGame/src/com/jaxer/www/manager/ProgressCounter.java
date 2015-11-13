package com.jaxer.www.manager;

import com.jaxer.www.Util.Logger;

public class ProgressCounter
{
    private long total;
    
    private long progress = 0;
    
    private int lastPrecent = 0;
    
    private String desc;
    
    /**
     * 输出精度的颗粒
     */
    private final int accuracy = 20;
    
    public ProgressCounter(long total, String desc)
    {
        this.total = total;
        this.desc = desc;
    }
    
    public void addProgress()
    {
        if (total < 50000)
        {
            return;
        }
        progress++;
        if ((progress * 100 / total) > lastPrecent)
        {
            StringBuilder build = new StringBuilder(desc);
            build.append(lastPrecent);
            build.append("% 共");
            build.append(total);
            Logger.info(build.toString());
            lastPrecent += accuracy;
        }
        
    }
    
    /**
     * 成功结束
     * 
     * @see [类、类#方法、类#成员]
     */
    public void end()
    {
        StringBuilder build = new StringBuilder(desc);
        build.append(lastPrecent);
        build.append("% 共");
        build.append(total);
        build.append(",成功结束");
        Logger.info(build.toString());
        
    }
    
    /** {@inheritDoc} */
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ProgressCounter [total=");
        builder.append(total);
        builder.append(", progress=");
        builder.append(progress);
        builder.append(", lastPrecent=");
        builder.append(lastPrecent);
        builder.append(", desc=");
        builder.append(desc);
        builder.append(", accuracy=");
        builder.append(accuracy);
        builder.append("]");
        return builder.toString();
    }
    
}
