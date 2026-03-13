// Make the DIV element draggable:


function dragElement(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;

	/*
  if (document.getElementById(elmnt.id + "header")) {
    // if present, the header is where you move the DIV from:
    document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
  } else {
    // otherwise, move the DIV from anywhere inside the DIV:
    elmnt.onmousedown = dragMouseDown;
  }
*/
	if($(elmnt).find(".popheader").length > 0) {
		$(elmnt).find(".popheader")[0].onmousedown = dragMouseDown;
	} else {
		elmnt.onmousedown = dragMouseDown;
	}

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
  }

  function closeDragElement() {
    // stop moving when mouse button is released:
    document.onmouseup = null;
    document.onmousemove = null;
  }
}

function modalDialogOpen(id) {
	var obj = document.getElementById(id);
	var parent = obj.parentNode;
	parent.style.display = "flex";
	
	$.each($(parent).find("input"), function(idx, element) {
		if($(element).attr("type") == 'text') {
			$(element).val("");
		} else if($(element).attr("type") == 'radio' || $(element).attr("type") == 'checkbox') {
			$(element).prop("checked", false);
		} 
	});
	
	$.each($(parent).find("select"), function(idx, element) {
		$(element).find("option").eq(0).prop("selected");
	});
	
	dragElement(obj);
	
}