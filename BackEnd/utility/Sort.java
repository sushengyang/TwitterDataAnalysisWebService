import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.TreeMap;


public class Sort {
    public static void main(String args[]) throws IOException {
    	FileInputStream fis = new FileInputStream(args[0]);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
    	
    	FileOutputStream fos = new FileOutputStream(args[0] + "_o");
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	
    	String line = null;
    	String user_id = null;
    	String tweet_time = null;
    	String text = null;
    	
    	TreeMap<String, String> map = new TreeMap<String, String>();
    	
    	try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty()) {
					continue;
				}
				
				String[] strArray = line.split("\t");
				user_id = strArray[0];
				tweet_time = strArray[1]; 
				text = strArray[2];
				map.put(user_id + "\t" + tweet_time, text);
			}
			
			for(Map.Entry<String, String> entry : map.entrySet()) {
			    bw.write(entry.getKey() + "\t" + entry.getValue());
			    bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	bw.close();
    	br.close();
    	fos.close();
    	fis.close();
    }
}
