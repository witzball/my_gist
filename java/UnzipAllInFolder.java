package de.tp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipAllInFolder {

	public static void main(String[] args) {
		UnzipAllInFolder uaif = new UnzipAllInFolder();
		uaif.go(args[0], args[1]);
		System.out.println("Finished.");
	}

	public void go(String strZipPath, String strDestinationPath) {
		try (Stream<Path> walk = Files.walk(Paths.get(strZipPath))) {

			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".zip")).collect(Collectors.toList());
//			result.forEach(System.out::println);
//			result.forEach(UnzipAllInFolder::unzip);
			for (int i = 0; i < result.size(); i++) {
				unzip(strDestinationPath, result.get(i));
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void unzip(String strDestinationPath, String strFilename) {
		ZipInputStream zis = null;
		ZipEntry ze = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
		File newFile = null;
//		File newFile = null;
		String fileName = null;
//		String fileName = null;
        int len;
		byte[] buffer = new byte[1024];

        File dir = new File(strDestinationPath);

		// create output directory if it doesn't exist
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// buffer for read and write data to file
		try {
			fis = new FileInputStream(strFilename);
			zis = new ZipInputStream(fis);
			ze = zis.getNextEntry();
			while(ze != null){
				fileName = ze.getName();
				newFile = new File(strDestinationPath + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());

				//create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();

                fos = new FileOutputStream(newFile);
                while ((len = zis.read(buffer)) > 0) {
                	fos.write(buffer, 0, len);
                }
//                fos.close();

                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	try {
    			//close last ZipEntry
                fos.close();
			} catch (Exception e2) {
				// do nothing
			}
        	try {
        		//close last ZipEntry
        		zis.closeEntry();
        	} catch (Exception e2) {
        		// do nothing
        	}
        	try {
        		//close last ZipEntry
        		zis.close();
        	} catch (Exception e2) {
        		// do nothing
        	}
        	try {
        		//close last ZipEntry
        		fis.close();
        	} catch (Exception e2) {
        		// do nothing
        	}
        }
	}
}
