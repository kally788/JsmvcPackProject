@echo off
echo "以下输入项如果不输入时，默认采用配置文件中的值"
echo ".............................................."
echo "请输入项目版本号并按回车，如：v1.0 或任意字符串"
set /p ver=
echo "请输入项目名称并按回车，如：release"
set /p name=
echo "请输入要打包的项目目录并按回车，如：d:/input/"
set /p input=
echo "请输入打包后输出的位置并按回车，如：d:/output/"
set /p output=
java.exe -jar JsmvcPackProject-v1.0.jar "%ver%" "%name%" "%input%" "%output%"
echo 打包已完成，请按任意键退出...
pause>nul