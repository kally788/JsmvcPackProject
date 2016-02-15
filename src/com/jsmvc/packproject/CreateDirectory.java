package com.jsmvc.packproject;

import java.io.File;
import java.io.IOException;

import com.jsmvc.utils.FileUnit;
import com.jsmvc.utils.Log;

/**   
 * @title: 创建项目的目录结构
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:47:10 
 *
 */
public class CreateDirectory {
	
	public CreateDirectory(Config config, Log log) throws IOException{

		log.info("  Main directory");
		FileUnit.createDir(config.getTmpDir());
		File dirJSMvc = new File(config.getInputDir()+config.getDirJSMvc());
		if(dirJSMvc.exists()){ 
			FileUnit.createDir(config.getTmpDir()+config.getDirJSMvc());
		}
		File dirLibrary = new File(config.getInputDir()+config.getDirLibrary());
		if(dirLibrary.exists()){ 
			FileUnit.createDir(config.getTmpDir()+config.getDirLibrary());
		}
		File dirJs = new File(config.getInputDir()+config.getDirJs());
		if(dirJs.exists()){ 
			FileUnit.createDir(config.getTmpDir()+config.getDirJs());
		}
		File dirCss = new File(config.getInputDir()+config.getDirCss());
		if(dirCss.exists()){ 
			FileUnit.createDir(config.getTmpDir()+config.getDirCss());
		}
		File dirHtml = new File(config.getInputDir()+config.getDirHtml());
		if(dirHtml.exists()){ 
			FileUnit.createDir(config.getTmpDir()+config.getDirHtml());
		}
		File dirImages = new File(config.getInputDir()+config.getDirImages());
		if(dirImages.exists()){ 
			FileUnit.createDir(config.getTmpDir()+config.getDirImages());
		}
		
		log.info("  Extend directory");
		File ext = new File(config.getInputDir()+config.getDirExt());
    	if(!ext.exists()){ 
    		return;
    	}
    	FileUnit.createDir(config.getTmpDir()+config.getDirExt());
    	File[] extList = ext.listFiles();
		for (int i = 0; i < extList.length; i++) {
			File extModel = extList[i];
			
			String outRoute = config.getTmpDir()+config.getDirExt()+"/"+extModel.getName();
			String sourceRoute = config.getInputDir()+config.getDirExt()+"/"+extModel.getName();
			
			FileUnit.createDir(outRoute);
			
			File dirExtJs = new File(sourceRoute+"/"+config.getDirExtJs());
			if(dirExtJs.exists()){ 
				FileUnit.createDir(outRoute+"/"+config.getDirExtJs());
			}
			File dirExtCss = new File(sourceRoute+"/"+config.getDirExtCss());
			if(dirExtCss.exists()){ 
				FileUnit.createDir(outRoute+"/"+config.getDirExtCss());
			}
			File dirExtHtml = new File(sourceRoute+"/"+config.getDirExtHtml());
			if(dirExtHtml.exists()){ 
				FileUnit.createDir(outRoute+"/"+config.getDirExtHtml());
			}
			File dirExtImages = new File(sourceRoute+"/"+config.getDirExtImages());
			if(dirExtImages.exists()){ 
				FileUnit.createDir(outRoute+"/"+config.getDirExtImages());
			}
		}
	}
}
