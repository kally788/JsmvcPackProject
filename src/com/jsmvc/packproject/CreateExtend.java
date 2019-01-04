package com.jsmvc.packproject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsmvc.packproject.make.Make;
import com.jsmvc.utils.FileUnit;
import com.jsmvc.utils.Log;

/**   
 * @title: 打包扩展模块
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:44:52 
 *
 */
public class CreateExtend {
	Config config;
	
	private String parseJs(File extModel, Map<String, String[]> json) throws Exception{
		String outRoute = config.getTmpDir()+config.getDirExt()+"/"+extModel.getName();
		String sourceRoute = config.getInputDir()+config.getDirExt()+"/"+extModel.getName();
		File packageFile = new File(sourceRoute+"/"+config.getExt());
		if(!packageFile.exists() || !packageFile.isFile()){
			FileUnit.deleteFile(outRoute);
			return "";
		}
		//压缩拷贝文件并读取为字符串
		FileUnit.copyJSMinFile(sourceRoute+"/"+config.getExt(), outRoute+"/"+config.getExt(), false);
		String packageJs = FileUnit.getFileToString(outRoute+"/"+config.getExt());
		Pattern regex = Pattern.compile("\\{(.*?)\\}");  
        Matcher matcher = regex.matcher(packageJs);
        String packageObject = "{}";
        if(matcher.find()){
        	packageObject = matcher.group(0);
        }
		JSONObject packagejson = JSONObject.parseObject(packageObject) ; 
		//修改加载项
		String route = config.getDirExt()+"/"+extModel.getName();
		for(java.util.Map.Entry<String,Object> entry:packagejson.entrySet()){
			switch(entry.getKey()){
			case "css":
				if(((JSONArray)entry.getValue()).size()>0){
					json.put("css", new String[]{route+"/"+config.getDirExtCss()+"/"+config.getCompression()+".min.css"});
				}
				break;
			case "js":
				//一级目录下的JS不打包
				String[] old = new String[((JSONArray)entry.getValue()).size()];
				((JSONArray)entry.getValue()).toArray(old);
				
				JSONArray newJs = new JSONArray();
				for(int j=0; j<old.length; j++){
					String jsURL = old[j];
					if(jsURL != null && (jsURL.toUpperCase().indexOf("HTTP://") == 0 || jsURL.toUpperCase().indexOf("HTTPS://") == 0)){
						newJs.add(jsURL);
						continue;
					}
					if(jsURL.split(config.getDirExtJs())[1].split("/").length <= 2){
						String[] name = jsURL.split("/");
						String prefix = name[name.length-1].substring(0,name[name.length-1].lastIndexOf("."));
						newJs.add(route+"/"+config.getDirExtJs()+"/"+prefix+".min.js");
					}
				}
				if(newJs.size() < old.length){
					newJs.add(route+"/"+config.getDirExtJs()+"/"+config.getCompression()+".min.js");
				}
				String[] jsList = new String[newJs.size()];
				newJs.toArray(jsList);
				if(jsList.length > 0){
					json.put("js", jsList);
				}
				break;
			case "html":
				if(((JSONArray)entry.getValue()).size()>0){
					json.put("html", new String[]{route+"/"+config.getDirExtHtml()+"/"+config.getCompression()+".min.html"});
				}
				break;
			}
        }
		return packageJs;
	}
	
	public CreateExtend(Config conf, Make make, Log log) throws Exception{
		config = conf;
		File ext = new File(config.getInputDir()+config.getDirExt());
    	if(!ext.exists() || !ext.isDirectory()){ 
    		return;
    	}
    	int num = 0;
    	File[] extList = ext.listFiles();
    	for (int i = 0; i < extList.length; i++) {
			File extModel = extList[i];
			if(!extModel.isDirectory()){
				continue;
			}
			log.info("  no. ["+(++num)+"] extend:"+extModel.getName()+" ...");
			log.info("    "+config.getExt());
			//重新生成package.json文件
			String outRoute = config.getTmpDir()+config.getDirExt()+"/"+extModel.getName();
			String sourceRoute = config.getInputDir()+config.getDirExt()+"/"+extModel.getName();
			Map<String, String[]> json = new HashMap<String, String[]>();
			String packageJs = parseJs(extModel, json);
			if(packageJs.equals("")){
				log.warning("ext does not exist:"+sourceRoute+"/"+config.getExt()); 
				continue;
			}
			FileUnit.writeStringToFile(packageJs.split("\\{")[0]+JSONObject.toJSON(json).toString(), outRoute+"/"+config.getExt(), false);
			//打包扩展包资源文件
			make.setLoadCheck(config.getInputDir()+config.getDirExt()+"/"+extModel.getName()+"/"+config.getExt());
			
			log.info("    "+config.getDirExtJs());
			make.getJs().packExt(extModel.getName());
			log.info("    "+config.getDirExtCss());
			make.getCss().packExt(extModel.getName());
			log.info("    "+config.getDirExtImages());
			make.getImages().packExt(extModel.getName());
			log.info("    "+config.getDirExtHtml());
			make.getHtml().packExt(extModel.getName());
			
    	}
	}
}
