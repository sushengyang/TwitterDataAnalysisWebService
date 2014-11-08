import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author kailianc
 * 
 * A mapper class which print out necessary info(time, location, hashtagText, hashtagIndex).
 *
 */
public class Mapper {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
	    String line = null;
	    JSONParser parser = new JSONParser();
    	
    	try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty() || line.equals("")) {
					continue;
				}
				
				JSONObject tweet = (JSONObject)parser.parse(line);
				
				if(tweet != null) {
					String time = (String)tweet.get("created_at");
					String tweet_time = transform(time);
					String tweet_id = (String)tweet.get("id_str");
					String location = null;
					
					Map place = (Map)tweet.get("place");
					String placeName = null;
					if(place != null) {
						placeName = (String)place.get("name");
					}
					
					if(place == null || placeName == null || placeName.isEmpty()) {
						Map user = (Map)tweet.get("user");
						String timeZone = (String)user.get("time_zone");
						if(timeZone == null || timeZone.matches("\btime\b")) {
							continue;
						}
						location = timeZone;
					} else {
						location = placeName;
					}					
					
					JSONObject entities = (JSONObject)tweet.get("entities");
					if(entities != null) {
						JSONArray hashtags = (JSONArray)entities.get("hashtags");	
						if(hashtags == null) {
							continue;
						}
						
						Map<String,ArrayList<String>> outputMap = new HashMap<String, ArrayList<String>>();
						
						for(int i = 0; i < hashtags.size(); i++) {
							JSONObject obj = (JSONObject)hashtags.get(i);
							String hashtagText = (String)obj.get("text");
							Object indices = (Object)obj.get("indices");
							JSONArray ind = (JSONArray)indices;
							int index = (int) ind.get(0);
							String output = tweet_time + "\t" + location + "\t" + hashtagText + "\t" + index + "\t" + tweet_id;
							
							ArrayList<String> outputList = new ArrayList<String>();
			                outputList.add(output);
			                outputList.add(String.valueOf(index));
			                if(!outputMap.containsKey(hashtagText)) {
			                    outputMap.put(hashtagText, outputList);
			                } else {  
			                    if(index < Integer.parseInt(outputMap.get(hashtagText).get(1))){
			                        outputMap.put(hashtagText, outputList);
			                    }
			                }
					    }	
						
						List<Map.Entry<String, ArrayList<String>>> listMap
	                    		= new ArrayList<Map.Entry<String,ArrayList<String>>>(outputMap.entrySet());
			            Iterator iter = listMap.iterator();
			            while (iter.hasNext()) {
			                Map.Entry entry = (Map.Entry) iter.next();
			                ArrayList<String> list = (ArrayList<String>)entry.getValue();
			                System.out.println(list.get(0));
			            }
					}
				}
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
		String year = strArray[5];
				
		return year + "-" + month + "-" + day;
	}
}
