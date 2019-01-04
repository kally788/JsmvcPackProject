package com.jsmvc.packproject.make;

import java.io.File;

import com.jsmvc.packproject.Config;
import com.jsmvc.utils.FileUnit;

/**   
 * @title: 打包HTML目录
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:50:44 
 *
 */
public class HTML{
	private Config config;
	private Make make;
	private String dir;
	
	//递归把所有HTML打包进一个文件并进行压缩
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
				String outFile = config.getTmpDir()+dir+"/"+config.getCompression()+".min.html";
				String[] u = node.getPath().substring(config.getInputDir().length()).split("\\.html")[0].replace("\\","/").split("/");
				String tplName = "";
				if(u.length>0){
					for(int j=0;j<u.length;j++){
						if(tplName.equals("")){
                            tplName = u[j];
                        }else{
                            tplName += "."+u[j];
                        }
					}
				}
				FileUnit.writeStringToFile("<jsmvc id=\""+tplName+"\">", outFile, true);
			    FileUnit.copyJSMinFile(node.getPath(), outFile, true);
			    FileUnit.writeStringToFile("</jsmvc>", outFile, true);
			}
		}
	}
	
	/**
	 * 打包主框架HTML
	 * @param path HTML目录
	 * @throws Exception 
	 */
	public void pack() throws Exception{
		dir = config.getDirHtml();
		reduce(config.getInputDir()+dir);
	}
	
	/**
	 * 打包扩展模块HTML目录
	 * @param extName 扩展模块名称
	 * @throws Exception 
	 */
	public void packExt(String extName) throws Exception{
		dir = config.getDirExt()+"/"+extName+"/"+config.getDirExtHtml();
		reduce(config.getInputDir()+dir);
	}
	
	public HTML(Config conf, Make mak){
		config = conf;
		make = mak;
	}
}
