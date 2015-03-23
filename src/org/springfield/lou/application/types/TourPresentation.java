/* 
* TourPresentation.java
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
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.mojo.http.HttpHelper;
import org.springfield.mojo.http.Response;

/**
 * TourPresentation.java
 *
 * @author Pieter van Leeuwen
 * @copyright Copyright: Noterik B.V. 2015
 * @package org.springfield.lou.application.types
 * 
 */
public class TourPresentation {
	private static final String EDITORTOOLURI = "http://editortool.linkedtv.eu/load_curated?id=";
	
	private FsNode presentation;
	private PresentationVideo[] presentationVideos;
	List<String> posters;
	List<String> locationCoordinates;
	String mainCoordinates;
	
	public TourPresentation(FsNode presentation) {
		String prefer = presentation.getReferid();
   	 	FsNode p = Fs.getNode(prefer);
   	 	this.presentation = p;
   	 	
   	 	List<FsNode> videos = Fs.getNodes(prefer+"/videoplaylist/1/video",1);
   	 	presentationVideos = new PresentationVideo[videos.size()];
   	 	
   	 	int i = 0;
   	 	
   	 	for (FsNode video : videos) {
   	 		PresentationVideo v = new PresentationVideo(video);
   	 		presentationVideos[i] = v;
   	 		i++;
   	 	}
		
   	 	List<FsNode> locations = Fs.getNodes(prefer+"/location",1);
   	 	locationCoordinates = new ArrayList<String>();
   	 	
   	 	for (FsNode location : locations) {
   	 		String lat = location.getProperty("latitude");
   	 		String lon = location.getProperty("longitude");
   	 		locationCoordinates.add(lat+"|"+lon);
   	 		
   	 		if (location.getProperty("main_location").equals("true")) {
   	 			mainCoordinates = lat+"|"+lon;
   	 		}
   	 	}
   	 	
		loadEuropeanaPosters(getVideo("de").getProperty("original_id"));
	}
	
	public String getProperty(String property) {
		return presentation.getProperty(property, "");
	}
	
	public PresentationVideo getVideo(String language) {
		for (int i = 0; i < presentationVideos.length; i++) {
			if (presentationVideos[i].getProperty("language").equals(language)) {
				return presentationVideos[i];
			}
		}
		return null;
	}
	
	private void loadEuropeanaPosters(String id) {
		Response etResponse = HttpHelper.sendRequest("GET", EDITORTOOLURI+id);
		String data = etResponse.getResponse();

		posters = new ArrayList<String>();
		
		try {
			Object obj = JSONValue.parse(data);
		
			JSONObject jsonObject = (JSONObject)obj;
			System.out.println(jsonObject.getClass());
			JSONObject temp = null;
			for(Iterator i1 = jsonObject.keySet().iterator(); i1.hasNext();) {
				String key = (String) i1.next();
				if (key.equals("chapters")) {
					JSONArray chapters = (JSONArray)jsonObject.get(key);
	
					for(Object c : chapters) {
						temp = (JSONObject)c;
						JSONObject dim = (JSONObject)((JSONObject)temp.get("dimensions")).get("tve_2");
						JSONArray annotations = (JSONArray)dim.get("annotations");
						for(Object a : annotations) {
							temp = (JSONObject)a;
							posters.add(temp.get("poster").toString());
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Could not load date from Editor Tool:"+e.getStackTrace());
		}
	}
	
	public String getEuropeanaPoster(int i) {
		if (posters == null) {
			return "";
		}

		if (i >= 0  && i < posters.size()) {
			return posters.get(i);
		}
		return posters.get(posters.size()-1);
	}
	
	public List<String> getEuropeanaPosters() {
		return posters;
	}
	
	public List<String> getLocationCoordinates() {
		return locationCoordinates;
	}
	
	public String getMainCoordinates() {
		return mainCoordinates;
	}
}
