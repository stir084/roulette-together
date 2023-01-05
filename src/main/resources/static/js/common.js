
/**
 * 사용자 고유 ID 생성
 */
var du = new DeviceUUID().get(); //.parse();
document.cookie = "userUUID="+du;
/**
 * 버튼에 클립보드 기능 연결
 */
var clipboard = new ClipboardJS('.shareBtn');

clipboard.on('success', function(e) {
    console.log(e);
});

clipboard.on('error', function(e) {
    console.log(e);
});


/**
 * 모바일 체크
 */
window.mobileCheck = function() {
    let check = false;
    (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
    return check;
};
/**
 * PC일 경우 목록 버튼 비활성화
 */
$(document).ready(function() {
    $(".navbar-brand").css("padding-left", "0.5rem");
    if(window.mobileCheck()==false){
        //$("#sidebarToggle").hide();
    }else{
       // $(".navbar-brand").css("padding-left", "0.5rem");
    }
})
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
    if(true == window.mobileCheck()){
        $("body").removeClass("sb-sidenav-toggled")
    }
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