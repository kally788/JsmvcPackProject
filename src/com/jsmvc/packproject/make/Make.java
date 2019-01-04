package com.jsmvc.packproject.make;

import java.io.File;

import com.jsmvc.packproject.Config;
import com.jsmvc.utils.FileUnit;
import com.jsmvc.utils.Md5;

public class Make {
	private Config config;
	private String checkFile;
	
	private JS js;
	private CSS css;
	private HTML html;
	private Images images;
	
	/**
	 * 设置加载资源列表入口页，index.html 和 扩展包中的 package.json
	 * @param path 入口页文件路径
	 * @throws Exception 
	 */
	public void setLoadCheck(String path) throws Exception{
		String tmpFileName = config.getTmpDir()+Md5.getMD5(java.util.UUID.randomUUID().toString())+".html";
		FileUnit.copyJSMinFile(path, tmpFileName, false);
		checkFile = FileUnit.getFileToString(tmpFileName);
		new File(tmpFileName).delete();
	}
	
	/**
	 * 校验要打包的文件是否需要加载
	 * @param path 要校验的文件路径
	 * @return boolean
	 */
	public boolean loadCheck(String path){
		if(checkFile.indexOf(path.substring(config.getInputDir().length()).replace("\\","/"))!=-1){
			return true;
		}
		return false;
	}
	
	public Make(Config conf) throws Exception{
		config = conf;
		js = new JS(conf, this);
		css = new CSS(conf, this);
		html = new HTML(conf, this);
		images = new Images(conf);
	}

	public JS getJs() {
		return js;
	}

	public CSS getCss() {
		return css;
	}

	public HTML getHtml() {
		return html;
	}

	public Images getImages() {
		return images;
	}
	
	
}
