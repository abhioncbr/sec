package com.sec.filing.analysis.spark;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsFileRead {
	
	public static void main(String args[]) throws IOException, URISyntaxException{
		Configuration configuration = new Configuration();
		configuration.set("fs.defaultFS", "hdfs://localhost:54310/");
		configuration.addResource(new Path("/home/abhishek/BigData/hadoop-2.6.4/conf/core-site.xml"));
		configuration.addResource(new Path("/home/abhishek/BigData/hadoop-2.6.4/conf/hdfs-site.xml"));
		
		FileSystem hdfs = FileSystem.get(configuration);
		//Path file = new Path("/home/abhishek/Desktop/sec/DowJones/DowJones35_Raw/0000012927_BA_2_8_2010.txt");
		Path destFilePath = new Path("hdfs://localhost:54310/sec/DowJones/DowJones35_Raw/0000012927_BA_2_8_2010.txt");
		
		getflSize(hdfs, destFilePath);
		//hdfs.copyFromLocalFile(file, destFilePath);
		
		/*if ( hdfs.exists( file )) { 
			BufferedReader br=new BufferedReader(new InputStreamReader(hdfs.open(file)));
            String line;
            line=br.readLine();
            while (line != null){
                    System.out.println(line);
                    line=br.readLine();
            }
		}  else {
			OutputStream os = hdfs.create(file);
			BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
			br.write("Hello World");
			br.close();
		}	*/
		hdfs.close();
	}
	
	public static long getflSize(FileSystem hdfs, Path destFilePath) throws IOException {
        ContentSummary cSummary = hdfs.getContentSummary(destFilePath);
        long length = cSummary.getLength();
        return length;
    }
}
