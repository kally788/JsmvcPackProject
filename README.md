JsmvcPackProject 概述
=====
JsMvc 打包工具。用于把项目的多个文件打包为一个并进行压缩处理
需要依赖 java jdk-7 环境

### 使用方式：
1. 配置
打开配配置文件  config.conf
修改 inputDir 你要打包的源码目录路径
修改 outputDir 打包后放置目录
其它的配置可以使用默认值

2.打包
执行 startup.bat
根据提示输入选项或者一路按回车即可

如果在linux下打包，需要自己写一个sh脚本。可参考startup.bat脚本