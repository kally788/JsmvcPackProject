@echo off
echo "以下输入项如果不输入时，默认采用配置文件中的值"
echo ".............................................."
echo -n "请输入项目版本号并按回车："
read ver 
echo -n "请输入项目名称并按回车："
read name
echo -n "请输入要打包的项目目录并按回车："
read input
echo -n "请输入打包后输出的位置并按回车："
read output
java -jar JsmvcPackProject-v1.0.jar "$ver" "$name" "$input" "$output"
echo "打包已完成!"