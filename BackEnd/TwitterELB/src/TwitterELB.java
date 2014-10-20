import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TwitterELB {
	public static void main(String[] args) throws IOException, ParseException {
    	Map<String, Integer> AFINN_TBL = createAfinnDict("AFINN.txt");
    	Set<String> BANNED_TBL = createBannedDict("banned.txt");
    	
    	FileInputStream fis = new FileInputStream("C:/Users/bbfee/Desktop/VMWareShare/15619f14twitter-parta-aa_o");
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
    	
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
    	
    	while((line = br.readLine()) != null) {
			if(line.equals(" ") || line.isEmpty()) {
				continue;
			}
			
			Map json = (Map)parser.parse(line, containerFactory);			
			Long id = (Long)json.get("id");
			String id_str = (String)json.get("id_str");
			String text = (String)json.get("text");
			
//			text = "adbdfst&indoctrinating=-2, postponing'abductions%&'('(')SHIT~SD=FUCK|||fuck??fingerfuck//pussy\\15619cctest^^^==coksucka";
			
			int score = calcSentScore(text, AFINN_TBL);
			String censoredText = censorText(text, BANNED_TBL);
			
			System.out.println("Original Text = " + text);
			System.out.println("Censored Text = " + censoredText);
			
//			assert(text.equals(censoredText));
    	}
		
		br.close();
		fis.close();
    }
    
    private static Map<String, Integer> createAfinnDict(String ifName) throws IOException {
    	Map<String, Integer> dict = new HashMap<String, Integer>();
    	FileInputStream fis = new FileInputStream(ifName);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
    	
        String line = null;
    	while((line = br.readLine()) != null) {
			if(line == "") {
				continue;
			}
			String[] strArray = line.split("\t");
            dict.put(strArray[0], Integer.parseInt(strArray[1]));
    	}
		
		br.close();
		fis.close();
		
		return dict;
    } 
    
    private static Set<String> createBannedDict(String ifName) throws IOException {
    	Set<String> dict = new HashSet<String>();
    	FileInputStream fis = new FileInputStream(ifName);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));    	
    	
        String line = null;
    	while((line = br.readLine()) != null) {
			if(line == "") {
				continue;
			}
			String str = decodeROT13(line);
            dict.add(str);
    	}
		
		br.close();
		fis.close();
		
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
