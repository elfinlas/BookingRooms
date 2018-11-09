/**
 * Meeting Modal을 제어하는 JS
 *
 * Created by MHLab on 09/11/2018.
 */


//모달이 닫을 때 이벤트 처리 메서드
$('#meetingDataModal').on('hidden.bs.modal', function () {

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
