package com.jsmvc.packproject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jsmvc.packproject.make.Make;
import com.jsmvc.utils.FileUnit;
import com.jsmvc.utils.Log;
import com.jsmvc.utils.Md5;

/**   
 * @title: 打包主项目
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:45:44 
 *
 */
public class CreateMain {
	
	private Config config;
	
	//修改启动入口函数
	private String run(String index) throws IOException{
		String runName = config.getRun().substring(0, config.getRun().lastIndexOf(".js"));
		String r = (config.getDirJSMvc().replace("$", "\\$"))+"\\."+runName+"\\.start\\((.*)\\)";
		
		Pattern runRegex = Pattern.compile(r);  
        Matcher runMatcher = runRegex.matcher(index);
        
        String runPath = config.getDirJSMvc()+"."+runName+".start(";
        
        if(!runMatcher.find()){
        	return runPath+")";
        }
        String runString = runMatcher.group(0);
        String[] arr = runString.split("start\\(");
        
        if(arr.length<2){
        	return runPath+")";
        }
        String[] parameter = arr[1].substring(0,arr[1].length()-1).split(",");
        long tmpVer = System.currentTimeMillis();
        if(parameter.length == 0 || (parameter.length == 1 && parameter[0].equals(""))){
        	return runPath+"null,null,null,null,"+(config.getVersion().equals("")?"'"+tmpVer+"'":"'"+config.getVersion()+"'")+")";
        }
        String[] paream = new String[5];
        for(int i=0;i<parameter.length;i++){
        	paream[i] = parameter[i];
        }
        if(!config.getVersion().equals("")){
        	paream[4] = "'"+config.getVersion()+"'";
        }else{
        	paream[4] = "'"+tmpVer+"'";
        }
		return runPath+paream[0]+","+paream[1]+","+paream[2]+","+paream[3]+","+paream[4]+")";
	}
	
	//压缩JS资源列表
	private String parseJs(String index) throws IOException{
		Pattern jsRegex = Pattern.compile("js=\\[(.*)\\]\\]");  
        Matcher jsMatcher = jsRegex.matcher(index);
        
        String js = "";
        
        if(!jsMatcher.find()){
        	return js;
        }
        String jsString = jsMatcher.group(0);
    	if(jsString.length() <= 7){
    		return js;
    	}
    	
    	js = "js=[";
    	jsString = jsString.substring(5,jsString.length()-2);
		String[] jsList = jsString.split("],\\[");
		for(int i=0; i<jsList.length;i++){
			String child = "[";
			String[] childList = jsList[i].split(",");
			//修改JS文件路径
			HashMap<String, String> key = new HashMap<String, String>();
			for(int j=0; j<childList.length; j++){
				String jsStr = childList[j];
				if(jsStr != null && (jsStr.toUpperCase().indexOf("HTTP://") == 1 || jsStr.toUpperCase().indexOf("HTTPS://") == 1)){
					if(child.equals("[")){
						child += jsStr;
					}else{
						child += ","+jsStr;
					}
					continue;
				}
				String[] jsDir = jsStr.split("/");
				if(jsDir.length>2){
					String keyStr = jsDir[0];
					key.put(keyStr, keyStr);
				}else{
					//一级目录下的JS不打包
					if(child.equals("[")){
						child += jsStr.substring(0,jsStr.lastIndexOf(".js"))+".min.js\"";
					}else{
						child += ","+jsStr.substring(0,jsStr.lastIndexOf(".js"))+".min.js\"";
					}
				}
			}
			//需要打包的JS文件路径压缩为一个路径
			if(key.size()>0){
				Iterator<?> iter = key.entrySet().iterator();
				while (iter.hasNext()) {
    				@SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry) iter.next();
    				if(child.equals("[")){
    					child += entry.getValue()+"/"+config.getCompression()+".min.js\"";
    				}else{
    					child += ","+entry.getValue()+"/"+config.getCompression()+".min.js\"";
    				}
				}
			}
			child += "]";
			
			if(js.equals("js=[")){
				js += child;
			}else{
				js += "," + child;
			}
		}
        return js + "];";
	}
	
	//压缩HTML资源列表
	private String parseHtml(String index) throws IOException{
		Pattern htmlRegex = Pattern.compile("html=\\[(.*?)\\]");  
        Matcher htmlMatcher = htmlRegex.matcher(index);
        
        String html = "";
        
        if(!htmlMatcher.find()){
        	return html;
        }
    	if(htmlMatcher.group(0).length() <= 7){
    		return html;
    	}
		return config.getDirHtml()+"/"+config.getCompression()+".min.html";
	}
	
	//压缩CSS资源列表
	private String parseCss(String index) throws IOException{
		Pattern cssRegex = Pattern.compile("css=\\[(.*?)\\]");  
        Matcher cssMatcher = cssRegex.matcher(index);
        
        String css = "";
        
        if(!cssMatcher.find()){
        	return css;
        }
    	if(cssMatcher.group(0).length() <= 6){
    		return css;
    	}
		return config.getDirCss()+"/"+config.getCompression()+".min.css";
	}
	
	public CreateMain(Config conf, Make make, Log log) throws Exception{
		config = conf;
		log.info("  "+config.getIndex());
		File enterPage = new File(config.getInputDir()+config.getIndex());
		if(!enterPage.exists() || !enterPage.isFile()){
			throw new Exception("Lack main file. Can't find enter page : "+config.getInputDir()+config.getIndex());
		}
		//拷贝入口页
		FileUnit.copyJSMinFile(config.getInputDir()+config.getIndex(), config.getTmpDir()+config.getIndex(), false);
		String index = FileUnit.getFileToString(config.getTmpDir()+config.getIndex());
		//修改加载项
		String html = parseHtml(index);
		String css = parseCss(index);
		String run = config.getDirJSMvc()+"/"+config.getRun();
		index = index.replace(run, run.substring(0,run.lastIndexOf(".js"))+".min.js");
		index = index.replaceAll("css=\\[(.*?)\\]", css.equals("")?"css=[];":"css=[\""+css+"\"];");
		index = index.replaceAll("html=\\[(.*?)\\]", html.equals("")?"html=[];":"html=[\""+html+"\"];");
  		String md5 = Md5.getMD5(index);
  		String js = parseJs(index);
		index = index.replaceAll("js=\\[(.*)\\]\\]", md5);
		index = index.replace(md5, js);
		//修改启动文件参数
		String runName = config.getRun().substring(0, config.getRun().lastIndexOf(".js"));
		String r = (config.getDirJSMvc().replace("$", "\\$"))+"\\."+runName+"\\.start\\((.*)\\)";
		String rs = run(index);
		rs = java.util.regex.Matcher.quoteReplacement(rs);
		index = index.replaceAll(r, rs);
		//重新生成入口文件
  		FileUnit.writeStringToFile(index, config.getTmpDir()+config.getIndex(), false);
  		//打包资源文件
  		make.setLoadCheck(config.getInputDir()+config.getIndex());
  		
  		log.info("  "+config.getDirJSMvc());
		make.getJs().pack(config.getDirJSMvc());
		log.info("  "+config.getDirLibrary());
		make.getJs().pack(config.getDirLibrary());
		log.info("  "+config.getDirJs());
		make.getJs().pack(config.getDirJs());
		
		log.info("  "+config.getDirImages());
		make.getImages().pack();
		
		log.info("  "+config.getDirCss());
		make.getCss().pack();
		
		log.info("  "+config.getDirHtml());
		make.getHtml().pack();
	}
	
	
}
