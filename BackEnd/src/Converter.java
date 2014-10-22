import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Converter {
    public static void main(String[] args) throws FileNotFoundException {
    	FileInputStream fis = new FileInputStream(args[0]);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
    	FileOutputStream fos = new FileOutputStream(args[0] + "_o");
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	
 	    String line = null;
 	    int count = 0;
 	    try {
 			while((line = br.readLine()) != null) {
 				if(line.equals(" ") || line.isEmpty() || line.equals("")) {
					continue;
				}
 				
 				String[] strArray = line.split("\t");
				String user_id = strArray[0];
				String tweet_time = strArray[1];
				String id_score_text = strArray[2];
				
				String[] strArray2 = id_score_text.split(":");
				String tweet_id = strArray2[0];
				String score = strArray2[1];
				String text = strArray2[2];
 				
				String content = user_id + "\t" + tweet_time + "\t" + tweet_id + 
						"\t" + score + "\t" + HexStringConverter.getInstance().hexToString(text);
				
				bw.write(content);
 				bw.newLine();
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
