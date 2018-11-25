/**
 * Meeting Modal을 제어하는 JS
 *
 * Created by MHLab on 09/11/2018.
 */

//모달에 필요한 데이터를 초기화 해주는 함수
function initWithModal() {
    //init select2
    $('#select2Room').select2({ });
    $('#select2User').select2({ });

    //Date picker
    $('#startDate').datepicker({
        'format': 'yyyy년 mm월 dd일',
        'language': 'ko',
        'setDate': new Date(),
        'autoclose': true
    }).datepicker("setDate", new Date());
    $('#startDate').prop('disabled', true); //picker가 뜨는 것을 막아준다.


    //Timepicker
    $('#startTime').timepicker({
        step: 5,
        scrollDefault: 'now',
        // timeFormat: '(A) H:i',
        timeFormat: 'H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', new Date());

    $('#endTime').timepicker({
        step: 5,
        scrollDefault: 'now',
        timeFormat: 'H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', new Date());
}

//모달이 닫을 때 이벤트 처리 메서드
$('#meetingDataModal').on('hidden.bs.modal', function () {

});

//모달 열릴 때 이벤트
$('#meetingDataModal').on('shown.bs.modal', function() {
    $(document).off('focusin.modal');
});


//새로운 데이터 등록
function showMoal4New() {
    $('.box-title').text('회의 데이터 등록');
    $('#saveUpdateBt').text('등록하기');

    $('#select2Room').find('option').remove().end().append('<option selected>회의실을 선택하세요</option>');
    $('#select2User').find('option').remove().end().append('<option value="add_user">외부인 추가</option>');

    $('#titleInput').prop('readonly', false);
    $('#isPrivate').prop('disabled', false);
    $('#contentInput').prop('readonly', false);

    $('#startDate').prop('readonly', false);
    $('#startDate').prop('disabled', false);
    $('#startTime').prop('readonly', false);
    $('#endTime').prop('readonly', false);

    $('#select2Room').prop('disabled', false);
    $('#select2User').prop('disabled', false);

    //값 초기화
    $('#meetingIdx').val('');
    $('#titleInput').val('');
    $('#isPrivate').prop("checked", false);
    $('#contentInput').val('');
    $('#startDate').val(moment(new Date()).format('YYYY년 MM월 DD일'));
    $('#startTime').val(moment(new Date()).format('HH:mm'));
    $('#endTime').val(moment(new Date()).format('HH:mm'));

    $('#buttonViewAdd').show();
    $('#buttonViewRead').hide();
}

//데이터 갱신 부분
function showModal4Update() { //새로운 데이터 갱신
    $('.box-title').text('회의 데이터 수정');
    $('#saveUpdateBt').text('수정하기');

    $('#titleInput').prop('readonly', false);
    $('#isPrivate').prop('disabled', false);
    $('#contentInput').prop('readonly', false);

    $('#startDate').prop('readonly', false);
    $('#startDate').prop('disabled', false);
    $('#startTime').prop('readonly', false);
    $('#endTime').prop('readonly', false);

    $('#select2Room').prop('disabled', false);
    $('#select2User').prop('disabled', false);

    $('#buttonViewAdd').show();
    $('#buttonViewRead').hide();
}

//데이터 읽기
function showModal4Read() {
    $('.box-title').text('회의 데이터 열람');

    //데이터를 입력 가능하게끔 처리
    $('#titleInput').prop('readonly', true);
    $('#isPrivate').prop('disabled', true);
    $('#contentInput').prop('readonly', true);

    $('#startDate').prop('readonly', true);
    $('#startDate').prop('disabled', true);
    $('#startTime').prop('readonly', true);
    $('#endTime').prop('readonly', true);

    $('#select2Room').prop('disabled', true);
    $('#select2User').prop('disabled', true);

    $('#buttonViewAdd').hide();
    $('#buttonViewRead').show();
}

//회의 추가
function click4AddMeetingData() {
    showMoal4New(); //
    //모달을 띄우기 전 서버로부터 데이터를 수신한다.
    $.ajax({
        url: "/meeting/get/add_res",
        type: "GET",
        contentType: "application/json; charset=UTF-8",
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        let resultCode = JSON.parse(jqXHR)['resultCode'];
        let accountList = JSON.parse(jqXHR)['resultMsg']['accountList'];
        let roomList = JSON.parse(jqXHR)['resultMsg']['roomList'];

        if (resultCode === 100) {
            //데이터를 추가하기 전에 두 데이터를 초기화 한다.
            $('#select2Room').find('option').remove().end().append('<option selected>회의실을 선택하세요</option>');
            $('#select2User').find('option').remove().end().append('<option value="add_user">외부인 추가</option>');

            //참석자 데이터 추가
            for (let i=0; i<accountList.length; i++) {
                $('#select2User').append('<option value='+accountList[i]['accountIdx']+'>'+ accountList[i]['name'] + ' / ' + accountList[i]['teamName'] +'</option>');
                $('#select2User').trigger('change');
            }

            //회의실 리스트
            for (let i=0; i<roomList.length; i++) {
                $('#select2Room').append('<option value='+roomList[i]['roomIdx']+'>'+ roomList[i]['name'] + '</option>');
                $('#select2Room').trigger('change');
            }

            $('#meetingDataModal').modal("toggle"); //다이얼로그 오픈
        }
        else { //정상적으로 값을 수신하지 못한 경우
            showSAlert('서버 에러', '서버에서 문제가 발생하였습니다. ('+resultCode+')' , 'error');
        }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}


//수정 버튼
function click4ModifyBt() {
    startLoading();
    //모달을 띄우기 전 서버로부터 데이터를 수신한다.
    $.ajax({
        url: "/meeting/get/add_res",
        type: "GET",
        contentType: "application/json; charset=UTF-8",
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        let resultCode = JSON.parse(jqXHR)['resultCode'];
        let accountList = JSON.parse(jqXHR)['resultMsg']['accountList'];
        let roomList = JSON.parse(jqXHR)['resultMsg']['roomList'];

        if (resultCode === 100) {
            let beforeRoomList = $('#select2Room').val();
            let beforeCompanyMember = $('#select2User').val();
            let beforeOutMember = []; //회의 참석인원을 담는 배열
            $('#select2User option:selected').each(function() {
                if ($(this).val() === '0') { beforeOutMember.push($(this).text()); } //새로 추가된 사용자인 경우
            });

            //데이터를 추가하기 전에 두 데이터를 초기화 한다.
            $('#select2Room').find('option').remove().end();//.append('<option selected>회의실을 선택하세요</option>');
            $('#select2User').find('option').remove().end().append('<option value="add_user">외부인 추가</option>');

            //참석자 데이터 추가 (사내 직원)
            for (let i=0; i<accountList.length; i++) {
                if (String(beforeCompanyMember).split(",").indexOf(String(accountList[i]['accountIdx'])) === -1) { //등록이 안되어 있는 경우
                    $('#select2User').append('<option value='+accountList[i]['accountIdx']+'>'+ accountList[i]['name'] + ' / ' + accountList[i]['teamName'] +'</option>');
                }
                else {
                    $('#select2User').append('<option selected value='+accountList[i]['accountIdx']+'>'+ accountList[i]['name'] + ' / ' + accountList[i]['teamName'] +'</option>');
                }
            }

            //참석자 데이터 추가 (사외 직원)
            for (let i=0; i<beforeOutMember.length; i++) {
                $('#select2User').append('<option selected value='+beforeOutMember[i]+'>'+ beforeOutMember[i] +'</option>');
            }
            $('#select2User').trigger('change');


            //회의실 리스트
            for (let i=0; i<roomList.length; i++) {
                if (String(beforeRoomList).split(",").indexOf(String(roomList[i]['roomIdx'])) === -1) { //등록이 안되어 있는 경우
                    $('#select2Room').append('<option value='+roomList[i]['roomIdx']+'>'+ roomList[i]['name'] + '</option>');
                }
                else {
                    $('#select2Room').append('<option selected value='+roomList[i]['roomIdx']+'>'+ roomList[i]['name'] + '</option>');
                }
                $('#select2Room').trigger('change');
            }

            //수정 처리 부분에 따른 작업
            showModal4Update();
        }
        else { //정상적으로 값을 수신하지 못한 경우
            showSAlert('서버 에러', '서버에서 문제가 발생하였습니다. ('+resultCode+')' , 'error');
        }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}

//삭제
function click4DeleteBt() {
    swal({
        title: '회의 데이터 삭제',
        html: "회의 데이터를 삭제하시겠습니까?",
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '삭제',
        cancelButtonText: '취소'
    }).then(function (result) {
        if (result.value) {
            startLoading(); //처리 결과가 길어지기 때문에
            $.ajax({
                url: "/meeting/delete/data/"+$('#meetingIdx').val(),
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                dataType: "text",
                processData: false,
                timeout:5000 //5 second timeout
            }).done(function(jqXHR, textStatus){ //가입 성공
                endLoading();
                let resultCode = JSON.parse(jqXHR)['resultCode'];
                if (resultCode === 112) { location.reload(); } //////////////////
                else { //정상적으로 값을 수신하지 못한 경우
                    showSAlert('서버 에러', '서버에서 문제가 발생하였습니다. ('+resultCode+')' , 'error');
                }
            }).fail(function(jqXHR, textStatus){
                endLoading();
                showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
                console.log("jqXHR = " + jqXHR);
                console.log("textStatus = " + textStatus);
            });
        }
        else if (result.dismiss === 'cancel') { } //Cancel
    });
}


//저장 또는 업데이트
function saveUpdateData() {
    let url = '';
    let isNewData = isNullValue($('#meetingIdx').val());
    let meetingIdx = isNewData===true?0:$('#meetingIdx').val();

    //Validation
    if($('#titleInput').val() === '') {
        showSAlert('제목 공백', '제목이 공백입니다.', 'error');
        return;
    }
    else if($('#contentInput').val() === '') {
        showSAlert('내용 공백', '내용이 공백입니다.', 'error');
        return;
    }
    else if($('#startDate').val() === '') {
        showSAlert('날짜 공백', '회의 날짜가 공백입니다.', 'error');
        return;
    }
    else if ($('#startTime').val() === $('#endTime').val()) {
        showSAlert('시간', '시작과 종료시간이 동일합니다.', 'error');
        return;
    }
    else if ($('#startTime').val() === '') {
        showSAlert('시작시간', '시작 시간이 공백입니다.', 'error');
        return;
    }
    else if ($('#endTime').val() === '') {
        showSAlert('종료시간', '종료 시간이 공백입니다.', 'error');
        return;
    }

    if (isNewData) { //새로 데이터를 저장하는 경우 추가 검증
        url = '/meeting/add/data';
        if($('#select2Room').prop('selectedIndex') === 0) {
            showSAlert("회의실", "회의실을 선택하지 않았습니다.", "error");
            return;
        }
        else if(isNullValue($('#select2User').val())) {
            showSAlert('참석자', '참석자가 없습니다.', 'error');
            return;
        }
    }
    else { url = '/meeting/update/data'; } //업데이트의 경우

    //회의 시작 시간과 종료시간을 가져오는 로직
    let tmp_start_date = dateParse($('#startDate').val().replace(/[^0-9]/g,"")); //시작일 Date 객체를 가져온다.
    let startTime = new Date(Date.UTC(tmp_start_date.getFullYear(),tmp_start_date.getMonth(),tmp_start_date.getDate(),
        Number($('#startTime').val().split(":")[0]),Number($('#startTime').val().split(":")[1]),0));
    let endTime = new Date(Date.UTC(tmp_start_date.getFullYear(),tmp_start_date.getMonth(),tmp_start_date.getDate(),
        Number($('#endTime').val().split(":")[0]),Number($('#endTime').val().split(":")[1]),0));

    startLoading();

    let attendUserList = []; //회의 참석인원을 담는 배열
    $('#select2User option:selected').each(function() {
        if ($(this).val() === '0') { attendUserList.push($(this).text()); } //새로 추가된 사용자인 경우
        else { attendUserList.push($(this).val()); } //회사 인원의 경우
    });

    let json = {'meetingIdx':meetingIdx, 'title':$('#titleInput').val(), 'content':$('#contentInput').val(), 'startDate':startTime,
        'endDate':endTime, 'isPublic':$('#isPrivate').prop("checked"), 'room':{'roomIdx':$('#select2Room').val()},
        'attendUserList':attendUserList };

    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(json),
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        let resultCode = JSON.parse(jqXHR)['resultCode'];
        if (resultCode === 110 || resultCode === 111) { location.reload(); }
        else if (resultCode === -110) { showSAlert('등록 불가', '선택한 회의실에 시작시간이 동일한 회의가 있습니다.', 'error'); }
        else if (resultCode === -111) { showSAlert('등록 불가', '선택한 회의실에 종료시간이 동일한 회의가 있습니다.', 'error'); }
        else if (resultCode === -112) { showSAlert('등록 불가', '선택한 회의실에 시작시간이 중복된 회의가 있습니다.', 'error'); }
        else if (resultCode === -113) { showSAlert('등록 불가', '선택한 회의실에 종료시간이 중복된 회의가 있습니다.', 'error'); }
        else if (resultCode === -114) { showSAlert('등록 불가', '선택한 회의실에 이미 중복된 회의가 있습니다.', 'error'); }
        else { showSAlert('에러 발생', '개발자에게 문의하세요.' + resultCode , 'error'); }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}


//외부인 추가 처리 로직
$('#select2User').on("select2:selecting", function(e) {
    if(e.params.args.data.text === '외부인 추가') {
        $("#select2User option[value='" + 'add_user'+ "']").remove(); //선택된 기타는 제거해준다.
        swal({
            title: '사용자 추가',
            html : '등록되지 않은 다른 사용자가 있다면 입력하세요.' + '<br>' + '다수 인원인 경우 <b>꼭</b> <b>,(쉼표)</b>로 구분하세요.',
            input: 'text',
            inputPlaceholder: '',
            showCancelButton: true,
            inputValidator: (value) => { return !value && '공백입니다.' }
        }).then(function (result) {
            if (result.value) {
                if(result.value === '외부인 추가') { showSAlert("사용 불가","사용이 불가능한 이름입니다.","error"); }
                else {
                    let addUserList = result.value.split(',');
                    for (let i in addUserList) {
                        $("#select2User").append("<option value='"+addUserList[i]+"' selected>"+addUserList[i]+"</option>");
                        //$("#select2User").append("<option value='"+addUserList[i]+'-customadd-0'+"' selected>"+addUserList[i]+"</option>");
                    }
                    $('#select2User').trigger('change');
                }
            }
            $("#select2User").append("<option value='"+'add_user'+"'>"+'외부인 추가'+"</option>"); //삭제한 유저를 다시 넣어준다.
        })
    }
});


//yyyyMMdd 형식으로 온 문자열을 Date로 변환 반환해주는 함수
function dateParse(str) {
    var y = str.substr(0, 4);
    var m = str.substr(4, 2);
    var d = str.substr(6, 2);
    return new Date(y,m-1,d);
}

