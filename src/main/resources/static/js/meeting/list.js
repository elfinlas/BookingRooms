/**
 * 회의 전체 리스트 처리 js
 *
 * Created by MHLab on 13/11/2018..
 */

$(document).ready(function () {
    initWithModal(); //모달 초기화
});

function click4Item(index) {
    startLoading();
    $.ajax({
        url: '/meeting/get/data/'+index,
        type: "GET",
        contentType: "application/json; charset=UTF-8",
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        let resultCode = JSON.parse(jqXHR)['resultCode'];
        if (resultCode === 113) {
            showModal4Read(); //열람 시 모달을 초기화 해준다.
            let resultMsg = JSON.parse(jqXHR)['resultMsg'];

            //모달 데이터 셋업
            $('#meetingIdx').val(resultMsg['meetingIdx']);
            $('#titleInput').val(resultMsg['title']);
            $('#contentInput').val(resultMsg['content']);
            $('#isPrivate').attr("checked", !resultMsg['isPublic']);
            $('#startDate').val(moment(resultMsg['startDate']).format('YYYY년 MM월 DD일'));
            $('#startTime').val(moment(resultMsg['startDate']).format('HH:mm'));
            $('#endTime').val(moment(resultMsg['endDate']).format('HH:mm'));

            //회의실
            $('#select2Room').append('<option value='+resultMsg['room']['roomIdx']+' selected>'+ resultMsg['room']['name']+'</option>');

            //참석인원
            $('#select2User').val(null).trigger('change'); //참석자를 초기화 해준다.
            let attendMember = resultMsg['attendMemberList']; //참석 인원

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
        else { showSAlert('에러 발생', '개발자에게 문의하세요.' + resultCode , 'error'); }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}
