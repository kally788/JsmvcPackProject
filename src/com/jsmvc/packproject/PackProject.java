package com.jsmvc.packproject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.jsmvc.packproject.make.Make;
import com.jsmvc.utils.FileUnit;
import com.jsmvc.utils.Log;
import com.jsmvc.utils.Zip;


public class PackProject {
	
	private Config config;
	private Log log;
	
	private void startup(){
		boolean err = false;
		try{
			log.info("Create directory start ...");
			new CreateDirectory(config, log);
			log.info("Create directory end ...");
			
			log.info("Make start...");
			Make make = new Make(config);
			new CreateMain(config, make, log);
			new CreateExtend(config, make, log);
			log.info("Make end.");
			
			
			log.info("Clear empty directory ...");
			FileUnit.clearDir(config.getTmpDir());
			
			log.info("Create output start ...");
			File outDir = new File(config.getOutputDir());
			if(!outDir.exists()){
				outDir.mkdirs();
			}
			String outFile = config.getOutputDir()+config.getName()+config.getVersion();
			//检查文件是否已经存在并判断是否要覆盖
			File target = new File(outFile+(config.getIsZip()?".zip":""));
			if(target.exists() && ((config.getIsZip() && target.isFile()) || (!config.getIsZip() && target.isDirectory()))){
				log.warning("  Target exist:"+target.getCanonicalPath());
				if(config.getIsOverride()){
					log.info("  Override.");
					if(!config.getIsZip()){
						FileUnit.deleteFile(target.getPath());
					}
				}else{
					log.info("  Clear temp file."); 
					FileUnit.deleteFile(config.getTmpDir());
					log.warning("Pack fail, target exist!");
					return;
				}
			}
			if(config.getIsZip()){
				Zip.zipFiles(config.getTmpDir(), outFile+".zip");
				log.info("  Output target:"+new File(outFile+".zip").getCanonicalPath()); 
			}else{
				new FileUnit().copyFiles(config.getTmpDir(), outFile);
				log.info("  Output target:"+new File(outFile).getCanonicalPath()); 
			}
			log.info("Create output end.");
			log.info("Complete!"); 
		}catch(Exception e){
			System.gc();
			StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        log.error("Fatal error!!!!"); 
        	log.error("Error info:");
        	log.error(sw.toString());
        	err = true;
		}finally{
			FileUnit.deleteFile(config.getTmpDir());
			try {
        		log.save(err);
			} catch (Exception e1) {
				log.error("Write log fail!");
				e1.printStackTrace();
			}
		}
	}
	
	private PackProject(Config conf, Log l){
		config = conf;
		log = l;
		startup();
	}
	
	private PackProject(Config conf){
		config = conf;
		log = new Log(config.getLogDir(), config.getLogMax(), config.getLogLevel());
		startup();
	}
	
	private PackProject() throws IOException{
		config = new Config();
		log = new Log(config.getLogDir(), config.getLogMax(), config.getLogLevel());
		startup();
	}
	
	/**
	 * 开始打包
	 * @param conf 指定一个自定义的配置文件
	 * @param l 日志对象
	 */
	public static void make(Config conf, Log l){
		new PackProject(conf, l);
	}
	public static void make(Config conf){
		new PackProject(conf);
	}
	public static void make() throws IOException{
		new PackProject();
	}
	
	/**
	 * 第一个参数为打包版本号
	 * 第二个参数为源码目录
	 * 第三个参数为输出目录
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Config conf = new Config();
		if(args.length > 0 && !args[0].equals("")){
			conf.setVersion(args[0]);//项目版本
		}
		if(args.length > 1 && !args[1].equals("")){
			conf.setName(args[1]);//项目文件名
		}
		if(args.length > 2 && !args[2].equals("")){
			conf.setInputDir(args[1]);//项目目录
		}
		if(args.length > 3 && !args[3].equals("")){
			conf.setOutputDir(args[2]);//输出目录
		}
		PackProject.make(conf);
		
	}
}
