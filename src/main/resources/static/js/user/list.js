/**
 * Created by MHLab on 04/12/2018..
 */


$(document).ready(function () {

});

function click4Option(idx, name) {
    $('#accountIdx').val(idx); //초기화 해준다.
    $('#userControlModal').modal("toggle");
    //alert("id = " + idx + 'name = ' + name);
}


function click4PwReset() {
    showAlert('암호 초기화','계정 암호를 초기화하겠습니까?', '초기화', '/user/reset/pw/'+$('#accountIdx').val());
}


function click4UserDelete() {
    showAlert('계정 삭제','계정을 삭제하겠습니까?', '삭제', '/users/delete/' + $('#accountIdx').val());
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
                $('#accountIdx').val(''); //reset

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