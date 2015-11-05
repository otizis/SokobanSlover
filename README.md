# sokoban slover 推箱子解法
		输入一个推箱子地图，输出移动箱子达到终点的解法。
		支持多个箱子多个目的地。

##使用方法：

导入工程，使用MapLib.java中或添加新的地图到MapLib.java。
修改main中的使用的地图，run一下main方法，在控制台看到输出。

##地图示例：
    public static final String map1 = // r
              "########|" // LR
            + "###--###|" // LR
            + "###--###|" // LR
            + "#--$*-##|" // LR
            + "#--.*--#|" // LR
            + "###-.--#|" // LR
            + "###$--##|" // LR
            + "###@####|" // LR
            + "########"; // LR
            
    public static final StringBuilder test5 = new StringBuilder()// LR
        .append("SSSSMSSSS;")
        .append("SBPBBBBBS;")
        .append("SSSSSSSSS;")
        .append("MMMMSMMMM;")
        .append("MMMGSSMMM;")
        .append("MMMGSSMMM;")
        .append("MMMGGGMMM;")
        .append("MMMGSSMMM");
        
支持以上两种格式。

##结果示例：
  
        W W W W W W W W 
        W W W X X W W W 
        W W W     W W W 
        W X     O X W W 
        W X   O O   X W 
        W W W   O   X W 
        W W W   X X W W 
        W W W X W W W W 
        W W W W W W W W 
        
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B B   W W 
        W     O B     W 
        W W W   O     W 
        W W W B     W W 
        W W W a W W W W 
        W W W W W W W W 
        
        开始走第1层分支，有走法：1
        开始走第2层分支，有走法：2
        开始走第3层分支，有走法：2
        开始走第4层分支，有走法：5
        开始走第5层分支，有走法：9
        开始走第6层分支，有走法：13
        开始走第7层分支，有走法：16
        开始走第8层分支，有走法：24
        开始走第9层分支，有走法：29
        开始走第10层分支，有走法：36
        开始走第11层分支，有走法：37
        开始走第12层分支，有走法：27
        开始走第13层分支，有走法：23
        开始走第14层分支，有走法：16
        开始走第15层分支，有走法：12
        开始走第16层分支，有走法：17
        开始走第17层分支，有走法：12
        开始走第18层分支，有走法：11
        开始走第19层分支，有走法：11
        开始走第20层分支，有走法：11
        开始走第21层分支，有走法：14
        开始走第22层分支，有走法：21
        开始走第23层分支，有走法：29
        开始走第24层分支，有走法：45
        开始走第25层分支，有走法：54
        开始走第26层分支，有走法：52
        开始走第27层分支，有走法：41
        开始走第28层分支，有走法：29
        开始走第29层分支，有走法：17
        开始走第30层分支，有走法：9
        开始走第31层分支，有走法：4
        开始走第32层分支，有走法：3
        开始走第33层分支，有走法：5
        开始走第34层分支，有走法：7
        开始走第35层分支，有走法：7
        开始走第36层分支，有走法：6
        开始走第37层分支，有走法：4
        开始走第38层分支，有走法：5
        开始走第39层分支，有走法：4
        开始走第40层分支，有走法：3
        开始走第41层分支，有走法：3
        开始走第42层分支，有走法：8
        开始走第43层分支，有走法：8
        开始走第44层分支，有走法：4
        开始走第45层分支，有走法：4
        开始走第46层分支，有走法：5
        开始走第47层分支，有走法：7
        开始走第48层分支，有走法：11
        开始走第49层分支，有走法：12
        开始走第50层分支，有走法：11
        开始走第51层分支，有走法：7
        开始走第52层分支，有走法：2
        耗时：709
        第0步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B B   W W 
        W     O B     W 
        W W W   O     W 
        W W W B     W W 
        W W W a W W W W 
        W W W W W W W W 
        
        第1步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B B   W W 
        W     O B     W 
        W W W B O     W 
        W W W ↑     W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第2步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B B   W W 
        W     B ←     W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第3步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B B   W W 
        W   B ← O     W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第4步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W     ↑ B   W W 
        W   B O O     W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第5步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W   → B O     W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第6步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W     → B     W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第7步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W     O → B   W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第8步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W     O B ←   W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第9步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W     B ←   W W 
        W     O B     W 
        W W W B O     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第10步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W     B O   W W 
        W     O ↓     W 
        W W W B B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第11步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W     → B   W W 
        W     O O     W 
        W W W B B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第12步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W     B O     W 
        W W W ↑ B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第13步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W   B ← O     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第14步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W   → B O     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第15步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W       B   W W 
        W     → B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第16步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W     B ←   W W 
        W     O B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第17步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W   B ← O   W W 
        W     O B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第18步
        
        W W W W W W W W 
        W W W     W W W 
        W W W ↓   W W W 
        W   B B O   W W 
        W     O B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第19步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W   B ↓ O   W W 
        W     B B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第20步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W   B   O   W W 
        W     B B     W 
        W W W   → B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第21步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W   B   B   W W 
        W     B ↑     W 
        W W W   O B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第22步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W   B   B   W W 
        W     B O     W 
        W W W   B ←   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第23步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W   B   ↑   W W 
        W     B O     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第24步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W   B   O   W W 
        W     ↓ O     W 
        W W W B B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第25步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W   → B O   W W 
        W     O O     W 
        W W W B B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 

        第26步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W     → B   W W 
        W     O O     W 
        W W W B B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第27步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       B   W W 
        W     B O     W 
        W W W ↑ B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第28步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       B   W W 
        W   B ← O     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第29步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       B   W W 
        W   → B O     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第30步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       B   W W 
        W     → B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第31步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W     B ←   W W 
        W     O B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第32步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W     B O   W W 
        W     O → B   W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第33步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W     B O   W W 
        W     O O ↓   W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第34步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W   B ← O   W W 
        W     O O     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第35步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   ↓ W W W 
        W   B   B   W W 
        W     O O     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第36步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W   B   ↓   W W 
        W     O B     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第37步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W   → B O   W W 
        W     O B     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第38步
        
        W W W W W W W W 
        W W W     W W W 
        W W W B   W W W 
        W     ↑ O   W W 
        W     O B     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第39步
        
        W W W W W W W W 
        W W W     W W W 
        W W W ↓   W W W 
        W     B O   W W 
        W     O B     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第40步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B O   W W 
        W     B ←     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第41步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B O   W W 
        W   B ← O     W 
        W W W   B B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第42步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B O   W W 
        W   B O O B   W 
        W W W   B ↑   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第43步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     B O   W W 
        W   B O B ←   W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第44步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W     ↓ O   W W 
        W   B B B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第45步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W       O   W W 
        W   B B B     W 
        W W W   → B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第46步
        
        W W W W W W W W 
        W W W     W W W 
        W W W     W W W 
        W       B   W W 
        W   B B ↑     W 
        W W W   O B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第47步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       ↑   W W 
        W   B B O     W 
        W W W   O B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第48步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       O   W W 
        W   B ↓ O     W 
        W W W B O B   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第49步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       O   W W 
        W   B O O     W 
        W W W B B ←   W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第50步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       O   W W 
        W   → B O     W 
        W W W B B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第51步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       O   W W 
        W     → B     W 
        W W W B B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第52步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   B W W W 
        W       O   W W 
        W     B B     W 
        W W W ↑ B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
        
        第53步
        
        W W W W W W W W 
        W W W     W W W 
        W W W   ↓ W W W 
        W       B   W W 
        W     B B     W 
        W W W   B     W 
        W W W       W W 
        W W W   W W W W 
        W W W W W W W W 
