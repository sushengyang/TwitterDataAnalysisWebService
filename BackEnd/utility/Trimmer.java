import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Trimmer {
    public static void main(String[] args) throws FileNotFoundException {
    	FileInputStream fis = new FileInputStream(args[0]);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
    	
    	FileOutputStream fos = new FileOutputStream(args[0] + "_o");
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	
 	    String line = null;
 	    int count = 0;
 	    try {
 			while((line = br.readLine()) != null) {
			    count++;
				if(count > Integer.parseInt(args[2])) {
 				    break;    
 				}
                if(count >= Integer.parseInt(args[1]) && count <= Integer.parseInt(args[2])) {
				    bw.write(line);
 				    bw.newLine();
				} 				
 			}
 			
 			bw.close();
 			fos.close();
 			br.close();
 			fis.close();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
    }
}
