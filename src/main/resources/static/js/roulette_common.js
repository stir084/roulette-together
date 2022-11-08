let theWheel; // 룰렛
let wheelObject;
let rotationAngle = 0; // 룰렛 각도
let segmentColorConfigArray = new Array('#007BFF', '#DC3545', '#28A745', '#FFC107'); // 룰렛 세그먼트 색깔
let segmentArray = new Array(); // 세그먼트 배열

// 이미지 파일이 로드되고 나서 룰렛 생성
let img = new Image();
img.src = 'assets/img/roulette-start.png';
img.onload = function(){
    initRoulette();
}

/**
 * 완료된 룰렛 각도 설정
 */
function setRouletteAngle(roulette, rouletteSegment){

    $("#result").text("결과 : " + rouletteSegment[roulette.prize - 1].element);
    $("#canvas").removeAttr("onclick");

    let angle = 360 / rouletteSegment.length;

    rotationAngle = (((roulette.prize) * angle) - (angle / 2)) * -1 % 360;
}
/**
 * 룰렛 세그먼트 초기화
 */
function setRouletteSegment(rouletteSegment) {
    rouletteSegment.forEach(function(item, index){
        if(rouletteSegment.length < index){
            index = index % rouletteSegment.length;
        }
        let rouletteSegmentObj = new Object();
        rouletteSegmentObj.fillStyle = segmentColorConfigArray[index];
        rouletteSegmentObj.text = item.element;
        segmentArray.push(rouletteSegmentObj);
    })
}

/**
 * 룰렛 생성 - 초기화
 */
function setRoulette(img) {
    wheelObject = {
        'numSegments': segmentArray.length, //8,     // Specify number of segments.
        'outerRadius': 212,   // Set outer radius so wheel fits inside the background.
        'textFontSize': 28,    // Set font size as desired.
        'innerRadius': 40,         // Make wheel hollow so segments don't go all way to center.
        'rotationAngle': rotationAngle,
        'textAlignment': 'outer',    // Align text to outside of wheel.
        'innerImage': img,
        'textFillStyle' : "white",
        'lineWidth': 3,
        'segments': segmentArray,     // Define segments including colour and text.
        /*[
            {'fillStyle': '#007BFF', 'text': 'Prize 2'},
            {'fillStyle': '#DC3545', 'text': 'Prize 2'},
            {'fillStyle': '#28A745', 'text': 'Prize 3'},
            {'fillStyle': '#FFC107', 'text': 'Prize 4'},
            {'fillStyle': '#007BFF', 'text': 'Prize 5'},
            {'fillStyle': '#DC3545', 'text': 'Prize 6'},
            {'fillStyle': '#28A745', 'text': 'Prize 7'},
            {'fillStyle': '#FFC107', 'text': 'Prize 8'}
        ],*/
        'animation':           // Specify the animation to use.
            {
                'type': 'spinToStop',
                'duration': 5,     // Duration in seconds.
                'spins': 8,     // Number of complete spins.
                'callbackFinished': alertPrize
            }
    }
    // Create new wheel object specifying the parameters at creation time.
    theWheel = new Winwheel(wheelObject);
}
/**
 * 스핀 파워 설정 - 사용안함
 */
function powerSelected(powerLevel)
{
    // Ensure that power can't be changed while wheel is spinning.
    if (wheelSpinning == false) {
        // Reset all to grey incase this is not the first time the user has selected the power.
        document.getElementById('pw1').className = "";
        document.getElementById('pw2').className = "";
        document.getElementById('pw3').className = "";

        // Now light up all cells below-and-including the one selected by changing the class.
        if (powerLevel >= 1) {
            document.getElementById('pw1').className = "pw1";
        }

        if (powerLevel >= 2) {
            document.getElementById('pw2').className = "pw2";
        }

        if (powerLevel >= 3) {
            document.getElementById('pw3').className = "pw3";
        }

        // Set wheelPower var used when spin button is clicked.
        wheelPower = powerLevel;

        // Light up the spin button by changing it's source image and adding a clickable class to it.
        //document.getElementById('spin_button').src = "spin_on.png";
        //document.getElementById('spin_button').className = "clickable";
    }
}


// Vars used by the code in this page to do power controls.
let wheelPower    = 0;
let wheelSpinning = false;


function saveRouletteItem(){
    //현재 색깔 설정 타입
    //
    wheelObject.segments.push({'fillStyle': '#FFC107', 'text': $("#rouletteItem").val()});
    wheelObject.numSegments = wheelObject.numSegments + 1;
    theWheel = new Winwheel(wheelObject);
    $("#rouletteItem").val("");
    //실제 데이터 저장
}
// -------------------------------------------------------
// 회전 시작
// -------------------------------------------------------


// -------------------------------------------------------
// 룰렛 초기화
// -------------------------------------------------------
/*function resetWheel()
{
    theWheel.stopAnimation(false);  // Stop the animation, false as param so does not call callback function.
    theWheel.rotationAngle = 0;     // Re-set the wheel angle to 0 degrees.

    theWheel.draw();                // Call draw to render changes to the wheel.

    wheelSpinning = false;          // Reset to false to power buttons and spin can be clicked again.
}*/
function resetRoulette(){
    var form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("action", "/roulette");
    document.body.appendChild(form);
    form.submit();

    //팝업창 오픈
    //ㅇ
}
function alertPrize(indicatedSegment)
{
    $("#result").text("결과 : "+indicatedSegment.text);
    //alert("You have won " + indicatedSegment.text);
}