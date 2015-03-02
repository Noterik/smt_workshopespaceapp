/* 
* Tour.java
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

import java.util.List;

import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;

/**
 * Tour.java
 *
 * @author Pieter van Leeuwen
 * @copyright Copyright: Noterik B.V. 2015
 * @package org.springfield.lou.application.types
 * 
 */
public class Tour {
	private FsNode tour;
	private int currentPresentation = 0;
	private TourPresentation[] tourPresentations;
	
	public Tour(String uri) {
		tour = Fs.getNode(uri);
		
		List<FsNode> presentations = Fs.getNodes(uri+"/presentation", 1);
		tourPresentations = new TourPresentation[presentations.size()];
		
		int i = 0;
		
		for (FsNode presentation : presentations) {
			TourPresentation t = new TourPresentation(presentation);
			tourPresentations[i] = t;
			i++;
		}
	}
	
	public String getProperty(String property) {
		return tour.getProperty(property, "");
	}
	
	public TourPresentation getPresentation(int i) {
		if (i >= 0 && i < tourPresentations.length) {
			return tourPresentations[i];
		}
		
		return null;
	}
	
	public int getCurrentPresentationNumber() {
		return currentPresentation;
	}
	
	public TourPresentation getCurrentPresentation() {
		return tourPresentations[currentPresentation];
	}
	
	public void setCurrentPresentationNumber(int i) {
		currentPresentation = i;
	}
	
	public int getNumberOfPresentations() {
		return tourPresentations.length;
	}
}
