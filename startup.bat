@echo off
echo "�������������������ʱ��Ĭ�ϲ��������ļ��е�ֵ"
echo ".............................................."
echo "��������Ŀ�汾�Ų����س����磺v1.0 �������ַ���"
set /p ver=
echo "������Ҫ�������ĿĿ¼�����س����磺d:/input/"
set /p input=
echo "���������������λ�ò����س����磺d:/output/"
set /p output=
java.exe -jar JsmvcPackProject-v1.0.jar "%ver%" "%input%" "%output%"
echo �������ɣ��밴������˳�...
pause>nul