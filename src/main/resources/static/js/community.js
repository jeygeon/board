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

    $('#post-form').submit();
    return false;
}

function replySave(event) {

    var _boardUid = $('#boardUid').val(),
        _userUid = $('#loginUser-uid').val(),
        _content = $('#myReply').val(),
        param = null;

    if (event.keyCode == 13) {

        if (_content == '') {
            alert('댓글을 입력해주세요.')
            $('#myReply').focus();
            return false;
        }

        param = {
            boardUid : _boardUid,
            userUid : _userUid,
            content : _content
        }

        $.ajax({
            url: '/api/board/' + _boardUid + '/reply/add',
            type: 'POST',
            data: param,
            dataType: 'json',
            success:function(data) {
                if (!data.result) {
                    alert(data.message);
                    return false;
                }

                var pagingParam = {
                    start : 0,
                    size : 5,
                    boardUid : _boardUid
                }

                $.ajax({
                    url: '/api/board/' + _boardUid + '/reply/paging',
                    type: 'POST',
                    data: pagingParam,
                    dataType: 'json',
                    success:function(data) {
                        var replyForm = '';
                        for (var i=0; i<data.replyList.length; i++) {
                            replyForm += '<div class="reply-section">';
                            replyForm += '<div class="profile-box">';
                            replyForm += '<img class="profile" src="/image/canal-6519196_1280.jpg">';
                            replyForm += '</div>';
                            replyForm += '<div style="width: 695px;">';
                            replyForm += '<div style="margin-bottom: 7px;">';
                            replyForm += '<span>jaeygun</span>';
                            replyForm += '<span class="reply-split">*</span>';
                            replyForm += '<span>2시간전</span>';
                            replyForm += '</div>';
                            replyForm += '<p style="margin-bottom: 7px;">우와 이쁘다~</p>';
                            replyForm += '<div>';
                            replyForm += '<span><i class="fa-solid fa-heart" style="color: #e67180;"></i></span>';
                            replyForm += '<span>156</span>';
                            replyForm += '<span class="reply-split">*</span>';
                            replyForm += '<span>답글달기</span>';
                            replyForm += '<span class="reply-split">*</span>';
                            replyForm += '<span>신고</span>';
                            replyForm += '</div></div></div>';
                        }
                        $('#reply').html(replyForm);
                    }
                })
            }
        })
    }
    return false;
}