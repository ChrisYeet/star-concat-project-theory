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
    JSONArray var = (JSONArray) jo.get("variables");
    JSONArray alph = (JSONArray) jo.get("alphabet");
    String start = (String) jo.get("start_symbol");
    JSONObject grammar = (JSONObject) jo.get("grammar");
    
    // testing
    System.out.println(var);
    System.out.println(alph);
    System.out.println(start);
    System.out.println(grammar);
    
    System.out.println(jo.toJSONString());
    
  }
  
}
