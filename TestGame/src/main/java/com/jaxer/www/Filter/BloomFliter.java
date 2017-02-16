package com.jaxer.www.Filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.jaxer.www.api.MapFliter;

public class BloomFliter implements MapFliter
{
    int expectedInsertions = Integer.MAX_VALUE / 10;
    
    public BloomFliter()
    {
        clear();
    }
    
    public BloomFliter(int expectedInsertions)
    {
        this.expectedInsertions = expectedInsertions;
        clear();
    }
    
    BloomFilter<byte[]> bloomFilter;
    
    @Override
    public boolean isExist(byte[] str)
    {
        if (bloomFilter.mightContain(str))
        {
            return true;
        }
        bloomFilter.put(str);
        return false;
    }
    
    @Override
    public void clear()
    {
        bloomFilter =
            BloomFilter.create(Funnels.byteArrayFunnel(), expectedInsertions);
    }
    
}
