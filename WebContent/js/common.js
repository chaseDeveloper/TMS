var common_options = {
    width: 'auto',
    height: '100%',
    collapsible: {on: true, toggle: false},    
    columnBorders: true,
    freezeCols: 0,
    freezeRows: 0,                        		
    numberCell: { show: true, width: 40 },
    pageModel: {
        type: "",
        rPP: 10, strRpp: "{0}",

        //customize localization strings.
        strDisplay: "{0} to {1} of {2}",
        strPage: "Page {0} / {1}",

        layout: ["strRpp", "|", 'first', 'prev', 'next', 'last', "|", "strPage", ]
    },

    //important option for this example.
    reactive: false,
    roundCorners: false,
    rowBorders: false,
    selectionModel: { type: 'cell', mode : 'block' },
    stripeRows: true,
    scrollModel: {autoFit: true},     
	trackModel: { on: true }, //to turn on the track changes
	autoRowHead : true,
	autoRow : false,
	rowHt : 40,
	wrap: false,     
    showHeader: true,
    showTitle: false,
    showToolbar: false,
    showTop: false,
    editable: false,
    autofill: false,
    fillHandle : '',
    title: "Grid Parts",
	menuUI : {
        buttons: [], //use no button at bottom of filter menu
        gridOptions: {
            rowHt: 30 //increase height of rows in menu grid.
        },
        tabs: [ 'filter' ] //display only filter tab in menu.
    }
};

 

function getGridHeight() {
	var sectionHegith = $(".sectionBody").height();
	var sectionContentTopHeight = $(".sectionContentTop").height();
	var sectionContentFilterAreaHeight = $(".sectionContentFilterArea").height();
	console.log("contentTopHeight: " + (sectionContentTopHeight + sectionContentFilterAreaHeight + 10));
	var contentTopHeight = (sectionContentTopHeight + sectionContentFilterAreaHeight + 70);
	console.log(sectionHegith);
	var gridHeight = sectionHegith - contentTopHeight;
	return gridHeight;
}

function errorHandler(xhr, textStatus) {
	console.log(xhr);
	if(xhr.status == 591) {
		alert(xhr.responseJSON.msg);
		top.location.href=xhr.responseJSON.url;
	} else if(xhr.status == 590) {
		alert(xhr.responseJSON.msg);
	} else {
		try {
			clearInterval(notifyInterval);
		} catch (e) {
			// TODO: handle exception
		}
		alert("서버와의 연결이 끊어졌습니다");
	}
}

function getContextPath() {
	var hostIndex = location.href.indexOf(location.host) + location.host.length;
	var contextPath = location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
	return contextPath;
}

function commonFileDownload(fileId, fileSeq) {
	$("#fileForm").html("");
	var fileIdObj = $(document.createElement("input")).attr("type", "hidden").attr("name", "fileId").val(fileId);
	var fileSeqObj = $(document.createElement("input")).attr("type", "hidden").attr("name", "fileSeq").val(fileSeq);
	$("#fileForm").append(fileIdObj).append(fileSeqObj);
	$("#fileForm").attr("action", getContextPath() + '/main/fileDownload.do');
	$("#fileForm").submit();
}



function numberCheck(id, maxlength) {
	console.log("id: " + id);
	console.log("maxlength: " + maxlength);
	
	
	$("#" + id).keydown( function(e) {
		console.log("keydown keycode: " + e.keyCode);
		console.log("keydown value: " + $(this).val());
		$(this).data("keyCode", e.keyCode);	
		$(this).data("olddata", $(this).val());	
		
	});
	
	$("#" + id).on('input', function() {		
		console.log("input value: " + $(this).val());
		/*
		if($(this).data("keyCode") == 229 || $(this).data("keyCode") == 69
			|| $(this).data("keyCode") == 190 || $(this).data("keyCode") == 187
			|| $(this).data("keyCode") == 189) {
			$(this).val($(this).data("olddata"));
		}
		*/
		if($(this).data("keyCode") < 48 || 57 < $(this).data("keyCode")) {
			//예외 방향키(37 ~ 40), 백스페이스(8), HOME, ENd (36,35), DEL(46)
			if($(this).data("keyCode") != 35
				&& $(this).data("keyCode") != 36
				&& $(this).data("keyCode") != 37
				&& $(this).data("keyCode") != 38
				&& $(this).data("keyCode") != 39
				&& $(this).data("keyCode") != 40
				&& $(this).data("keyCode") != 46
				&& $(this).data("keyCode") != 8) {
					const num = /^[0-9]+$/;
					if(!num.test($(this).val())) {
						$(this).val($(this).data("olddata"));
					}
	
				}						
		}
		
		if($(this).val().length > maxlength) {
			$(this).val($(this).data("olddata"));
		}
	});
}



$.datepicker.regional['ko'] = {
	    closeText: '닫기',
	    prevText: '이전달',
	    nextText: '다음달',
	    currentText: '오늘',
	    monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	    monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	    dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'],
	    dayNamesShort: ['일','월','화','수','목','금','토'],
	    dayNamesMin: ['일','월','화','수','목','금','토'],
	    weekHeader: '주',
	    dateFormat: 'yy-mm-dd',
	    firstDay: 0,
	    isRTL: false,
	    showMonthAfterYear: true,
	    yearSuffix: '년'
	};
	$.datepicker.setDefaults($.datepicker.regional['ko']);
