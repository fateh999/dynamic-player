package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


class StartPlayer{

	public static void main(String[] args) throws IOException{
	
		//String filePath = new File("").getAbsolutePath();
		
		String path = System.getProperty("user.home")+"\\Documents\\";
		File fp = new File(path+"theme.txt");
		if(fp.exists()){
		//BufferedReader in = new BufferedReader(new FileReader(filePath + "/src/application/theme.txt"));
		BufferedReader in = new BufferedReader(new FileReader(fp.getAbsolutePath()));
		String line;		
		while((line = in.readLine()) != null)
		{
		if(line.equals("Graphite")){
			Graphite.main(null);	
			}
		if(line.equals("Colorful")){
			Colorful.main(null);
			}
		if(line.equals("Metal")){
			Metal.main(null);
		}
		}
		in.close();
		}
		else
		{
			Colorful.main(null);
		}
	}
}