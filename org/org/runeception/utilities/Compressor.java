package org.runeception.utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compressor {
	
	public void compress(String outputName, LinkedHashMap<Integer, File> files) {
		try {
			FileOutputStream destination = new FileOutputStream(outputName);
			ZipOutputStream output = new ZipOutputStream(new BufferedOutputStream(destination));
			FileInputStream input;
			ZipEntry[] entry = new ZipEntry[files.size()];
			for (int fileIndex = 0; fileIndex < files.size(); fileIndex++) {
				input = new FileInputStream(files.get(fileIndex));
				entry[fileIndex] = new ZipEntry(files.get(fileIndex).getName());
				byte[] dataBuffer = new byte[(int) files.get(fileIndex).length()];
				output.putNextEntry(entry[fileIndex]);
				int length;
				while ((length = input.read(dataBuffer, 0, 2048)) != -1) {
					output.write(dataBuffer, 0, length);
				}
			}
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void compress(String outputName, File file) {
		compress(outputName, file);
	}

}
