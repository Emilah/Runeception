package org.runeception.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * Decompressor Utility
 * @author Emily Perkins (emilah@live.com)
 * @since 3/12/2012 6:22PM
 * @version 1.0.0
 *
 */
public class Decompressor {
	/**
	 * Decompresses Zip Archive into individual Entries
	 * @param fileLocation File Location of Archive
	 * @param outDirectory Output Location of Entries 
	 * @param deleteArchive Delete Zip File on Finish
	 */
	 public void decompress(String fileLocation, String outDirectory, boolean deleteArchive) {
         try {
        	 File cache = new File(fileLocation);
             ZipInputStream input = new ZipInputStream(new BufferedInputStream(new FileInputStream(cache)));
             ZipEntry entry;
             FileOutputStream output;
             while((entry = input.getNextEntry()) != null) {
            	 output =  new FileOutputStream(outDirectory + entry.getName());
                 byte[] buffer = new byte[1024];
                 int length = 0;
                 while((length = input.read(buffer)) != -1) {
                	 output.write(buffer, 0, length);
                 }
                 output.close();
             }
             input.close();
             if(deleteArchive)
            	 cache.delete();
         } catch(Exception e) {
             e.printStackTrace();
         }
     }
	 /**
	  * Decompresses Zip Archive into individual Entries
	  * @param fileLocation File Location of Archive
	  * @param outDirectory Output Location of Entries 
	  */
	 public void decompress(String fileLocation, String outDirectory) {
		 decompress(fileLocation, outDirectory, false);
	 }
	 

}
