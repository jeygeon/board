$(document).ready(function() {

    // 게시글 댓글 페이징 함수
    $('.page-btn').click(function(data) {

        var _page = data.currentTarget.value,
            _boardUid = $('#boardUid').val(),
            _start = _page - 1,
            pagingParam = null;

        pagingParam = {
            start : _start,
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
                    replyForm += '<span>' + data.replyList[i].userDTO.nickName + '</span>';
                    replyForm += '<span class="reply-split">*</span>';
                    replyForm += '<span>2시간전</span>';
                    replyForm += '</div>';
                    replyForm += '<p style="margin-bottom: 7px;">' + data.replyList[i].content + '</p>';
                    replyForm += '<div><div style="display: inline-block" id="reply-hit" class="reply-hit">';
                    if (data.replyList[i].likeCheck) {
                        replyForm += '<span><i class="fa-solid fa-heart" id="hit-icon" style="color: #e67180;"></i></span>';
                    } else {
                        replyForm += '<span><i class="fa-regular fa-heart" id="hit-icon" style="color: #e67180;"></i></span>';
                    }
                    replyForm += '<span>' + data.replyList[i].likeCount + '</span></div>';
                    replyForm += '<span class="reply-split">*</span>';
                    replyForm += '<span>답글달기</span>';
                    replyForm += '<span class="reply-split">*</span>';
                    replyForm += '<span>신고</span>';
                    replyForm += '<input type="hidden" value="' + data.replyList[i].replyUid + '" id="replyUid">';
                    replyForm += '</div></div></div>';
                }

                // 댓글 리스트 재생성
                $('#reply').html(replyForm);
            }
        })
    })
});

// 댓글 좋아요 버튼 클릭시 icon toggle and like count add
$(document).on('click', '#reply-hit', function() {

    var like = $(this),
        _replyUid = like.siblings()[4].value,
        _boardUid = $('#boardUid').val(),
        _status = "up",
        param = null;

    if (like[0].children[0].children[0].classList.contains('fa-solid')) {
        _status = "down";
    }

    param = {
        status : _status,
        boardUid : _boardUid,
        replyUid : _replyUid
    }

    $.ajax({
        url: '/api/board/' + _boardUid + '/reply/' + _replyUid,
        type: 'PUT',
        data: param,
        dataType: 'json',
        success:function(data) {
            like = like.find('#hit-icon').toggleClass('fa-regular fa-solid');
            // like = like[0].children[0].children[0].toggleClass('fa-regular fa-solid');
            var likeCount = data.reply.likeCount;
            like.prevObject[0].children[1].innerText = likeCount;
        }
    })

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
                            replyForm += '<span>' + data.replyList[i].userDTO.nickName + '</span>';
                            replyForm += '<span class="reply-split">*</span>';
                            replyForm += '<span>2시간전</span>';
                            replyForm += '</div>';
                            replyForm += '<p style="margin-bottom: 7px;">' + data.replyList[i].content + '</p>';
                            replyForm += '<div><div style="display: inline-block" id="reply-hit" class="reply-hit">';
                            if (data.replyList[i].likeCheck) {
                                replyForm += '<span><i class="fa-solid fa-heart" id="hit-icon" style="color: #e67180;"></i></span>';
                            } else {
                                replyForm += '<span><i class="fa-regular fa-heart" id="hit-icon" style="color: #e67180;"></i></span>';
                            }
                            replyForm += '<span>' + data.replyList[i].likeCount + '</span></div>';
                            replyForm += '<span class="reply-split">*</span>';
                            replyForm += '<span>답글달기</span>';
                            replyForm += '<span class="reply-split">*</span>';
                            replyForm += '<span>신고</span>';
                            replyForm += '<input type="hidden" value="' + data.replyList[i].replyUid + '" id="replyUid">';
                            replyForm += '</div></div></div>';
                        }

                        // 댓글 리스트 재생성
                        $('#reply').html(replyForm);

                        // 입력했던 댓글 창 clear
                        $('#myReply').val('');

                        // 총 댓글 Count수정
                        $('#reply-totalCount').text(data.totalCount);

                    }
                })
            }
        })
    }
    return false;
}
