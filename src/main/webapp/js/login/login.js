var submit=document.getElementById('submit');
submit.ontouchstart=function () {
    submit.style.opacity='0.5';
}
submit.ontouchend=function () {
    submit.style.opacity='1';
}
var strcookie=document.cookie;
var arrcookie=strcookie.split(';');
var cookieLen=arrcookie.length;
var loginFailed=document.getElementById('failed');
for (var i = 0; i < cookieLen; i++) {
    var cookieInfos=arrcookie[i].split('=');
    if (cookieInfos[1]=='failed'){
        loginFailed.style.display='block';
    }else if (cookieInfos[1]=='succeed'){
        loginFailed.style.display='none';
    }
}