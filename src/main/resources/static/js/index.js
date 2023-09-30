function join() {
    var passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/,
        _name = $('#name').val(),
        _id= $('#id').val(),
        _password = $('#password').val(),
        param = null;

    if (_name == '') {
        alert('이름을 입력해주세요.');
        $('#name').focus();
        return false;
    }

    if (_id == '') {
        alert('아이디를 입력해주세요.');
        $('#id').focus();
        return false;
    }

    if (_password == '') {
        alert('패스워드를 입력해주세요.');
        $('#password').focus();
        return false;
    }

    if (_password != $('#password-check').val()) {
        alert('입력한 패스워드가 일치하지 않습니다.');
        $('#password-check').focus();
        return false;
    }

    if (!passwordRegex.test(_password)) {
        alert('패스워드는 영문, 숫자 조합 8자리 이상입니다.');
        $('#password').focus();
        return false;
    }

    param = {
        name : _name,
        id : _id,
        password : _password
    };

    $.ajax({
        url: '/join.json',
        type: 'POST',
        data: param,
        dataType: 'json',
        success:function(data) {
            alert(data.message);
            if (data.result) {
                window.location.href = data.page;
            } else {
                return false;
            }
        }
    })
}

function login() {

    var _id = $('#id').val(),
        _password = $('#password').val(),
        param = null;

    if (_id == '') {
        alert('아이디를 입력해주세요.');
        $('#id').focus();
        return false;
    }

    if (_password == '') {
        alert('패스워드를 입력해주세요.');
        $('#password').focus();
        return false;
    }

    param = {
        id : _id,
        password : _password
    }

    $.ajax({
        url: '/login.json',
        type: 'POST',
        data: param,
        dataType: 'json',
        success:function(data) {
            if (data.result) {
                window.location.href = data.page;
            } else {
                return false;
            }
        }
    })

}