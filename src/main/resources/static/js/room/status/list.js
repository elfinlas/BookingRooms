/**
 * 관리
 * 회의실 현황
 *
 * Created by MHLab on 06/11/2018.
 */


$(document).ready(function () {

});


//회의실 조회
function click4Item(idx) {
    $.ajax({
        url: "/room/get/data/"+idx,
        type: "GET",
        contentType: "application/json; charset=UTF-8",
        dataType: "text",
        processData: false,
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //가입 성공
        endLoading();
        let resultCode = JSON.parse(jqXHR)['resultCode'];
        let room = JSON.parse(jqXHR)['resultMsg'];
        if (resultCode === 200) {
            modalModeChange('read'); // 열람

            $('#roomIdx').val(room['roomIdx']);
            $('#roomName').val(room['name']);
            $('#roomDescription').val(room['description']);
            $("#roomName").attr("readonly",true);
            $("#roomDescription").attr("readonly",true);
            $('#roomDataAddModal').modal("toggle");
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


//회의실 등록
function click4SaveUpdate() {
    //Validation
    if(isNullValue($('#roomName').val())) {
        showSAlert('이름 공백', '이름이 공백입니다.', 'error');
        return;
    }

    startLoading();
    let url = ''; //url
    let json = {'name':$('#roomName').val(), 'description':$('#roomDescription').val()};

    if (isNullValue($('#roomIdx').val())) { //등록용
        url = '/room/add/data';
    }
    else { //수정
        url = '/room/update/data';
        json['roomIdx'] = $('#roomIdx').val();
    }

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
        if (resultCode === 201 || resultCode === 202 ) { location.reload(); }
        else { showSAlert('에러 발생', '개발자에게 문의하세요.' + resultCode , 'error'); }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error');
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}


//삭제처리
function click4Delete() {
    swal({
        title: '회의실 데이터 삭제',
        html: "회의실 데이터를 삭제하시겠습니까?",
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
                url: "/room/delete/data/"+$('#roomIdx').val(),
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                dataType: "text",
                processData: false,
                timeout:5000 //5 second timeout
            }).done(function(jqXHR, textStatus){ //가입 성공
                endLoading();
                let resultCode = JSON.parse(jqXHR)['resultCode'];
                if (resultCode === 203) { location.reload(); }
                else if (resultCode === -203) { showSAlert('삭제 불가', '최소 1개 이상의 회의실은 존재해야 합니다.' , 'error'); }
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

//수정처리
function click4Modify() {
    $('#roomDataAddModal').modal("hide");
    setTimeout(function(){
        modalModeChange('update');
        $('#roomDataAddModal').modal("toggle");
    },400);
}


//회의실 등록 다이얼로그
function click4Add() {
    modalModeChange('add');
    $('#roomDataAddModal').modal("toggle");
}


//회의 데이터 모달 처리 함수
function modalModeChange(mode) {
    if (mode === 'add') { //등록
        //초기화
        $('#roomIdx').val('');
        $('#roomName').val('');
        $('#roomDescription').val('');
        $("#roomName").removeAttr("readonly");
        $("#roomDescription").removeAttr("readonly");

        $('.box-title').html('회의실 정보 등록');
        $('#buttonView4Add').show();
        $('#buttonViewRead').hide();
    }
    else if (mode === 'update') { //수정
        $("#roomName").removeAttr("readonly");
        $("#roomDescription").removeAttr("readonly");

        $('.box-title').html('회의실 정보 수정');
        $('#buttonView4Add').show();
        $('#buttonViewRead').hide();
    }
    else { //수정
        $('.box-title').html('회의실 정보');
        $('#buttonView4Add').hide();
        $('#buttonViewRead').show();
    }

}