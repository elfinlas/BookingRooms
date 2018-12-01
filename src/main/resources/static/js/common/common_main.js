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


function logout() {
    swal({
        title: '로그아웃',
        text: "로그아웃 하시겠습니까?",
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '로그아웃',
        cancelButtonText: '취소'
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: "/users/logout",
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                dataType: "text",
                timeout:5000 //5 second timeout
            }).done(function(jqXHR, textStatus){ //가입 성공
                endLoading();
                var result = JSON.parse(jqXHR);
                if(result.resultCode === 30) { self.location = "/users/login/"+result.resultMsg; }
                else { showSAlert('로그아웃 실패', '로그아웃에 실패하였습니다. <br> 개발자에게 문의주세요.', 'error') }
            }).fail(function(jqXHR, textStatus){
                endLoading();
                showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error')
                console.log("jqXHR = " + jqXHR);
                console.log("textStatus = " + textStatus);
            });
        }
    });
}