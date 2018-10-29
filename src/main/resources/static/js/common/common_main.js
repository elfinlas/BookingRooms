/**
 * Created by MHLab on 21/10/2018..
 */

function initWithModal() {

    $('#meetingDataAddModal').on('shown.bs.modal', function() {
        $(document).off('focusin.modal');
    });

    //Date picker
    $('#startDate').datepicker({
        'format': 'yyyy년 mm월 dd일',
        'language': 'ko',
        'setDate': new Date(),
        'autoclose': true
    }).datepicker("setDate", new Date());

    //Timepicker
    $('#startTime').timepicker({
        step: 15,
        scrollDefault: 'now',
        timeFormat: '(A) H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', new Date());
    //$('#startTime').timepicker('option', { useSelect: true });

    $('#endTime').timepicker({
        step: 15,
        scrollDefault: 'now',
        timeFormat: '(A) H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', new Date());

    $('#select2Room').select2({  });

    $('#select2User').select2({
        placeholder: '회의 참석자를 선택하세요.',
        allowClear: true
    });
}

//회의 등록 Modal open
function click4AddMeetingData() {
    //회의 데이터를 가져온다.

    //
    $.ajax({
        url: "/meeting/get/add_res",
        type: "GET",
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        var resultCode = JSON.parse(jqXHR)['resultCode'];
        var msg = JSON.parse(jqXHR)['msg'];

        console.log("msg = "+ jqXHR)

        /*
        msg = {"resultCode":100,
        "resultMsg":
        {"accountList":[{"accountIdx":2,"id":"test1","name":"김철수","teamName":"개발 1실\n","createDate":"2018-10-21T06:18:46","updateDate":"2018-10-21T06:19:19"},
        {"accountIdx":3,"id":"test2","name":"홍길동\n","teamName":"개발 1실","createDate":"2018-10-21T06:21:19","updateDate":"2018-10-21T06:22:10"},
        {"accountIdx":4,"id":"test3","name":"안미영","teamName":"개발 2실","createDate":"2018-10-21T06:22:43","updateDate":"2018-10-21T06:22:40"},
        {"accountIdx":5,"id":"test4","name":"김미영","teamName":"디자인 1팀","createDate":"2018-10-21T06:23:12","updateDate":"2018-10-21T06:23:09"}],
        "roomList":[{"roomIdx":1,"name":"Room A","description":"테스트 회의실"},{"roomIdx":2,"name":"Room 2F B","description":"테스트 중앙 회의실"},
        {"roomIdx":3,"name":"B-Tower 19 A","description":"테스트 B타워 19층\n"}]}}
         */


        // if(resultCode === 1050) { self.location="/board/archive/list"+makeUrl4ListCriteria(); }
        // else { showSAlert('에러 발생', '개발자에게 문의하세요.' + resultCode , 'error'); }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });

    $('#meetingDataAddModal').modal("toggle");
}


function click4Save() {
    //Validation
    if($('#titleInput').val() === '') {
        showSAlert('제목 공백', '제목이 공백입니다.', 'error');
        return;
    }
    // else if($('#contentInput').val() === '') {
    //     showSAlert('내용 공백', '내용이 공백입니다.', 'error');
    //     return;
    // }
    // else if($('#startDate').val() === '') {
    //     showSAlert('날짜 공백', '회의 날짜가 공백입니다.', 'error');
    //     return;
    // }
    // else if($('#select2Room').prop('selectedIndex') === 0) {
    //     showSAlert("회의실", "회의실을 선택하지 않았습니다.", "error");
    //     return;
    // }
    // else if(isNullValue($('#select2User').val())) {
    //     showSAlert('참석자', '참석자가 없습니다.', 'error');
    //     return;
    // }
    else if ($('#startTime').val() === $('#endTime').val()) {
        showSAlert('시간', '시작과 종료시간이 동일합니다.', 'error');
        return;
    }

    var jsonObj = {'meetingIdx':0 };

    $.ajax({
        url: "----",
        type: "POST",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(jsonObj),
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        var resultCode = JSON.parse(jqXHR)['resultCode'];

        // if(resultCode === 1050) { self.location="/board/archive/list"+makeUrl4ListCriteria(); }
        // else { showSAlert('에러 발생', '개발자에게 문의하세요.' + resultCode , 'error'); }
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
                    var addUserList = result.value.split(',');
                    for (var i in addUserList) {
                        $("#select2User").append("<option value='"+addUserList[i]+'-customadd-0'+"' selected>"+addUserList[i]+"</option>");
                    }
                    $('#select2User').trigger('change');
                }
            }
            $("#select2User").append("<option value='"+'add_user'+"'>"+'외부인 추가'+"</option>"); //삭제한 유저를 다시 넣어준다.
        })
    }
});


