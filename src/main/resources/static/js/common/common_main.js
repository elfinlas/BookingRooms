/**
 * Created by MHLab on 21/10/2018..
 */

function initWithModal() {
    let nowDate = new Date();
    $('#meetingDataAddModal').on('shown.bs.modal', function() {
        $(document).off('focusin.modal');
    });

    //Date picker
    $('#startDate').datepicker({
        'format': 'yyyy년 mm월 dd일',
        'language': 'ko',
        'setDate': new Date(),
        'autoclose': true
    }).datepicker("setDate", nowDate);


    //Timepicker
    $('#startTime').timepicker({
        step: 5,
        scrollDefault: 'now',
        timeFormat: '(A) H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', nowDate);


    $('#endTime').timepicker({
        step: 5,
        scrollDefault: 'now',
        timeFormat: '(A) H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', nowDate);


    $('#select2Room').select2({  });

    $('#select2User').select2({
        placeholder: '회의 참석자를 선택하세요.',
        allowClear: true
    });
}

//회의 등록 Modal open
function click4AddMeetingData() {
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

            $('#meetingDataAddModal').modal("toggle");
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

//저장
function click4Save() {
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
    else if($('#select2Room').prop('selectedIndex') === 0) {
        showSAlert("회의실", "회의실을 선택하지 않았습니다.", "error");
        return;
    }
    else if(isNullValue($('#select2User').val())) {
        showSAlert('참석자', '참석자가 없습니다.', 'error');
        return;
    }
    else if ($('#startTime').val() === $('#endTime').val()) {
        showSAlert('시간', '시작과 종료시간이 동일합니다.', 'error');
        return;
    }

    startLoading();

    //회의 시작 시간과 종료시간을 가져오는 로직
    let today = new Date();
    let startTime = new Date(today.getFullYear(),today.getMonth(),today.getDate(),
        Number($('#startTime').val().split(' ')[1].split(":")[0]),Number($('#startTime').val().split(' ')[1].split(":")[1]),0,0);
    let endTime = new Date(today.getFullYear(),today.getMonth(),today.getDate(),
        Number($('#endTime').val().split(' ')[1].split(":")[0]),Number($('#endTime').val().split(' ')[1].split(":")[1]),0,0);

    let json = {'meetingIdx':0, 'title':$('#titleInput').val(), 'content':$('#contentInput').val(), 'startDate':startTime,
        'endDate':endTime, 'isPublic':$('#isPrivate').prop("checked"), 'room':{'roomIdx':$('#select2Room').val()}, 'attendUserList':$('#select2User').val() };

    $.ajax({
        url: "/meeting/add/data",
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(json),
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        let resultCode = JSON.parse(jqXHR)['resultCode'];
        if (resultCode === 110) { location.reload(); }
        else { showSAlert('에러 발생', '개발자에게 문의하세요.' + resultCode , 'error'); }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}


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
                        $("#select2User").append("<option value='"+addUserList[i]+'-customadd-0'+"' selected>"+addUserList[i]+"</option>");
                    }
                    $('#select2User').trigger('change');
                }
            }
            $("#select2User").append("<option value='"+'add_user'+"'>"+'외부인 추가'+"</option>"); //삭제한 유저를 다시 넣어준다.
        })
    }
});


