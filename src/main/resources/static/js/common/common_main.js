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




