import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Merger {
    public static void main(String[] args) throws IOException {
    	FileOutputStream fos = new FileOutputStream("result");
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	
    	for(String str : args) {
	    	FileInputStream fis = new FileInputStream(str);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
	    	
	 	    String line = null;
 	    	while((line = br.readLine()) != null) {
 				bw.write(line);
 				bw.newLine();	
 			}
 			
 			br.close();
 			fis.close();
    	}
    	bw.close();
		fos.close();
    }
}
