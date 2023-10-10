function join() {
    var passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/,
        phoneNumberRegex = /^\d{4}$/,
        _name = $('#name').val(),
        _nickName = $('#nickName').val(),
        _id= $('#id').val(),
        _password = $('#password').val(),
        _emailId = $('#emailId').val(),
        _fPhoneNumber = $('#phoneNumber-first').val(),
        _mPhoneNumber = $('#phoneNumber-middle').val(),
        _lPhoneNumber = $('#phoneNumber-last').val(),
        param = null;

    if (_name == '') {
        alert('이름을 입력해주세요.');
        $('#name').focus();
        return false;
    }

    if (_nickName == '') {
        alert('닉네임을 입력해주세요.');
        $('#nickName').focus();
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

    if (_mPhoneNumber == '' || _lPhoneNumber == '') {
        alert('핸드폰 번호를 입력해주세요.');
        if (_mPhoneNumber == '') {
            $('#phoneNumber-middle').focus();
            return false;
        } else {
            $('#phoneNumber-last').focus();
            return false;
        }
    }

    if (!phoneNumberRegex.test(_mPhoneNumber)) {
        alert('올바른 4자리 번호를 입력해주세요.');
        $('#phoneNumber-middle').focus();
        return false;
    } else if (!phoneNumberRegex.test(_lPhoneNumber)) {
        alert('올바른 4자리 번호를 입력해주세요.');
        $('#phoneNumber-last').focus();
        return false;
    }

    param = {
        name : _name,
        nickName : _nickName,
        id : _id,
        password : _password,
        emailId : _emailId,
        phoneNumber : _fPhoneNumber + _mPhoneNumber + _lPhoneNumber
    };

    $.ajax({
        url: '/user/join',
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
        url: '/login',
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

function findUser() {
    var phoneNumberRegex = /^\d{4}$/,
        _name = $('#name').val(),
        _fPhoneNumber = $('#phoneNumber-first').val(),
        _mPhoneNumber = $('#phoneNumber-middle').val(),
        _lPhoneNumber = $('#phoneNumber-last').val(),
        param = null;

    if (_name == '') {
        alert('이름을 입력해주세요.');
        $('#name').focus();
        return false;
    }

    if (_mPhoneNumber == '' || _lPhoneNumber == '') {
        alert('핸드폰 번호를 입력해주세요.');
        if (_mPhoneNumber == '') {
            $('#phoneNumber-middle').focus();
            return false;
        } else {
            $('#phoneNumber-last').focus();
            return false;
        }
    }

    if (!phoneNumberRegex.test(_mPhoneNumber)) {
        alert('올바른 4자리 번호를 입력해주세요.');
        $('#phoneNumber-middle').focus();
        return false;
    } else if (!phoneNumberRegex.test(_lPhoneNumber)) {
        alert('올바른 4자리 번호를 입력해주세요.');
        $('#phoneNumber-last').focus();
        return false;
    }

    param = {
        name : _name,
        phoneNumber : _fPhoneNumber + _mPhoneNumber + _lPhoneNumber
    };

    $.ajax({
        url: '/user/find',
        type: 'POST',
        data: param,
        dataType: 'json',
        success:function(data) {
            if (data.result) {
                var form = ''
                for (var i=0; i<data.idList.length; i++) {
                    form += '<div>' + data.idList[i] + '</div>';
                }
                $('#idList').html(form);
            } else {
                alert(data.message);
                return false;
            }
        }
    })
}