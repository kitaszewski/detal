function charCounter(str) {
		var lng = str.length;
		if (lng > 160) {
			document.getElementById("messageLength").style.color = "red";
		} else {
			document.getElementById("messageLength").style.color = "black";
		}
		document.getElementById("messageLength").innerHTML = lng + ' znaków. Limit 160 znaków.';
	}