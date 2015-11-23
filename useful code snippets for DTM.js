// events in PLRs
for(var i = 0, max = _satellite.pageLoadRules.length; i < max; i++) { 
	var plr = _satellite.pageLoadRules[i]; 
	console.log("PLR: " + plr.name);
	for(var j = 0, max2 = plr.trigger.length; j < max2; j++) { 
		var tr = plr.trigger[j]; 
		if (tr.command == "addEvent") { 
			for (k = 0; k < tr.arguments.length; k++) { 
				console.log("  " + tr.arguments[k]); 
			} 
		} 
	}
}

// events in EBRs
for (var i = 0, max = _satellite.rules.length; i < max; i++) {
	var ebr = _satellite.rules[i];
	console.log("EBR: " + ebr.name);
	for (var j = 0, max2 = ebr.trigger.length; j < max2; j++) {
		var tr = ebr.trigger[j];
		if (typeof tr.arguments !== 'undefined') {
			for (var k = 0, max3 = tr.arguments.length; k < max3; k++) {
				var arg = tr.arguments[k];
				if (typeof arg.addEvent !== 'undefined') {
					for (var l = 0; l < arg.addEvent.length; l++) {
						console.log("  " + arg.addEvent[l]);
					}
				}
			}
		}
	}
}

// events in DCRs
