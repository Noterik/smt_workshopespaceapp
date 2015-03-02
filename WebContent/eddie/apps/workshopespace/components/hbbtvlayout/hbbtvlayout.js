var Hbbtvlayout = function(options) {
	var self = {};
	var settings = {}
	
	$.extend(settings, options);
	//setInterval((function(){var position = isNaN(document.getElementById("video1").playPosition) ? 0 : document.getElementById("video1").playPosition; var duration = isNaN(document.getElementById("video1").playTime) ? 0 : document.getElementById("video1").playTime; eddie.putLou('','timeupdate('+Math.floor(position/1000)+':'+Math.floor(duration/1000)+')');}), 1000);
	var vid = $("#video1").get(0);
	vid.onPlayStateChange = onPlayStateChange;
	vid.onPlayPositionChanged = updateProgress;
	vid.onPlaySpeedChanged = onPlaySpeedChange;
    vid.onError = onPlayStateChange;
    setInterval('checkPlayState()', 1000);
    
	self.putMsg = function(msg){
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.currentTarget).attr('class').split(" ");
		}
		var content = msg.content;
		for(i=0;i<command.length;i++){
			switch(command[i]) { 
				case 'setBackground':
					var body = document.getElementsByTagName('body')[0];
					body.setAttribute("class", content);
	  				break;
	  			case 'hideLoading':
					$('.loading_wrapper').hide();
	  				break;
	  			case 'putTitle':
	  				$('#tourtitle').html(content);
	  				break;
	  			case 'putVideoTitle':
	  				$('.videotitle').html(content);
	  				break;
	  			case 'putVideoDate':
	  				var parts = content.split('-');
	  				if (parts.length == 3) {
	  					$('.videodate').html(parts[2]+" / "+parts[1]+" / "+parts[0]);
	  				}
	  				break;
	  			case 'putDescription':
	  				var description = content.substring(0, content.indexOf('\t'));
	  				$('#videodescription').html(description);
	  				break;
	  			case 'displayOn':
	  				$('#display_wrapper').show();
	  				break;
	  			case 'setVideo':
	  				$('#video1').get(0).stop();
	  				$('#video1').attr('data', content);
	  				$('#video1').get(0).play(1);
	  				
	  				break;
	  			case 'setEuropeanaImage':
	  				$('#europeanaimage').attr('src', content);
	  				break;
	  			case 'togglePlayPause':
	  				var video = $("#video1").get(0);
	  				var playState = video.playState;
	  				
	  				if (playState == 1) {
	  					video.play(0);
	  				} else if (playState == 2) {
	  					video.play(1);
	  				}
	  				break;
				default:
					console.log('unhandled msg in hbbtvlayout.js : '+command); 
			}
		}
	}
	
	return self;
}

function onPlayStateChange() {
	var video = $("#video1").get(0);
	console.log(video.playState);
}

function updateProgress() {
	var video = $("#video1").get(0);
	console.log(video.playState);
	console.log(video.playPosition);
}

function onPlaySpeedChange() {
	console.log("");
}

function onPlayStateChange() {
	var video = $("#video1").get(0);
	console.log(video.playState);
}

function checkPlayState() {
	var video = $("#video1").get(0);
	var position = isNaN(video.playPosition) ? 0 : video.playPosition; 
	var duration = isNaN(video.playTime) ? 0 : video.playTime; 
	eddie.putLou('','timeupdate('+Math.floor(position/1000)+':'+Math.floor(duration/1000)+')');
	
    var playState = video.playState;

    switch (playState) {
        case 0: // stopped
        	console.log("playstate 0");
            break;
        case 1: // playing
        	console.log("playstate 1");
            break;
        case 2: // paused
        	console.log("playstate 2");
            break;
        case 3: // connecting
        	console.log("playstate 3");
        	break;
        case 4: // buffering
        	console.log("playstate 4");
            break;
        case 5: // finished
        	console.log("playstate 5");
            eddie.putLou('','playoutfinished()');
            break;
        case 6: // error
        	console.log("playstate 6");
            break;
        default:
            // do nothing
            break;
    }
}