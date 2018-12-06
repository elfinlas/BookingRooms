/**
 * Created by MHLab on 04/12/2018..
 */


$(document).ready(function () {

});

function click4Option(idx, name, teamName) {
    $('#accountIdx').val(idx); //초기화 해준다.
    $('#team_name').val(teamName);
    $('#user_name').val(name);
    $('#userControlModal').modal("toggle");
}


function click4PwReset() {
    showAlert('암호 초기화','계정 암호를 초기화하겠습니까?', '초기화', '/user/update/account/reset/pw/'+$('#accountIdx').val());
}


function click4UserDelete() {
    showAlert('계정 삭제','계정을 삭제하겠습니까?', '삭제', '/users/delete/account/delete/' + $('#accountIdx').val());
}

function click4UserInfo() {
    let jsonObj = {'signUpIdx':$('#accountIdx').val(),  'signUpName':$('#user_name').val(), 'teamName':$('#team_name').val()}; //Create JSON Object
    startLoading();
    $.ajax({
        url: '/users/update/account/info',
        type: 'POST',
        data: JSON.stringify(jsonObj),
        contentType: 'application/json; charset=UTF-8',
        dataType: 'text',
        timeout:5000 //5 second timeout
    }).done(function(jqXHR, textStatus){ //삭제 성공
        endLoading();
        //reset
        $('#accountIdx').val('');
        $('#team_name').val('');
        $('#user_name').val('');

        if(JSON.parse(jqXHR).resultCode === 60) { location.reload() }
        else if(JSON.parse(jqXHR).resultCode === -60) {
            showSAlert("변경 실패","사용자 정보 변경에 실패하였습니다.","error");
        }
    }).fail(function(jqXHR, textStatus){
        endLoading();
        showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error')
        console.log("jqXHR = " + jqXHR);
        console.log("textStatus = " + textStatus);
    });
}


function showAlert(title, msg, btMsg, url) {
    swal({
        title: title,
        text: msg,
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: btMsg,
        cancelButtonText: '취소'
    }).then(function (result) {
        if (result.value) {
            startLoading();
            $.ajax({
                url: url,
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                dataType: "text",
                timeout:5000 //5 second timeout
            }).done(function(jqXHR, textStatus){ //삭제 성공
                endLoading();
                //reset
                $('#accountIdx').val('');
                $('#team_name').val('');
                $('#user_name').val('');

                if(JSON.parse(jqXHR).resultCode === 40 || JSON.parse(jqXHR).resultCode === 50) { location.reload() }
                else if(JSON.parse(jqXHR).resultCode === -40) {
                    showSAlert("초기화 실패","초기화에 실패하였습니다.","error");
                }
                else if(JSON.parse(jqXHR).resultCode === -50) {
                    showSAlert("삭제 실패","삭제에 실패하였습니다.","error");
                }
            }).fail(function(jqXHR, textStatus){
                endLoading();
                showSAlert('서버 에러', '서버에서 문제가 발생하였습니다.', 'error')
                console.log("jqXHR = " + jqXHR);
                console.log("textStatus = " + textStatus);
            });
        }
    })
}