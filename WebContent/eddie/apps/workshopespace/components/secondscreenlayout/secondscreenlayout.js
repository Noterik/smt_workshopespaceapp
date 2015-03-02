var map;

var Secondscreenlayout = function(options) {
	var self = {};
	var settings = {}
	
	loadMapScript();
	
	$.extend(settings, options);
    
	self.putMsg = function(msg){
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.currentTarget).attr('class').split(" ");
		}
		var content = msg.content;
		for(i = 0; i < command.length; i++) {
			console.log(command[i]);
			switch(command[i]) { 
	  			case 'showcontrols':
					$('#controls').show();
	  				break;
	  			case 'updatemap':
	  				var parts = content.split("|");
	  				if (parts.length == 2) {
	  					var latlng = new google.maps.LatLng(parts[0],parts[1]);
	  					var marker = new google.maps.Marker({
	  						position: latlng,
	  						map: map
	  					});
	  					map.panTo(latlng);
	  					map.setZoom(16);
					}
	  				break;
				default:
					console.log('unhandled msg in secondscreenlayout.js : '+command); 
			}
		}
	}	
	return self;
}

function loadTour(tour) {
	eddie.putLou('', 'loadTour('+tour+')');
}

function prev() {
	eddie.putLou('', 'loadPrevious()');
}

function playpause() {
	eddie.putLou('', 'togglePlayPause()');
}

function next() {
	eddie.putLou('', 'loadNext()');
}

function initializemap() {
	var mapOptions = {
	    zoom: 11,
	    center: new google.maps.LatLng(52.5018995, 13.3983573)
	};

	map = new google.maps.Map(document.getElementById('map-canvas'),
	      mapOptions);
}

function loadMapScript() {
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp' +
	      '&key=AIzaSyDJOxqX-ZLHR0D1hmfnHvclgHJFU7AYFPY&callback=initializemap';
	document.body.appendChild(script);
}
