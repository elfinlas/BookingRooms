/**
 * 회의 현황 조회
 *
 * Created by MHLab on 06/11/2018.
 */

$(document).ready(function () {

    //@GetMapping("get/data/meeting/time")

    let json = {'roomList':[1,2,3,4,5,6]};

    ////@GetMapping("get/data/meeting/time")
    $.ajax({
        url: '/room/get/data/meeting/time',
        type: 'GET',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(json),
        dataType: 'text',
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



    var options = {
        url: '#', // makes the event clickable
        class: 'vip', // additional css class
        data: { // each property will be added to the data-* attributes of the DOM node for this event
            id: 4,
            ticketType: 'VIP'
        },
        onClick: function(event, timetable, clickEvent) {
            showAttribute(event['options']['data']);
        } // custom click handler, which is passed the event object and full timetable as context
    };



    var timetable = new Timetable();
    timetable.setScope(0, 23);
    timetable.addLocations(['Silent Disco', 'Nile', 'Len Room', 'Maas Room']);
    timetable.addEvent('Frankadelic', 'Nile', new Date(2018,11,6,10,45), new Date(2018,11,6,18,45), options);
    var renderer = new Timetable.Renderer(timetable);
    renderer.draw('.timetable'); // any css selector
});