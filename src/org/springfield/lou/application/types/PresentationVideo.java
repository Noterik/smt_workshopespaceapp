/* 
* PresentationVideo.java
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
 * PresentationVideo.java
 *
 * @author Pieter van Leeuwen
 * @copyright Copyright: Noterik B.V. 2015
 * @package org.springfield.lou.application.types
 * 
 */
public class PresentationVideo {
	private FsNode video;
	private RawVideo[] rawVideos;
	
	public static enum quality { original, nhd, hd, fullhd, mobile; }
	
	public PresentationVideo(FsNode video) {
		String vrefer = video.getReferid();
		FsNode v = Fs.getNode(vrefer);
		this.video = v;
		
		List<FsNode> raws = Fs.getNodes(vrefer+"/rawvideo",1);
		rawVideos = new RawVideo[raws.size()];
		
		int i = 0;
		
		for (FsNode raw : raws) {
			RawVideo r = new RawVideo(raw);
			rawVideos[i] = r;
			
			i++;
		}
	}
	
	public String getProperty(String property) {
		return video.getProperty(property, "");
	}
	
	public RawVideo getRaw(String q) {
		switch (quality.valueOf(q)) {
		case original :
			return rawVideos[0];
		case nhd :
			return rawVideos[1];
		case hd :
			return rawVideos[2];
		case fullhd :
			return rawVideos[3];
		case mobile :
			return rawVideos[4];
		}
		return rawVideos[1];
	}
	
	
}
