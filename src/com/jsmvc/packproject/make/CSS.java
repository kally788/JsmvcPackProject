package com.jsmvc.packproject.make;

import java.io.File;

import com.jsmvc.packproject.Config;
import com.jsmvc.utils.FileUnit;

/**   
 * @title: 打包CSS目录
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:48:26 
 *
 */
public class CSS{
	private Config config;
	private Make make;
	private String dir;
	
	//递归把所有CSS打包进一个文件，未进行压缩处理，JSMIN算法压缩CSS后存在错误
	private void reduce(String path) throws Exception{
		File dirs = new File(path);
		if(!dirs.exists() || !dirs.isDirectory()){
			return;
		}
		File[] list = dirs.listFiles();
		for (int i = 0; i < list.length; i++) {
			File node = list[i];
			if (node.isDirectory()) {
				reduce(node.getPath());
			}else if(make.loadCheck(node.getPath())){
			    FileUnit.copyFile(node.getPath(), config.getTmpDir()+dir+"/"+config.getCompression()+".min.css", true);
			}
		}
	}
	
	/**
	 * 打包主框架CSS目录
	 * @param path CSS目录
	 * @throws Exception 
	 */
	public void pack() throws Exception{
		dir = config.getDirCss();
		reduce(config.getInputDir()+dir);
	}
	
	/**
	 * 打包扩展模块CSS目录
	 * @param extName 扩展模块名称
	 * @throws Exception 
	 */
	public void packExt(String extName) throws Exception{
		dir = config.getDirExt()+"/"+extName+"/"+config.getDirExtCss();
		reduce(config.getInputDir()+dir);
	}
	
	public CSS(Config conf, Make mak){
		config = conf;
		make = mak;
	}
}
