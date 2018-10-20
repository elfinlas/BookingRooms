/**
 * Created by elfinlas on 2017. 11. 8..
 */



//Null을 체크하는 함수
//전달인자에 따라서 Bool 리턴함    / Null 일 경우 true   null이 아닌 경우 false
function isNullValue(str) {
    if (str == null) return true;
    if (str == "NaN") return true;
    if (new String(str).valueOf() == "undefined") return true;
    var chkStr = new String(str);
    if( chkStr.valueOf() == "undefined" ) return true;
    if (chkStr == null) return true;
    if (chkStr.toString().length == 0 ) return true;
    return false;
}


function validateEmail(email) {
    var regExp = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    return regExp.test(email);
}

function validatePhone(phone) {
    var regExp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
    return regExp.test(phone);
}

function validateUrl(str) {
    var regex = /^(((http(s?))\:\/\/)?)([0-9a-zA-Z\-]+\.)+[a-zA-Z]{2,6}(\:[0-9]+)?(\/\S*)?/;
    return regex.test(str);
}



function showSAlert(title, msg, type) {
    swal( title, msg, type )
}


//전화번호가 들어오면 자동으로 하이픈을 붙여주는 함수
function autoHypenPhone(targetInput) {
    var value = $(targetInput).val().replace(/[^0-9]/g, '');
    var tmp = '';
    if( value.length < 4){
        $(targetInput).val(value);
        return
    }
    else if(value.length < 7){
        tmp += value.substr(0, 3);
        tmp += '-';
        tmp += value.substr(3);
        $(targetInput).val(tmp)
        return
    }
    else if(value.length < 11){
        tmp += value.substr(0, 3);
        tmp += '-';
        tmp += value.substr(3, 3);
        tmp += '-';
        tmp += value.substr(6);
        $(targetInput).val(tmp)
        return
    }
    else{
        tmp += value.substr(0, 3);
        tmp += '-';
        tmp += value.substr(3, 4);
        tmp += '-';
        tmp += value.substr(7);
        $(targetInput).val(tmp)
        return
    }
    $(targetInput).val(value)
}


function getJsonFromUrl() {
    var query = location.search.substr(1);
    var result = {};
    query.split("&").forEach(function(part) {
        var item = part.split("=");
        result[item[0]] = decodeURIComponent(item[1]);
    });
    return result;
}

//Url 파라메터를 구하는 함수
function getUrlParam(paramName) {
    // 리턴값을 위한 변수 선언
    var returnValue;

    // 현재 URL 가져오기
    var url = location.href;

    // get 파라미터 값을 가져올 수 있는 ? 를 기점으로 slice 한 후 split 으로 나눔
    var parameters = (url.slice(url.indexOf('?') + 1, url.length)).split('&');

    // 나누어진 값의 비교를 통해 paramName 으로 요청된 데이터의 값만 return
    for (var i = 0; i < parameters.length; i++) {
        var varName = parameters[i].split('=')[0];
        if (varName.toUpperCase() == paramName.toUpperCase()) {
            returnValue = parameters[i].split('=')[1];
            return decodeURIComponent(returnValue);
        }
    }
}


function showAttribute(obj) {
    try {
        var data = '';

        for (var attr in obj) {
            if (typeof(obj[attr]) == 'string' || typeof(obj[attr]) == 'number') {
                data = data + 'Attr Name : ' + attr + ', Value : ' + obj[attr] + ', Type : ' + typeof(obj[attr]) + '\n';
            } else {
                data = data + 'Attr Name : ' + attr + ', Type : ' + typeof(obj[attr]) + '\n';
            }
        }
        console.log("data = " + data)
        //document.getElementById('attr_show').value = data;
    } catch (e) {
        alert(e.message);
    }
}


function bytesToSize(bytes) {
    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    if (bytes == 0) return 'n/a';
    var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
    if (i == 0) return bytes + ' ' + sizes[i];
    return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
}
