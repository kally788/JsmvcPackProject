package com.jsmvc.packproject.make;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jsmvc.packproject.Config;
import com.jsmvc.utils.FileUnit;

/**   
 * @title: 打包JS文件，对于JS根目录下的文件不会打包到一起
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:54:43 
 *
 */
public class JS{
	private Config config;
	private Make make;
	private String dir;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, HashMap> namespace;
	//递归打包JS到一个文件并进行压缩
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void reduce(String path) throws Exception{
    	File file = new File(path);
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File node = fileList[i];
			if (node.isDirectory()) {
				reduce(node.getPath());
				continue;
			}
			if(make.loadCheck(node.getPath())){
				//命名空间
				String[] route = node.getPath().substring(config.getInputDir().length()).replace("\\","/").split("/");
				if(!namespace.containsKey(route[0])){
					namespace.put(route[0], new HashMap<String, HashMap>());
				}
				HashMap childMap = namespace.get(route[0]);
				for(int j = 1;j<route.length-1;j++){
					if(!childMap.containsKey(route[j])){
						childMap.put(route[j], new HashMap<String, HashMap>());
					}
					childMap = (HashMap) childMap.get(route[j]);
				}
				//拷贝
				FileUnit.copyJSMinFile(node.getPath(), config.getTmpDir()+dir+"/"+config.getCompression()+".min.js", true);
			}
		}
    }
	
	//打包并压缩JS，对于根目录下的不会打包到一个文件，而是分开各自压缩并保持原样
	private void pull() throws Exception{
		File dirs = new File(config.getInputDir()+dir);
		if(!dirs.exists() || !dirs.isDirectory()){
			return;
		}
		File[] list = dirs.listFiles();
		for (int i = 0; i < list.length; i++) {
			File node = list[i];
			if (node.isDirectory()) {
				reduce(node.getPath());
			}else if(make.loadCheck(node.getPath())){
				String fileName = node.getName();
			    String prefix = fileName.substring(0,fileName.lastIndexOf("."));
			    FileUnit.copyJSMinFile(node.getPath(), config.getTmpDir()+dir+"/"+prefix+".min.js", false);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private String createNamespace(HashMap v, String var, String p){
		if(v.size()>0){
			Iterator<?> iter = v.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String np;
				if(p.equals("")){
					np = (String) entry.getKey();
					var += "var "+np+";if(!"+np+"){"+np+"={}};";
				}else{
					np = p+"."+entry.getKey();
					var += "if(!"+np+"){"+np+"={}};";
				}
				var = createNamespace((HashMap)entry.getValue(), var, np);
			}
		}
		return var;
	}
	
	/**
	 * 打包主框架JS文件，对于一级目录下的文件不打包
	 * @param path JS目录
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public void pack(String path) throws Exception{
		namespace = new HashMap<String, HashMap>();
		dir = path;
		pull();
		//追加命名空间到压缩文件顶部
		String out = config.getTmpDir()+dir+"/"+config.getCompression()+".min.js";
		if(new File(out).exists()){
			FileUnit.writeStringToFile(createNamespace(namespace,"",""),out+".tmp",false);
			FileUnit.copyFile(out,out+".tmp",true);
			FileUnit.copyFile(out+".tmp",out,false);
			FileUnit.deleteFile(out+".tmp");
		}
	}
	
	/**
	 * 打包扩展模块JS文件，对于一级目录下的文件不打包
	 * @param extName 扩展模块名称
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public void packExt(String extName) throws Exception{
		namespace = new HashMap<String, HashMap>();
		dir = config.getDirExt()+"/"+extName+"/"+config.getDirExtJs();
		pull();
		//追加命名空间到压缩文件顶部
		String out = config.getTmpDir()+dir+"/"+config.getCompression()+".min.js";
		if(new File(out).exists()){
			FileUnit.writeStringToFile(createNamespace(namespace,"",""),out+".tmp",false);
			FileUnit.copyFile(out,out+".tmp",true);
			FileUnit.copyFile(out+".tmp",out,false);
			FileUnit.deleteFile(out+".tmp");
		}
	}

	public JS(Config conf, Make mak){
		config = conf;
		make = mak;
	}
}
