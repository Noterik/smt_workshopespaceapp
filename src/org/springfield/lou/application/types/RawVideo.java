/* 
* RawVideo.java
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

import org.springfield.fs.FsNode;

/**
 * RawVideo.java
 *
 * @author Pieter van Leeuwen
 * @copyright Copyright: Noterik B.V. 2015
 * @package org.springfield.lou.application.types
 * 
 */
public class RawVideo {
	private FsNode rawvideo;
	
	public RawVideo(FsNode rawvideo) {
		this.rawvideo = rawvideo;
	}
	
	public String getProperty(String property) {
		return rawvideo.getProperty(property, "");
	}
	
	public String getVideoStream() {
		String mounts = rawvideo.getProperty("mount");
		String mount = mounts.substring(0, mounts.indexOf(","));
		return "http://"+mount+".noterik.com/progressive/"+mount+rawvideo.getPath()+"/raw.mp4";
	}
}
