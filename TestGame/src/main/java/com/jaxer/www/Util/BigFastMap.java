package com.jaxer.www.Util;

import java.util.Arrays;

public class BigFastMap
{
    private Node node;
    
    private int catNum;
    
    private int radix;
    
    public BigFastMap(int radix, int catNum)
    {
        String max = "";
        
        for (int i = 0; i < catNum; i++)
        {
            max += (radix - 1);
            
        }
        int nodeRadix = Integer.valueOf(max, radix) + 1;
        Logger.info("节点数组长度：" + nodeRadix);
        this.node = new Node(nodeRadix);
        this.catNum = catNum;
        this.radix = radix;
    }
    
    public boolean contain(byte[] info)
    {
        return node.isExit(change(info));
    }
    
    public void add(byte[] info)
    {
        if (Logger.isdebug)
        {
            Logger.debug("change-->" + Arrays.toString(info));
        }
        node.add(change(info));
        
    }
    
    private int[] change(byte[] source)
    {
        if (Logger.isdebug)
        {
            Logger.debug("change-->" + Arrays.toString(source));
        }
        int hebingNum = catNum;
        int soureRadix = radix;
        
        int len = source.length / hebingNum;
        if (source.length % hebingNum != 0)
        {
            len++;
        }
        int[] newa = new int[len];
        for (int i = 0; i < newa.length; i++)
        {
            double a = 0;
            for (int j = 0; j < hebingNum; j++)
            {
                byte n;
                if (i * hebingNum + j < source.length)
                {
                    
                    n = source[i * hebingNum + j];
                }
                else
                {
                    n = 0;
                }
                
                a += Math.pow(soureRadix, j) * n;
            }
            newa[i] = (int)a;
        }
        return newa;
    }
    
    class Node
    {
        private Node[] subNode;
        
        private Boolean[] set;
        
        /** node 的等级 */
        int level;
        
        /** 数据的进制 */
        int radix;
        
        public Node(int radix)
        {
            this.level = 1;
            this.radix = radix;
        }
        
        public Node(int level, int radix)
        {
            this.level = level;
            this.radix = radix;
        }
        
        /**
         * 获取子节点
         * 
         * @return
         * @see [类、类#方法、类#成员]
         */
        public Node makeSubNode()
        {
            return new Node(this.level + 1, this.radix);
        }
        
        public boolean isExit(int[] index)
        {
            
            if (level == index.length)
            {
                if (set == null || set[index[level - 1]] == null)
                {
                    return false;
                }
                
                return set[index[level - 1]];
            }
            if (subNode == null || subNode[index[level - 1]] == null)
            {
                return false;
            }
            return subNode[index[level - 1]].isExit(index);
        }
        
        public void add(int[] info)
        {
            if (level == info.length)
            {
                if (set == null)
                {
                    set = new Boolean[radix];
                }
                set[info[level - 1]] = true;
                return;
            }
            if (subNode == null)
            {
                subNode = new Node[radix];
            }
            if (subNode[info[level - 1]] == null)
            {
                subNode[info[level - 1]] = makeSubNode();
            }
            subNode[info[level - 1]].add(info);
            
        }
        
    }
    
    public static void main(String[] args)
    {
        System.out.println(Integer.valueOf("2222", 3));
        byte[] a1 = new byte[] {0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2};
            
        byte[] a2 = new byte[] {1, 2, 2, 2, 2};
        BigFastMap a = new BigFastMap(3, 4);
        
        a.add(a1);
        
        System.out.println(a.contain(a1));
        System.out.println(a.contain(a2));
        a.add(a2);
        System.out.println(a.contain(a1));
        System.out.println(a.contain(a2));
        
    }
    
}
