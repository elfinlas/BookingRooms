/**
 * 회의 현황 조회
 *
 * Created by MHLab on 06/11/2018.
 */

//전역변수
var timetable;

$(document).ready(function () {
    let now = new Date();
    //Date picker
    $('#searchDate').datepicker({
        'format': 'yyyy년 mm월 dd일',
        'language': 'ko',
        'setDate': new Date(),
        'autoclose': true
    }).datepicker("setDate", now);

    $('#title_date').text(moment(now).format('YYYY년 MM월 DD일'));

    getServerData('a', now);
});


function click4Search() {
    $('#title_date').text(moment($('#searchDate').datepicker('getDate')).format('YYYY년 MM월 DD일'));
    getServerData('a', $('#searchDate').datepicker('getDate'))
}


//회의 정보를 가져오는 메서드
function getServerData(room, date) {
    startLoading();
    $.ajax({
        url: '/room/get/data/meeting/time/' + room + '/'+ moment(date).format('YYYY-MM-DD'),
        type: 'GET',
        contentType: 'application/json; charset=UTF-8',
        dataType: 'text',
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();

        if (JSON.parse(jqXHR)['resultCode'] === 210) { //성공한 경우
            let resultMsg = JSON.parse(jqXHR)['resultMsg']; //데이터
            initWithTimeTable(); //Timetable을 초기화 수행

            //회의실 데이터
            timetable.addLocations(resultMsg['roomList'].map(data => data['name']));

            for (let i=0; i<resultMsg['meetingList'].length; i++) {
                //시작 시간과 종료 시간을 가져오는 로직
                let tmpStartTime = resultMsg['meetingList'][i]['startDate'];
                let tmpEndTime = resultMsg['meetingList'][i]['endDate'];

                let startTime = new Date(Number(tmpStartTime.split('T')[0].split('-')[0]), Number(tmpStartTime.split('T')[0].split('-')[1]), Number(tmpStartTime.split('T')[0].split('-')[2]),
                    Number(tmpStartTime.split('T')[1].split(':')[0]), Number(tmpStartTime.split('T')[1].split(':')[1]), 0);

                let endTime = new Date(Number(tmpEndTime.split('T')[0].split('-')[0]), Number(tmpEndTime.split('T')[0].split('-')[1]), Number(tmpEndTime.split('T')[0].split('-')[2]),
                    Number(tmpEndTime.split('T')[1].split(':')[0]), Number(tmpEndTime.split('T')[1].split(':')[1]), 0);

                let option = {url:'#', class:'',
                    data:{
                        meetingIdx: resultMsg['meetingList'][i]['meetingIdx'], meetingTitle: resultMsg['meetingList'][i]['title'],
                        meetingContent: resultMsg['meetingList'][i]['content'], meetingStart: startTime, meetingEnd: endTime
                    },
                    onClick: function(event, timetable, clickEvent) {
                        showAttribute(event['options']['data']);

                        $('#titleInput').val(resultMsg['meetingList'][i]['title']);
                        $('#contentInput').val(resultMsg['meetingList'][i]['content']);
                        $('#startDate').val(moment(startTime).format('YYYY년 MM월 DD일'));
                        $('#startTime').val(moment(startTime).format('HH시 mm분'));
                        $('#endTime').val(moment(endTime).format('HH시 mm분'));

                        $('#showRoomInMeetingModal').modal("toggle");
                    }
                };

                //등록한 정보를 토대로 정보 등록
                timetable.addEvent(resultMsg['meetingList'][i]['title'], resultMsg['meetingList'][i]['room']['name'], startTime, endTime, option);
            }
            var renderer = new Timetable.Renderer(timetable);
            renderer.draw('.timetable'); // any css selector
        }
        else { showSAlert('에러 발생', '개발자에게 문의하세요.' + resultCode , 'error'); }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}


//Timetable 초기화 함수
function initWithTimeTable() {
    timetable = new Timetable();
    timetable.setScope(0, 23);
}