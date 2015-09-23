# soko
推箱子 解法
		输入一个推箱子地图，输出移动箱子达到终点的解法。
		支持多个箱子多个目的地。

使用方法：
--------
导入工程，在Main.java中，修改main中的地图，run一下main方法，在控制台看到输出。

地图示例：
--------
		StringBuilder line = new StringBuilder();
		line.append("MSSSSSMM").append(";");
		line.append("MYMMMSSS").append(";");
		line.append("PSSYSSYS").append(";");
		line.append("SGGMSYSM").append(";");
		line.append("MGGMSSSM");
M为墙，S为空，Y为箱子，G为目标点，P为玩家所在位置。
每个位置用，分隔。
每行后有；结尾。