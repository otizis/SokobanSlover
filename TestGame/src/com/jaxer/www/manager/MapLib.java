package com.jaxer.www.manager;

public class MapLib
{
    
    /**
     * 9*9最难
     */
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
            
    public static final String map2 = // r
        "--#####--|"// LR
            + "-##---###|"// LR
            + "-#------#|"// LR
            + "-#*#*#*-#|"// LR
            + "-#-#@$-##|"// LR
            + "##-#-#.#-|"// LR
            + "#------#-|"// LR
            + "#---#--#-|"// LR
            + "########-";// LR
            
    public static final String map3 = //
        "---###########|"// lr
            + "---#----#----#|"// lr
            + "####$$--$--$-#|"// lr
            + "#------##--$-#|"// lr
            + "#--$#--##$#$-#|"// lr
            + "##$-#####--@##|"// lr
            + "##---######$##|"// lr
            + "#---$--##...-#|"// lr
            + "#-$---$-$.*..#|"// lr
            + "##$##$-##..#.#|"// lr
            + "#---#-$-#-#--#|"// lr
            + "#-$$#-$-#..-.#|"// lr
            + "#---$---#....#|"// lr
            + "###-#-#--*..##|"// lr
            + "--#---###...#-|"// lr
            + "--#####-#####-";// lr
            
    public static final String test = //
        "########------;" + // LR
            "#--#---#------;" + // LR
            "#-*..--#------;" + // LR
            "#-*$-$-#------;" + // LR
            "#-.**$########;" + // LR
            "#---.$---#@-##;" + // LR
            "##.**$-#--*$##;" + // LR
            "#--#-----.$.-#;" + // LR
            "#######-$.*.-#;" + // LR
            "------#-$.$.-#;" + // LR
            "------##$.$*-#;" + // LR
            "------##--#--#;" + // LR
            "------########";
            
    /**
     * 
     * 来源：MF8第27
     * 
     */
    public static final String test2 = //
        "####---#-------|" // LR
            + "##-.-*-*-*$#*#-|" // LR
            + "#--$-#-*##-#---|" // LR
            + "#-*#-*---*-.-*#|" // LR
            + "-*---#-.##---*-|" // LR
            + "-$*-###---#*#*-|" // LR
            + "-*-*-*--*-*@---|" // LR
            + "------###--*--#"; // LR
            
    /**
     * 
     * 来源：MF8第每月一关9月17
     * 
     */
    public static final String test9_17 = //
        "-######--|"// Le
            + "-#----#--|"// Le
            + "-#--#$#--|"// Le
            + "-#-$--###|"// Le
            + "##-#--$-#|"// Le
            + "#-$$----#|"// Le
            + "#--.##$##|"// Le
            + "###.##-#-|"// Le
            + "#-$.##-##|"// Le
            + "#....#--#|"// Le
            + "##-#.$--#|"// Le
            + "-#--@#--#|"// Le
            + "-########";// Le
            
    /**
     * 6个箱子，来源：mf8论坛
     */
    public static final StringBuilder test3 = new StringBuilder()// LR
        .append("MMMMMSSSMMMMMMMMMM;")
        .append("MMMMMBSSMMMMMMMMMM;")
        .append("MMMMMSSBMMMMMMMMMM;")
        .append("MMMSSBSBSMMMMMMMMM;")
        .append("MMMSMSMMSMMMMMMMMM;")
        .append("MSSSMSMMSMMMMMSSGG;")
        .append("MSBSSBSSSSSSSSSSGG;")
        .append("MMMMMSMMMSMPMMSSGG;")
        .append("MMMMMSSSSSMMMMMMMM");
        
    /**
     * 9个箱子地图，来源百度最难地图
     */
    public static final StringBuilder test4 = new StringBuilder()// LR
        .append("MMMMMSSMSSM;")
        .append("MMMMMSSBBSM;")
        .append("MMMMMSBMSSM;")
        .append("GGGMMMSMSSM;")
        .append("GSSMSSBSMSS;")
        .append("GSSSSBSBSBS;")
        .append("GSSMSSBSMSS;")
        .append("GGGMMMSMSSM;")
        .append("MMMMMPBSSSM;")
        .append("MMMMMSSMSSM");
        
    /**
     * 银魔，来源http://bbs.mf8-china.com/forum.php?mod=viewthread&tid=37384
     */
    public static final StringBuilder test5 = new StringBuilder()// LR
        .append("SSSSMSSSS;")
        .append("SBPBBBBBS;")
        .append("SSSSSSSSS;")
        .append("MMMMSMMMM;")
        .append("MMMGSSMMM;")
        .append("MMMGSSMMM;")
        .append("MMMGGGMMM;")
        .append("MMMGSSMMM");
        
    /**
     * 9*9地图，华容道提问，是不是最难地图
     */
    public static final StringBuilder test6 = new StringBuilder()// LR
        .append("MMMMMMMM;")
        .append("MMSPSMMM;")
        .append("MMSMBSSM;")
        .append("MSRGSGSM;")
        .append("MSSBBSMM;")
        .append("MMMSMGMM;")
        .append("MMMSSSMM;")
        .append("MMMMMMMM");
        
}
