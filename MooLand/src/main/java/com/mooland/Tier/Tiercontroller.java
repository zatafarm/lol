package com.mooland.Tier;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Tiercontroller {
	@Autowired
	private TierService ts;
	
	@GetMapping(value = "/tier")
	public String TierList(Model model, @RequestParam(name = "position",required = false) String position, @RequestParam(name = "role",required = false) String role) throws Exception {
	    Pattern pattern = Pattern.compile("([^#]+)#([^#]+)");
	    
	    if (position == null) {
	    	return "view/tier";
        }
	    if(role.equals("all")) {
		    List<TierDTO> tierlist = ts.getlist(position);
		    
			   
		    for(int a = 0 ; a<tierlist.size() ; a++ ) {	
		    	 Matcher matcher = pattern.matcher(tierlist.get(a).getLOLNickName());
	             if (matcher.find()) {
	                tierlist.get(a).setLOLNickName1(matcher.group(1));
	                tierlist.get(a).setLOLNickName2(matcher.group(2));
	             }
	             String bjurl = tierlist.get(a).getBJID();
		    	tierlist.get(a).setImgUrl("/bjimg/" + bjurl+ ".jpg");
			    
			    model.addAttribute("tierList", tierlist);
			    model.addAttribute("position", position);
		    }
	    }
	    if(role.equals("main")) {

	    List<TierDTO> tierlist = ts.getlist(position);
	    
	   
	    for(int a = 0 ; a<tierlist.size() ; a++ ) {	
	    	 Matcher matcher = pattern.matcher(tierlist.get(a).getLOLNickName());
             if (matcher.find()) {
                tierlist.get(a).setLOLNickName1(matcher.group(1));
                tierlist.get(a).setLOLNickName2(matcher.group(2));
             }
             String bjurl = tierlist.get(a).getBJID();
	    	tierlist.get(a).setImgUrl("/bjimg/" + bjurl+ ".jpg");
		    
		    model.addAttribute("tierList", tierlist);
		    model.addAttribute("position", position);
	    }
	  
	    }
	    else if(role.equals("sub")) {
		    List<TierDTO> tierlist = ts.getslist(position);
		    
			   
		    for(int a = 0 ; a<tierlist.size() ; a++ ) {	
		    	 Matcher matcher = pattern.matcher(tierlist.get(a).getLOLNickName());
	             if (matcher.find()) {
	                tierlist.get(a).setLOLNickName1(matcher.group(1));
	                tierlist.get(a).setLOLNickName2(matcher.group(2));
	             }
	             String bjurl = tierlist.get(a).getBJID();
		    	tierlist.get(a).setImgUrl("/bjimg/" + bjurl+ ".jpg");
			    model.addAttribute("tierList", tierlist);
			    model.addAttribute("position", position);
	    }
        
	    }

	    return "view/tier";
	}
	    


}
