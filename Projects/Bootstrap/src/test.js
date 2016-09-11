
var func = function (he) {
    var str = prompt('What is your name?', '');
    document.getElementById('confirm').innerHTML = str + he;
    document.getElementById('div').innerHTML = str + document.getElementById('but').innerHTML;
};

var add = function (da, ge, gh) {
    var int = da + ge;
    document.getElementById(gh).innerHTML = int;
};
