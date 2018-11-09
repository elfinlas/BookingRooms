/**
 * Created by MHLab on 08/11/2018.
 */

$(document).ready(function () {
    //init select2
    $('#select2Room').select2({ });
    $('#select2User').select2({ });

    $('#buttonViewAdd').hide(); //일반 저장 버튼 뷰 감춘다.

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
        timeFormat: '(A) H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', new Date());


    $('#endTime').timepicker({
        step: 5,
        scrollDefault: 'now',
        timeFormat: '(A) H:i',
        option:{ useSelect: true }
    }).timepicker('setTime', new Date());

    initWithCalendar();
});


//수정
function click4Modify() {
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
            //데이터를 추가하기 전에 두 데이터를 초기화 한다.
            // $('#select2Room').find('option').remove().end().append('<option selected>회의실을 선택하세요</option>');
            // $('#select2User').find('option').remove().end().append('<option value="add_user">외부인 추가</option>');

            //참석자 추가 부분은 등록해둔다.
            $('#select2User').append('<option value=add_user>외부인 추가</option>');

            //참석자 데이터 추가
            for (let i=0; i<accountList.length; i++) {
                if (String($('#select2User').val()).split(",").indexOf(String(accountList[i]['accountIdx'])) === -1) { //등록이 안되어 있는 경우
                    $('#select2User').append('<option value='+accountList[i]['accountIdx']+'>'+ accountList[i]['name'] + ' / ' + accountList[i]['teamName'] +'</option>');
                }
                $('#select2User').trigger('change');
            }

            //회의실 리스트
            for (let i=0; i<roomList.length; i++) {
                if (String($('#select2Room').val()).split(",").indexOf(String(roomList[i]['roomIdx'])) === -1) { //등록이 안되어 있는 경우
                    $('#select2Room').append('<option value='+roomList[i]['roomIdx']+'>'+ roomList[i]['name'] + '</option>');
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
function click4Delete() {
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


//Fullcalendar 초기화
function initWithCalendar() {
    $('#calendar').fullCalendar({
        locale: 'ko',
        aspectRatio: 2,
        contentHeight: "500",
        header    : {
            left  : 'prev,next',
            center: 'title',
            right : ''
        },
        buttonText: {
            today:    'today',
            month:    'month',
            week:     'week',
            day:      'day',
            list:     'list'
        },
        events: function (start, end, timezone, callback) { //일정을 가져오는 콜백
            //시작 날짜와 종료 날짜를 서버로 보내서 회의 데이터를 가져오는 메서드
            $('#loadingSpin4Calendar').show(); //일정 로딩을 열어준다.
            $.ajax({
                url: "/meeting/get/calendar/"+start.format("YYYY-MM-DD 00:00")+"/"+end.format("YYYY-MM-DD 00:00"),
                type: "GET",
                contentType: "application/json; charset=UTF-8",
                dataType: "text",
                processData: false,
                timeout:5000 //5 second timeout
            }).done(function(jqXHR, textStatus){ //가입 성공
                let resultCode = JSON.parse(jqXHR)['resultCode'];
                if (resultCode === 120) { //정상
                    let resultMsg = JSON.parse(jqXHR)['resultMsg'];
                    var events = []; //달력에 표시될 이벤트를 저장할 배열
                    for (let i=0; i<resultMsg.length; i++) {
                        let meetingData = resultMsg[i];

                        events.push({
                            idx:meetingData['meetingIdx'],
                            title:meetingData['title'],
                            start:meetingData['startDate'],
                            end:meetingData['endDate'],
                            meetData:meetingData,
                            allDay:false
                        });
                    }//end for
                }
                else { //정상적으로 값을 수신하지 못한 경우
                    showSAlert('서버 에러', '서버에서 문제가 발생하였습니다. ('+resultCode+')' , 'error');
                }
                $('#loadingSpin4Calendar').hide(); //일정 로딩을 감춘다.
                callback(events); //등록된 일정을 fullcalendar에 등록시킨다.
            }).fail(function(jqXHR, textStatus){
                $('#loadingSpin4Calendar').hide(); //일정 로딩을 감춘다.
                showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
                console.log("jqXHR = " + jqXHR);
                console.log("textStatus = " + textStatus);
            });


        },
        eventClick: function(calEvent, jsEvent, view) { //이벤트 클릭 함수
            showModal4Read(); //열람 시 모달을 초기화 해준다.
            //모달 데이터 셋업
            $('#meetingIdx').val(calEvent.idx);
            $('#titleInput').val(calEvent.title);
            $('#contentInput').val(calEvent.meetData.content);
            $('#isPrivate').attr("checked", calEvent.meetData.isPublic);
            $('#startDate').val(moment(calEvent.startDate).format('YYYY년 MM월 DD일'));
            $('#startTime').val(moment(calEvent.startDate).format('HH시 mm분'));
            $('#endTime').val(moment(calEvent.endDate).format('HH시 mm분'));

            //회의실
            $('#select2Room').append('<option value='+calEvent.meetData.room['roomIdx']+' selected>'+ calEvent.meetData.room['name']+'</option>');

            //참석인원
            $('#select2User').val(null).trigger('change'); //참석자를 초기화 해준다.
            let attendMember = calEvent.meetData['attendMemberList']; //참석 인원

            for (let i=0; i<attendMember.length; i++) { //참석인원 체크
                let companyMember = attendMember[i]['attendCompanyMember']; //내부 직원
                let outMember = attendMember[i]['attendOutMember']; //외부 추가
                if (companyMember !== null) { //내부 직원이 존재하는 경우
                    $('#select2User').append('<option value='+companyMember['accountIdx']+' selected>'+ companyMember.name + ' / ' + companyMember['teamName'] +'</option>');
                }

                if (outMember != null) { //외부 추가 직원이 존재하는 경우
                    $('#select2User').append('<option value=0 selected>'+ outMember +'</option>');
                }
            }

            $('#meetingDataModal').modal("toggle"); //다이얼로그 오픈
        }
    })
}