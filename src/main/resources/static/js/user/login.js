/**
 * Created by MHLab on 29/11/2018..
 */


$(document).ready(function(){
    var serverMsg = $('#server_msg').val();
    var userIdObj = $('#userId');
    if(serverMsg === 'nosession') {
        showSAlert('세션 문제', '세션이 만료되었습니다.', 'error');
    }
    else if(serverMsg === 'logout') {
        showSAlert('로그아웃', '정상적으로 로그아웃되었습니다.', 'success');
    }
    else if(serverMsg === 'security4AutoLogin') {
        showSAlert('토큰 문제', '자동 로그인이 만료되었습니다.', 'error');
    }

    if(Cookies.get('keepUserId') !== undefined) {
        userIdObj.val(Cookies.get('keepUserId'));
        userIdObj.attr('placeholder', '');
        if(userIdObj.val() !== ''){ $('#keepId').attr('checked', true); }
    }

    initWithLoginView();
});


//Sign-Up Part (Enter)
$('#userId').keyup(function(e) { if (e.keyCode === 13) { click4Login(); } });
$('#userPw').keyup(function(e) { if (e.keyCode === 13) { click4Login(); } });


function click4Login() {
    if(validData()) {
        startLoading();
        var jsonObj = {'loginId':$('#userId').val(), 'loginPw':$('#userPw').val(), 'autoLogin':$('#keepLogin').is(':checked')}; //Create JSON Object

        $.ajax({
            url: '/users/login',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(jsonObj),
            dataType: 'text',
            timeout:5000 //5 second timeout
        }).done(function(jqXHR, textStatus){ //가입 성공
            endLoading();
            clean4LoginInput();
            var resultCode = JSON.parse(jqXHR).resultCode;
            if(resultCode === 10) { self.location = '/'; }
            else if(resultCode === 11) { self.location = '/admin/'; }
            else if(resultCode === -10) { showSAlert('로그인 실패', 'Id 또는 암호가 틀립니다.', 'error'); }
            else if(resultCode === -11) { showSAlert('로그인 실패', '비 활성화 계정입니다.', 'error'); }
            else if(resultCode === -12) { showSAlert('가입 대기', '해당 계정은 가입대기 상태입니다.', 'error'); }
        }).fail(function(jqXHR, textStatus){
            endLoading();
            showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
            console.log('jqXHR = ' + jqXHR);
            console.log('textStatus = ' + textStatus);
        });
    }
}


function click4SignUp() {
    if(validData4SignUp()) {
        startLoading();
        let jsonObj = {'signUpId':$('#su_id').val(), 'signUpName':$('#su_name').val(), 'teamName':$('#su_team').val(), 'signUpPw':$('#su_pw').val() }; //Create JSON Object

        $.ajax({
            url: '/users/signup',
            type: 'POST',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(jsonObj),
            dataType: 'text',
            timeout:5000 //5 second timeout
        }).done(function(jqXHR, textStatus){ //가입 성공
            clean4SignUpInput();
            endLoading();
            var resultCode = JSON.parse(jqXHR).resultCode;
            if(resultCode === 20) {
                clean4SignUpInput();
                $("#goLeft").click();
                showSAlert('가입 성공', '회원 등록이 완료되었습니다.', 'success');
            }
            else if(resultCode === -20) { showSAlert('가입 실패','이미 사용하고 있는 계정 ID 입니다.','error'); }
            else if(resultCode === -21) { showSAlert('가입 실패','시스템에서 사용하는 계정 ID 입니다.','error'); }
            else { showSAlert("Unknown","Error = " + textStatus,'error') }
        }).fail(function(jqXHR, textStatus){
            endLoading();
            showSAlert('Unknown fail','Error = ' + textStatus,'error');
            console.log('textStatus = ' + textStatus);
        });
    }
}

function validData4SignUp() {
    var signUpId = $('#su_id').val().toLowerCase();

    if(isNullValue(signUpId)) {
        showSAlert('사용자 ID 공백', '사용자 ID를 입력하세요.', 'error');
        return false;
    }
    else if(signUpId === 'admin' || signUpId === 'system') {
        showSAlert('ID 등록 불가', '사용할 수 없는 ID입니다.', 'error');
        return false;
    }
    else if(isNullValue($('#su_name').val())) {
        showSAlert('사용자 이름 공백', '사용자 이름를 입력하세요.', 'error');
        return false;
    }
    else if(isNullValue($('#su_team').val())) {
        showSAlert('부서명 공백', '부서명을 입력하세요.', 'error');
        return false;
    }
    else if(isNullValue($('#su_pw').val())) {
        showSAlert('암호 공백', '사용자 암호를 입력하세요.', 'error');
        return false;
    }
    else if(isNullValue($('#su_pw_valid').val())) {
        showSAlert('검증 암호 공백', '검증 암호를 입력하세요.', 'error')
        return false
    }
    else if($('#su_pw').val() !== $('#su_pw_valid').val()) {
        showSAlert('암호 불일치', '검증 암호가 틀립니다.', 'error');
        return false;
    }
    return true;
}


function validData() {
    if(isNullValue($('#userId').val())) {
        showSAlert('Id 공백', 'ID를 입력하세요.', 'error');
        return false;
    }
    else if (isNullValue($('#userPw').val())) {
        showSAlert('암호 공백', 'Pw를 입력하세요.', 'error');
        return false;
    }
    return true;
}

$('#keepId').change(function(){
    if($('#keepId').is(':checked')){
        if($('#userId').val() !== '') { Cookies.set('keepUserId', $('#userId').val(), { expires: 14 }); }
        showSAlert('주의', '공용 장소에서는 사용을 권장하지 않습니다.', 'warning');
    }
    else{ Cookies.remove('keepUserId'); }
});

$('#keepLogin').change(function(){
    if($('#keepLogin').is(':checked')){ showSAlert('주의', '자동 로그인은 가급적 개인 PC에서만 사용하시기 바랍니다.', 'warning'); }
});


$('#userId').keyup(function(){
    if($('#keepId').is(':checked')) {
        Cookies.set('keepUserId', $('#userId').val(), { expires: 14 });
    }
});


function clean4LoginInput() {
    $('#userId').val("");
    $('#userPw').val("");
}

function clean4SignUpInput() {
    $('#su_id').val("");
    $('#su_name').val("");
    $('#su_team').val("");
    $('#su_pw').val("");
    $('#su_pw_valid').val("");
}

function caps_lock(e) {
    var keyCode = 0;
    var shiftKey = false;
    keyCode = e.keyCode;
    shiftKey = e.shiftKey;
    if (((keyCode >= 65 && keyCode <= 90) && !shiftKey)
        || ((keyCode >= 97 && keyCode <= 122) && shiftKey)) {
        show_caps_lock();
        setTimeout('hide_caps_lock()', 3500);
    } else {
        hide_caps_lock();
    }
}

function show_caps_lock() { $('#capslock').show(); }
function hide_caps_lock() { $('#capslock').hide(); }

function initWithLoginView() {
    $('#goRight').on('click', function(){
        $('#slideBox').animate({
            'marginLeft' : '0'
        });
        $('.topLayer').animate({
            'marginLeft' : '100%'
        });
    });
    $('#goLeft').on('click', function(){
        if (window.innerWidth > 769){
            $('#slideBox').animate({
                'marginLeft' : '50%'
            });
        }
        else {
            $('#slideBox').animate({
                'marginLeft' : '20%'
            });
        }
        $('.topLayer').animate({
            'marginLeft': '0'
        });
    });
}

/* ====================== *
 *  Initiate Canvas       *
 * ====================== */
paper.install(window);
paper.setup(document.getElementById("canvas"));

// Paper JS Variables
var canvasWidth,
    canvasHeight,
    canvasMiddleX,
    canvasMiddleY;

var shapeGroup = new Group();

var positionArray = [];

function getCanvasBounds() {
    // Get current canvas size
    canvasWidth = view.size.width;
    canvasHeight = view.size.height;
    canvasMiddleX = canvasWidth / 2;
    canvasMiddleY = canvasHeight / 2;
    // Set path position
    var position1 = {
        x: (canvasMiddleX / 2) + 100,
        y: 100,
    };

    var position2 = {
        x: 200,
        y: canvasMiddleY,
    };

    var position3 = {
        x: (canvasMiddleX - 50) + (canvasMiddleX / 2),
        y: 150,
    };

    var position4 = {
        x: 0,
        y: canvasMiddleY + 100,
    };

    var position5 = {
        x: canvasWidth - 130,
        y: canvasHeight - 75,
    };

    var position6 = {
        x: canvasMiddleX + 80,
        y: canvasHeight - 50,
    };

    var position7 = {
        x: canvasWidth + 60,
        y: canvasMiddleY - 50,
    };

    var position8 = {
        x: canvasMiddleX + 100,
        y: canvasMiddleY + 100,
    };

    positionArray = [position3, position2, position5, position4, position1, position6, position7, position8];
};


/* ====================== *
 * Create Shapes          *
 * ====================== */
function initializeShapes() {
    // Get Canvas Bounds
    getCanvasBounds();

    var shapePathData = [
        'M231,352l445-156L600,0L452,54L331,3L0,48L231,352',
        'M0,0l64,219L29,343l535,30L478,37l-133,4L0,0z',
        'M0,65l16,138l96,107l270-2L470,0L337,4L0,65z',
        'M333,0L0,94l64,219L29,437l570-151l-196-42L333,0',
        'M331.9,3.6l-331,45l231,304l445-156l-76-196l-148,54L331.9,3.6z',
        'M389,352l92-113l195-43l0,0l0,0L445,48l-80,1L122.7,0L0,275.2L162,297L389,352',
        'M 50 100 L 300 150 L 550 50 L 750 300 L 500 250 L 300 450 L 50 100',
        'M 700 350 L 500 350 L 700 500 L 400 400 L 200 450 L 250 350 L 100 300 L 150 50 L 350 100 L 250 150 L 450 150 L 400 50 L 550 150 L 350 250 L 650 150 L 650 50 L 700 150 L 600 250 L 750 250 L 650 300 L 700 350 '
    ];

    for (var i = 0; i <= shapePathData.length; i++) {
        // Create shape
        var headerShape = new Path({
            strokeColor: 'rgba(255, 255, 255, 0.5)',
            strokeWidth: 2,
            parent: shapeGroup,
        });
        // Set path data
        headerShape.pathData = shapePathData[i];
        headerShape.scale(2);
        // Set path position
        headerShape.position = positionArray[i];
    }
};

initializeShapes();

/* ====================== *
 * Animation              *
 * ====================== */
view.onFrame = function paperOnFrame(event) {
    if (event.count % 4 === 0) {
        // Slows down frame rate
        for (var i = 0; i < shapeGroup.children.length; i++) {
            if (i % 2 === 0) {
                shapeGroup.children[i].rotate(-0.1);
            } else {
                shapeGroup.children[i].rotate(0.1);
            }
        }
    }
};

view.onResize = function paperOnResize() {
    getCanvasBounds();

    for (var i = 0; i < shapeGroup.children.length; i++) {
        shapeGroup.children[i].position = positionArray[i];
    }

    if (canvasWidth < 700) {
        shapeGroup.children[3].opacity = 0;
        shapeGroup.children[2].opacity = 0;
        shapeGroup.children[5].opacity = 0;
    } else {
        shapeGroup.children[3].opacity = 1;
        shapeGroup.children[2].opacity = 1;
        shapeGroup.children[5].opacity = 1;
    }
};