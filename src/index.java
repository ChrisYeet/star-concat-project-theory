//Christopher Yee
//CFLs closed under star concat
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;

public class index {
  public index() {}
  
  public static void main(String[] args) throws Exception {
    
    // parsing file
    Object obj = new JSONParser().parse(new FileReader(args[0]));
    // typecasting
    JSONObject jo = (JSONObject) obj;
    
    // testing
    String st = String.valueOf(jo);
    System.out.println(st);
    
    // get all my strings / arrays of strings / grammars / everything
    try {
      JSONArray var = (JSONArray) jo.get("variables");
      JSONArray alph = (JSONArray) jo.get("alphabet");
      String start = (String) jo.get("start_symbol");
      String old_start = start;
      JSONObject grammar = (JSONObject) jo.get("grammar");
    
    // testing to see if I have everything aka nothing is null / empty
      if (var == null || alph == null || (start == null || start.isEmpty()) || grammar == null) {
        System.out.println("Something is empty in the state of your tuple");
        System.exit(1);
      }
      
      // validates grammar rules
      validateGrammar(grammar, var, alph);
      
      // checks if start_symbol + ' exists, if it does do some other start_symbol
      start = check(var, start);
      
      var.add(start);
      jo.put("start_symbol", start);
      grammar.put(start, Arrays.asList(old_start + start, "ε"));
      
      // testing (won't work for the array as it prints the array's memory reference)
      /*System.out.println(jo.toJSONString());*/
    
    } catch (Exception e) {
      System.out.println("You did something wrong with the setup of your tuple");
      System.exit(1);
    }
    
    try {
      FileWriter file = new FileWriter("output-" + args[0]);
      file.write(printHashMap(jo.toJSONString()));
      file.flush();
      file.close();
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  // Checks to see if new start variable will work ('), if not keep adding "'" to the end
  public static String check(JSONArray variable, String start) {
    if(variable.contains(start + "'")) {
      start = start + "'";
      check(variable, start);
    } else {
      start = start + "'";
    }
    
    return start;
  }
  
  // Validates that each rule only contains variables and alphabet symbols
  public static void validateGrammar(JSONObject grammar, JSONArray variables, JSONArray alphabet) throws Exception {
    // Combine variables and alphabet into a set for quick lookup
    Set<String> vSymbols = new HashSet<>();
    vSymbols.addAll(variables);
    vSymbols.addAll(alphabet);
    vSymbols.add("ε");
    
    // Iterate over each variable's grammar rules
    for (Object key : grammar.keySet()) {
      String variable = (String) key;
      JSONArray grammars = (JSONArray) grammar.get(variable);
      
      // Check each grammar rule
      for (Object g : grammars) {
        String rule = (String) g;
        for (int i = 0; i < rule.length(); i++) {
          String symbol = String.valueOf(rule.charAt(i));
          if (!vSymbols.contains(symbol)) {
            System.out.println("Invalid grammar rule detected: " + rule);
            throw new Exception();
          }
        }
      }
    }
  }
  
  
  // A helper method to print JSON not on one line - formatting
  public static String printHashMap(String jsonString) {
    StringBuilder prettyJSON = new StringBuilder();
    boolean inQuotes = false;
    boolean isArray = false;
    
    for (char c : jsonString.toCharArray()) {
      switch (c) {
        case '"':
          inQuotes = !inQuotes;
          prettyJSON.append(c);
          break;
        case '{':
          if (!inQuotes) {
            prettyJSON.append(c).append("\n");
          } else {
            prettyJSON.append(c);
          }
          break;
        case '}':
          if (!inQuotes) {
            prettyJSON.append("\n");
            prettyJSON.append(c);
          } else {
            prettyJSON.append(c);
          }
          break;
        case '[':
          if (!inQuotes) {
            isArray = true;  // In array
            prettyJSON.append(c);
          } else {
            prettyJSON.append(c);
          }
          break;
        case ']':
          prettyJSON.append(c);  // End array
          if (!inQuotes && isArray) {
            isArray = false;
          }
          break;
        case ',':
          prettyJSON.append(c);
          if (!inQuotes && !isArray) {
            prettyJSON.append("\n");
          } else if (!inQuotes && isArray) {
            prettyJSON.append(" ");  // Add space array
          }
          break;
        case ':':
          prettyJSON.append(c);
          if (!inQuotes) {
            prettyJSON.append(" ");
          }
          break;
        default:
          prettyJSON.append(c);
          break;
      }
    }
    
    return prettyJSON.toString();
  }
}
