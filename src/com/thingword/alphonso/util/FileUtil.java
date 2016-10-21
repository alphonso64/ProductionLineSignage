package com.thingword.alphonso.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

	public static void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
//	    path.delete();  
	}  
	
	public static List<File> getXlsFileList(String parent){
		List<File> ls = new ArrayList<>();
		File root = new File(parent);
		File[] files = root.listFiles();
		for(File file:files){
			if(file.getName().toLowerCase().endsWith(".xls") && file.isFile()){
				ls.add(file);
			}
		}
		return ls;
	}
	
}
