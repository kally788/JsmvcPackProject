package com.jsmvc.packproject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.jsmvc.utils.Md5;
import com.jsmvc.utils.StringUnit;

/**   
 * @title: 目录配置，优先读取 conf.txt 文件中的配置
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:41:50 
 *
 */
public class Config {
	
	//设置项目目录
	private String inputDir = "./input/";
	//设置输出文件位置录
	private String outputDir = "./output/";
	//设置打包文件名称
	private String name = "release";
	//默认打包版本号
	private String version = "";
	
	//是否压缩
	private boolean isZip = false;
	//是否要覆盖
	private boolean isOverride = true;
	
	//日志目录
	private String logDir = "./log/";
	//日志级别，<=0信息+警告+错误，=1警告+错误，=2错误，>=3关闭日志
	private int logLevel = 0;
	//单个日志文件大小,字节
	private int logMax = 2048000;
		
	//一级目录定义，注意前后不能带/
	private String dirCss = "css";
	private String dirJs = "js";
	private String dirHtml = "html";
	private String dirImages = "images";
	private String dirJSMvc = "$jsmvc$";
	private String dirLibrary = "$library$";
	//扩展包目录定义，注意前后不能带/
	private String dirExt = "ext";
	private String dirExtCss = "css";
	private String dirExtJs = "js";
	private String dirExtHtml = "html";
	private String dirExtImages = "images";
	
	//启动脚本
	private String run = "run.js";
	//入口页
	private String index = "index.html";
	//扩展包入口文件
	private String ext = "package.js";
	//压缩后的文件名
	private String compression = "wrap";
	//临时目录，打包时会在打包程序根目录下创建该目录
	private String tmpDir = Md5.getMD5(java.util.UUID.randomUUID().toString())+"/";
	
	//修正配置，确保配置项的正确性
	private void correctConfig(){
		inputDir = StringUnit.addBar(inputDir);
		outputDir = StringUnit.addBar(outputDir);
		logDir = StringUnit.addBar(logDir);
		
		dirCss = StringUnit.trimBothBar(dirCss);
		dirJs = StringUnit.trimBothBar(dirJs);
		dirHtml = StringUnit.trimBothBar(dirHtml);
		dirImages = StringUnit.trimBothBar(dirImages);
		dirJSMvc = StringUnit.trimBothBar(dirJSMvc);
		dirLibrary = StringUnit.trimBothBar(dirLibrary);
		
		dirExt = StringUnit.trimBothBar(dirExt);
		dirExtCss = StringUnit.trimBothBar(dirExtCss);
		dirExtJs = StringUnit.trimBothBar(dirExtJs);
		dirExtHtml = StringUnit.trimBothBar(dirExtHtml);
		dirExtImages = StringUnit.trimBothBar(dirExtImages);
	}
	//读取配置文件
	private void readConfig(String configPath) throws IOException{
		List<String> conf = Files.readAllLines(Paths.get(configPath), StandardCharsets.UTF_8);
		for(int i=0;i<conf.size();i++){
			String line = conf.get(i);
			if(line.length() < 1){
				continue;
			}
			if(line.replaceAll("\\s*", "").substring(0,1).equals("#")){
				continue;
			}
			String[] lineItem = line.split("=");
			if(lineItem.length < 2){
				continue;
			}
			String v = line.substring(lineItem[0].length()+1,line.length()).trim();
			if(v.equals("") && !lineItem[0].trim().equals("version")){
				continue;
			}
			switch(lineItem[0].trim()){
				case "inputDir":
					inputDir = v;
					break;
				case "outputDir":
					outputDir = v;
					break;
				case "name":
					name = v;
					break;
				case "isZip":
					isZip = v.equals("true")?true:false;
					break;
				case "isOverride":
					isOverride = v.equals("true")?true:false;
					break;
				case "dirCss":
					dirCss = v;
					break;
				case "dirJs":
					dirJs = v;
					break;
				case "dirHtml":
					dirHtml = v;
					break;
				case "dirImages":
					dirImages = v;
					break;
				case "dirJSMvc":
					dirJSMvc = v;
					break;
				case "dirLibrary":
					dirLibrary = v;
					break;
				case "dirExt":
					dirExt = v;
					break;
				case "dirExtCss":
					dirExtCss = v;
					break;
				case "dirExtJs":
					dirExtJs = v;
					break;
				case "dirExtHtml":
					dirExtHtml = v;
					break;
				case "dirExtImages":
					dirExtImages = v;
					break;
				case "logDir":
					logDir = v;
					break;
				case "logLevel":
					try{
						logLevel = Integer.parseInt(v);
					} catch (Exception e) {
					}
					break;
				case "logMax":
					try{
						logMax = Integer.parseInt(v);
					} catch (Exception e) {
					}
					break;
				case "run":
					run = v;
					break;
				case "index":
					index = v;
					break;
				case "ext":
					ext = v;
					break;
				case "compression":
					compression = v;
					break;
				case "version":
					version = v;
					break;
			}
		}
	}

	/**
	 * 创建一个配置文件
	 * @param configPath 指定配置文件路径
	 * @throws IOException
	 */
	public Config(String configPath) throws IOException{
		File configFile = new File(configPath);
		if(!configFile.exists()||!configFile.isFile()){
			throw new IOException("Read custom config error:" + configPath);
		}else{
			readConfig(configPath);
			correctConfig();
		}
	}
	
	/**
	 * 创建一个配置文件，如果默认根目录下有config.conf文件，即读取，否则采用程序默认值
	 * @throws IOException
	 */
	public Config() throws IOException{
		File configFile = new File("config.conf");
		if(configFile.exists() && configFile.isFile()){
			readConfig("config.conf");
		}
		correctConfig();
	}
	
	//可写属性
	
	public void setInputDir(String v){
		inputDir = StringUnit.addBar(v);
	}
	
	public void setOutputDir(String v){
		outputDir = StringUnit.addBar(v);
	}
	
	public void setName(String v){
		if(!v.equals("")){
			name = v;
		}
	}
	
	public void setVersion(String v){
		version = v;
	}

	//可读属性
	
	public String getInputDir() {
		return inputDir;
	}

	public String getOutputDir() {
		return outputDir;
	}
	
	public String getTmpDir() {
		return tmpDir;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getIsZip() {
		return isZip;
	}
	
	public boolean getIsOverride() {
		return isOverride;
	}
	
	public String getDirCss() {
		return dirCss;
	}

	public String getDirJs() {
		return dirJs;
	}

	public String getDirHtml() {
		return dirHtml;
	}

	public String getDirImages() {
		return dirImages;
	}

	public String getDirJSMvc() {
		return dirJSMvc;
	}

	public String getDirLibrary() {
		return dirLibrary;
	}

	public String getDirExt() {
		return dirExt;
	}

	public String getDirExtCss() {
		return dirExtCss;
	}

	public String getDirExtJs() {
		return dirExtJs;
	}

	public String getDirExtHtml() {
		return dirExtHtml;
	}

	public String getDirExtImages() {
		return dirExtImages;
	}
	
	public String getLogDir() {
		return logDir;
	}
	
	public int getLogLevel() {
		return logLevel;
	}
	
	public int getLogMax() {
		return logMax;
	}
	
	public String getRun() {
		return run;
	}

	public String getIndex() {
		return index;
	}

	public String getExt() {
		return ext;
	}

	public String getCompression() {
		return compression;
	}
	
	public String getVersion(){
		return version;
	}
	
}
