import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Reducer {
	private static Map<String, Integer> AFINN_TBL = new HashMap<String, Integer>();
	private static Set<String> BANNED_TBL = new HashSet<String>();
	
	static {
		try {
			AFINN_TBL = createAfinnDict("AFINN.txt");
			BANNED_TBL = createBannedDict("banned.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
	
	 
	public static void main(String[] args) throws UnsupportedEncodingException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    	String line = null;
    	
		try {
			while((line = br.readLine()) != null) {
				if(line.equals(" ") || line.isEmpty()) {
					continue;
				}
				
				String[] strArray = line.split("\t");
				String user_id = strArray[0];
				String tweet_time = strArray[1];
				String tweet_id = strArray[2];
				String text = strArray[3];
				int score = calcSentScore(text, AFINN_TBL);
				String censoredText = censorText(text, BANNED_TBL);
				byte[] array = censoredText.getBytes("UTF-8");
				String repText = new String(array, Charset.forName("UTF-8"));
				System.out.println(user_id + "\t" + tweet_time + "\t" + tweet_id + 
						"\t" + String.valueOf(score) + "\t" + repText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
    private static Map<String, Integer> createAfinnDict(String ifName) throws IOException {
    	Map<String, Integer> dict = new HashMap<String, Integer>();
		InputStream fs = Reducer.class.getClassLoader().getResourceAsStream(ifName);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fs));    	
    	
        String line = null;
    	while((line = br.readLine()) != null) {
			if(line == "") {
				continue;
			}
			String[] strArray = line.split("\t");
            dict.put(strArray[0], Integer.parseInt(strArray[1]));
    	}
		
		br.close();
		fs.close();
		
		return dict;
    } 
    
    private static Set<String> createBannedDict(String ifName) throws IOException {
    	Set<String> dict = new HashSet<String>();
		InputStream fs = Reducer.class.getClassLoader().getResourceAsStream(ifName);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fs));    	
    	
        String line = null;
    	while((line = br.readLine()) != null) {
			if(line == "") {
				continue;
			}
			String str = decodeROT13(line);
            dict.add(str);
    	}
		
		br.close();
		fs.close();
		
		return dict;
    }
    
    private static String decodeROT13(String word) {
    	StringBuilder str = new StringBuilder();
    	for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            str.append(c);
        }
    	return str.toString();
    }

    private static int calcSentScore(String text, Map<String, Integer> afinnDict) {
    	if(text == null) {
    		System.out.println("Text is null");
    		return -1;
    	} 
    	
    	if(afinnDict == null) {
    		System.out.println("Afinn Dictionary is null");
    		return -1;
    	}
    	
    	int score = 0;
    	String text2lower = text.toLowerCase();
    	StringBuilder word = new StringBuilder();
    	for(int i = 0; i < text2lower.length(); i++) {
    		char c = text2lower.charAt(i);
    		if(Character.isAlphabetic(c) || Character.isDigit(c)) {
    			word.append(c);
    		} else {
    			if(word.length() != 0) {
    				if(afinnDict.containsKey(word.toString())) {
    					score += afinnDict.get(word.toString());
    				}
    				word.delete(0, word.length());
    			}
    		}
    	}
    	
    	if(word.length() != 0) {
			if(afinnDict.containsKey(word)) {
				score += afinnDict.get(word.toString());
			}
			word.delete(0, word.length());
		}
    	
    	return score;
    }
    
    private static String censorText(String text, Set<String> bannedDict) {
    	if(text == null) {
    		System.out.println("Text is null");
    		return null;
    	} 
    	
    	if(bannedDict == null) {
    		System.out.println("Banned Dictionary is null");
    		return null;
    	}
    	
    	StringBuilder censoredText = new StringBuilder();
    	StringBuilder word = new StringBuilder();
    	for(int i = 0; i < text.length(); i++) {
    		char c = text.charAt(i);
    		if(Character.isAlphabetic(c) || Character.isDigit(c)) {
    			word.append(c);
    			continue;
    		} 
    		
			if(word.length() != 0) {
				if(bannedDict.contains(word.toString().toLowerCase())) {
					StringBuilder str = new StringBuilder();
					for(int j = 1; j < word.length() - 1; j ++) {
						str.append("*");
					}
					censoredText.append(word.replace(1, word.length() - 1, str.toString()));
	    		} else {
	    			censoredText.append(word);
	    		}
				word.delete(0, word.length());
			}
			censoredText.append(c);			
    	}
    	
    	if(word.length() != 0) {
    		if(bannedDict.contains(word.toString().toLowerCase())) {
    			StringBuilder str = new StringBuilder();
				for(int j = 1; j < word.length() - 1; j ++) {
					str.append("*");
				}
				censoredText.append(word.replace(1, word.length() - 1, str.toString()));
    		} else {
    			censoredText.append(word);
    		}
			word.delete(0, word.length());
    	}
    	
    	return censoredText.toString();
    }
    
}
