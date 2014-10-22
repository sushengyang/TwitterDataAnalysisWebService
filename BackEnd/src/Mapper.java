import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.charset.Charset;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.UnsupportedEncodingException;


public class Mapper {
	public static void main(String[] args) throws UnsupportedEncodingException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    String line = null;
	    JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory(){
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}       
		};
    	
    	try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty() || line.equals("")) {
					continue;
				}
				
				Map tweet = (Map)parser.parse(line, containerFactory);
				
				Map user = (Map)tweet.get("user");
				String user_id = (String)user.get("id_str");

				String time = (String)tweet.get("created_at");
				String tweet_time = transform(time);
				
				String tweet_id = (String)tweet.get("id_str");
				String text = (String)tweet.get("text"); 
				String rep = text.replace("\r\n", ";").replace("\n", ";").replace("\r", ";");
				byte[] array = rep.getBytes("UTF-8");
				String repText = new String(array, Charset.forName("UTF-8"));
				System.out.println(user_id + "\t" + tweet_time + "\t" + tweet_id + "\t" + repText);
				
				//System.out.println(user_id + "\t" + tweet_time + "\t" + tweet_id + "\t" + 
      			//	HexStringConverter.getInstance().stringToHex(repText));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static Map<String, String> map = new HashMap<String, String>(); 
	static {
		map.put("Jan", "01");
		map.put("Feb", "02");
		map.put("Mar", "03");
		map.put("Apr", "04");
		map.put("May", "05");
		map.put("June", "06");
		map.put("July", "07");
		map.put("Aug", "08");
		map.put("Sept", "09");
		map.put("Oct", "10");
		map.put("Nov", "11");
		map.put("Dec", "12");
	}
	
	private static String transform(String str) {
		String[] strArray = str.split(" ");
		
		String month = map.get(strArray[1]);
		String day = strArray[2];
		String time = strArray[3];
		String year = strArray[5];
				
		return year + "-" + month + "-" + day + "+" + time;
	}
}
