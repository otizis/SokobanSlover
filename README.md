# soko
推箱子 解法
输入一个推箱子地图，输出移动箱子达到终点的解法。<br/>
支持多个箱子多个目的地。<br/>

使用方法：<br/>
导入工程，在Main.java中，修改main中的地图，run一下main方法，在控制台看到输出。<br/>

地图示例：<br/>
        StringBuilder line = new StringBuilder();<br/>
        line.append("MSSSSSMM").append(";");<br/>
        line.append("MYMMMSSS").append(";");<br/>
        line.append("PSSYSSYS").append(";");<br/>
        line.append("SGGMSYSM").append(";");<br/>
        line.append("MGGMSSSM");<br/>
M为墙，S为空，Y为箱子，G为目标点，P为玩家所在位置。<br/>
每个位置用，分隔。<br/>
每行后有；结尾。<br/>