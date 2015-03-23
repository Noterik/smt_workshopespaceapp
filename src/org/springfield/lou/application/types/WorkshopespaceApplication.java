/* 
* WorkshopespaceApplication.java
* 
* Copyright (c) 2015 Noterik B.V.
* 
* This file is part of smt_workshopespaceapp, related to the Noterik Springfield project.
*
* smt_workshopespaceapp is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* smt_workshopespaceapp is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with smt_workshopespaceapp.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.springfield.lou.application.types;

import java.util.ArrayList;
import java.util.List;

import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.Html5Application;
import org.springfield.lou.screen.Capabilities;
import org.springfield.lou.screen.Screen;

/**
 * WorkshopespaceApplication.java
 *
 * @author Pieter van Leeuwen
 * @copyright Copyright: Noterik B.V. 2015
 * @package org.springfield.lou.application.types
 * 
 */
public class WorkshopespaceApplication extends Html5Application {
	
	public static enum quality { original, nhd, hd, fullhd, mobile; }
	
	private static final String toursBaseUrl = "/domain/espace/user/rbb/collection/";
	private Tour tour = null;
	private int currentEuropeanaImage = 0;
	private String language = "uk";
	private String quality = "nhd";

	public WorkshopespaceApplication(String id) {
		super(id);
	}
	
    public void onNewScreen(Screen s) {
    	//allow hbbtv testing on non hbbtv devices using role=hbbtv
    	String role = s.getParameter("role");
    	if (role == null) {
    		role = "";
    	}

    	Capabilities cap = s.getCapabilities();
 		
 		loadStyleSheet(s, "generic");
 		
 		if (cap.getDeviceMode() == cap.MODE_HBBTV || role.equals("hbbtv")) {
 			//HBBTV main screen
 			s.setRole("mainscreen");
 			loadStyleSheet(s, "hbbtv");
 			loadContent(s, "hbbtvlayout");
 		} else {
 			//second screen
 			loadStyleSheet(s, "secondscreen");
 			loadContent(s, "secondscreenlayout");
 			
 			List<FsNode> tours = loadTours();
 			
 			String toursListing = formatTours(tours);
 			s.setContent("tourslisting", toursListing);
 		}
    }

    private List<FsNode> loadTours() {
    	List<FsNode> collections = Fs.getNodes(toursBaseUrl,1);
    	
    	List<FsNode> tours = new ArrayList<FsNode>();
    	for (FsNode collection: collections) {
    		if (collection.getId().indexOf("tour") > -1) {
    			tours.add(collection);
    		}   		
    	}
    	return tours;
    }
    
    private String formatTours(List<FsNode> tours) {
    	String html = "<ul>";
    	
    	for (FsNode tour: tours) {
    		html += "<li><a href='#' onclick='loadTour(\""+toursBaseUrl+tour.getId()+"\")'>"+tour.getProperty("title")+"</a></li>";
    	}
    	
    	html += "</ul>";
    	return html;
    }
    
    private void loadTour(String content) { 
    	tour = new Tour(content);
    
        this.componentmanager.getComponent("hbbtvlayout").put("app", "hideLoading()");
        this.componentmanager.getComponent("hbbtvlayout").put("app", "setBackground(y1970)");
        this.componentmanager.getComponent("hbbtvlayout").put("app", "putTitle("+tour.getProperty("title")+")"); 
        loadVideo();
    }
    
    private void loadVideo() {
    	this.componentmanager.getComponent("hbbtvlayout").put("app", "putVideoTitle("+tour.getCurrentPresentation().getProperty("title")+")");
    	this.componentmanager.getComponent("hbbtvlayout").put("app", "putVideoDate("+tour.getCurrentPresentation().getProperty("date")+")");
    	this.componentmanager.getComponent("hbbtvlayout").put("app", "putDescription("+tour.getCurrentPresentation().getProperty("description")+")");
        this.componentmanager.getComponent("hbbtvlayout").put("app", "displayOn()");
        this.componentmanager.getComponent("hbbtvlayout").put("app", "setVideo("+tour.getCurrentPresentation().getVideo(language).getRaw(quality).getVideoStream()+")");
        this.componentmanager.getComponent("hbbtvlayout").put("app", "setEuropeanaImage("+tour.getCurrentPresentation().getEuropeanaPoster(currentEuropeanaImage)+")");
        this.componentmanager.getComponent("secondscreenlayout").put("app", "showcontrols()");
        this.componentmanager.getComponent("secondscreenlayout").put("app", "updatemap("+tour.getCurrentPresentation().getMainCoordinates()+")");
    }
    
    public void putOnScreen(Screen s,String from,String msg) {
    	int pos = msg.indexOf("(");
        if (pos!=-1) {
        	String command = msg.substring(0,pos);
            String content = msg.substring(pos+1,msg.length()-1);
            if (command.equals("loadTour")) {
            	loadTour(content);
            } else if (command.equals("timeupdate")) {
                handleTimeupdate(s,content);
            } else if (command.equals("playoutfinished")) {
            	int totalPresentations = tour.getNumberOfPresentations();
            	if (tour.getCurrentPresentationNumber()+1 < totalPresentations) {
            		tour.setCurrentPresentationNumber(tour.getCurrentPresentationNumber()+1);
            		loadVideo();
            	}
            } else if (command.equals("loadPrevious")) {
            	if (tour.getCurrentPresentationNumber() > 0) {
            		tour.setCurrentPresentationNumber(tour.getCurrentPresentationNumber()-1);
            		loadVideo();
            	}
            } else if (command.equals("loadNext")) {
            	int totalPresentations = tour.getNumberOfPresentations();
            	if (tour.getCurrentPresentationNumber()+1 < totalPresentations) {
            		tour.setCurrentPresentationNumber(tour.getCurrentPresentationNumber()+1);
            		loadVideo();
            	}
            } else if (command.equals("togglePlayPause")) {
            	this.componentmanager.getComponent("hbbtvlayout").put("app", "togglePlayPause()");
            }
         }
    }
    
    private void handleTimeupdate(Screen s,String content) {    	
    	String[] parts = content.split(":");
    	
    	if (parts.length != 2) {
    		return;
    	}
    	
    	if (tour == null) {
    		//no tour selected yet
    		return; 
    	}
    	
    	int position = Integer.parseInt(parts[0]);
    	String d = tour.getCurrentPresentation().getVideo(language).getRaw(quality).getProperty("duration");
    	double duration = Double.parseDouble(d);

        List<String> posters = tour.getCurrentPresentation().getEuropeanaPosters();
        
        int interval = (int) duration / posters.size();
        
        if (position/interval > currentEuropeanaImage) {
        	currentEuropeanaImage++;
        	 this.componentmanager.getComponent("hbbtvlayout").put("app", "setEuropeanaImage("+tour.getCurrentPresentation().getEuropeanaPoster(currentEuropeanaImage)+")");
        }  	
    }
}
