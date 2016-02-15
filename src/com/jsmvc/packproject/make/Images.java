package com.jsmvc.packproject.make;

import com.jsmvc.packproject.Config;
import com.jsmvc.utils.FileUnit;

/**   
 * @title: 拷贝图片文件到输出目录
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:52:44 
 *
 */
public class Images {
	Config config;
	/**
	 * 拷贝主框架图片目录
	 * @param path 图片目录
	 * @throws Exception 
	 */
	public void pack() throws Exception{
		new FileUnit().copyFiles(config.getInputDir()+config.getDirImages(),config.getTmpDir()+config.getDirImages());
	}
	
	/**
	 * 拷贝扩展模块图片目录
	 * @param extName 扩展模块名称
	 * @throws Exception 
	 */
	public void packExt(String extName) throws Exception{
		new FileUnit().copyFiles(config.getInputDir()+"/"+config.getDirExt()+"/"+extName+"/"+config.getDirExtImages(),config.getTmpDir()+config.getDirExt()+"/"+extName+"/"+config.getDirExtImages());
	}
	
	public Images(Config conf){
		config = conf;
	}
}
