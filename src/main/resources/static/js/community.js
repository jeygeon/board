$(document).ready(function() {
    //여기 아래 부분
    $('#summernote').summernote({
        height: 300,                 // 에디터 높이
        minHeight: null,             // 최소 높이
        maxHeight: null,             // 최대 높이
        focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
        lang: "ko-KR",					// 한글 설정
        placeholder: '최대 2048자까지 쓸 수 있습니다'	//placeholder 설정

    });
});

function savePost() {
    var _subject = $('.post-subject').val(),
        _content = $('.post-content').val(),
        param = null;

    if (_subject == '') {
        alert('제목을 입력해주세요.');
        return false;
    }

    if (_content == '') {
        alert('내용을 입력해주세요.');
        return false;
    }

    param = {
        subject : _subject,
        content: _content
    }

    $('#post-form').submit();
    return false;

/*    $.ajax({
        url: '/board/save',
        type: 'POST',
        data: param,
        dataType: 'json',
        success:function(data) {
            alert(data.message);
        }
    })*/
}