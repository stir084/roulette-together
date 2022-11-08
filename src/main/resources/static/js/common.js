/**
 * 카카오 이미지 캐시 방지를 위한 랜덤 스트링
 */
const generateRandomString = (num) => {
    const characters ='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    let result = '';
    const charactersLength = characters.length;
    for (let i = 0; i < num; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    return result;
}

/**
 * 모바일 side-nav 활성화 시 Body 클릭 시 돌아가기
 */
$("#layoutSidenav_content").on("click", function(){
    $("body").removeClass("sb-sidenav-toggled")
})

/**
 * 이미지 로딩 속도 개선
 */
function preloading (imageArray) {
    let n = imageArray.length;
    for (let i = 0; i < n; i++) {
        let img = new Image();
        img.src = imageArray[i];
    }
}

fn_ajaxSubmit = function (p_formId, p_url, httpMethod, p_callback, progressYn) {
 	if (progressYn == "N") {
	} else {
       //showLoadingBar();
       $('#container').loading('start');
	}

   	var opt = {
   	    url: p_url, type: httpMethod,
   	    success	: function(data) {
   	    	var obj = {};
/*   	    	try {
   	    		obj = JSON.parse(data);
   	    		if (obj.throwException) obj = obj.throwException;
   	    	} catch(e) {
   	    		try {
   	    			obj = JSON.parse(decodeURIComponent(data.replace(/\+/g,'%20')));
   	    		} catch(e) {
   	    			obj = "알수없는 오류가 발생하였습니다.";
   	    		}
   	    	}*/
            //hideLoadingBar();
            $('#container').loading('stop');

   	    	if (typeof(obj) == "string") {
   	    		console.log(data);
   	    		alert(obj);
   	    	} else if (typeof(p_callback) != "undefined") {
   	    		p_callback(obj);
   	    	}
   	    }, error : function(request, textStatus, errorThrown) {
   	        alert(JSON.stringify(request)+"-"+textStatus+"-"+errorThrown);
   			//location.href = "/html/error.html";
   		}
   	};

   	$(p_formId).ajaxSubmit(opt);
};


function showLoadingBar() {
    var maskHeight = $(document).height();
    var maskWidth = window.document.body.clientWidth;

    var mask = "<div id='mask' style='position:absolute; z-index:9000; display:none; left:0; top:0;'></div>";
    var loadingImg = '';

    loadingImg += "<div id='loadingImg' style='position:absolute; left:50%; top:40%; display:none; z-index:10000;'>";
    loadingImg += "    <img src='./assets/img/loading.gif'/>";
    loadingImg += "</div>";

    $('body').append(mask).append(loadingImg);

    $('#mask').css({
        'width' : maskWidth
        , 'height': maskHeight
        , 'opacity' : '0.3'
    });

    $('#mask').show();
    $('#loadingImg').show();
}

function hideLoadingBar() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').remove();
}