document.getElementById('id').addEventListener('input', function() {
    const userId = this.value;
	var regExp = /^[a-z]+[a-z0-9]{5,19}$/g;
    const messageElement = document.getElementById('usernameMessage');
	if(regExp.test(userId)){
    if (userId.length > 0) {
        const encodedUserId = encodeURIComponent(userId);
		console.log(encodedUserId);
        fetch(`/check-username?username=${encodedUserId}`)
            .then(response => response.json())
            .then(data => {
                if (data) {
                    messageElement.textContent = '이미 존재하는 아이디 입니다.';
					messageElement.style.fontSize = "12px"
					messageElement.style.color = 'red';
					document.getElementById('idCheck').value = "1";
                } else {
                    messageElement.textContent = '사용 가능한 아이디 입니다.';
					messageElement.style.fontSize = "12px"
					messageElement.style.color = 'blue';
					document.getElementById('idCheck').value = "0";
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    } else {
		document.getElementById('idCheck').value = "1";
        messageElement.textContent = '';
    }
	}
	else {
		messageElement.textContent = '영문자로 시작하는 영문자 또는 숫자 6~20자로 설정해주세요';
		messageElement.style.fontSize = "12px"
					messageElement.style.color = 'red';
					document.getElementById('idCheck').value = "1";
	}
});

document.getElementById('nickname').addEventListener('input', function() {
    const nickname = this.value;
    const messageElement = document.getElementById('nicknameMessage');
	const nicknameRegex = /^(?!.*[ㄱ-ㅎㅏ-ㅣ])[a-zA-Z0-9가-힣]{2,12}$/;
	
    if (nickname.length > 0) {
		if (nicknameRegex.test(nickname)) {
        fetch(`/check-nickname?nickname=${nickname}`)
            .then(response => response.json())
            .then(data => {
                if (data) {
                    messageElement.textContent = '이미 사용중인 닉네임 입니다.';
					messageElement.style.fontSize = "12px"
					messageElement.style.color = 'red';
					document.getElementById('nicknameCheck').value = "1";
                } else {
                    messageElement.textContent = '사용 가능한 닉네임입니다.';
					messageElement.style.fontSize = "12px"
					messageElement.style.color = 'blue';
					document.getElementById('nicknameCheck').value = "0";
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
			}
			else {
				messageElement.textContent = '닉네임은 최소 3자에서 최대 10자까지 허용합니다';
				messageElement.style.color = 'red';
				document.getElementById('nicknameCheck').value = "1";
				messageElement.style.fontSize = '12px';			
			}
    } else {
        messageElement.textContent = '';
    }
});

document.getElementById('pass2').addEventListener('input', function() {
	const pass2 = this.value;
	const pass1 = document.getElementById('pass').value;
	const messageElement = document.getElementById('passmessage2');
	if (pass2.length > 0) {
		if (pass2 == pass1) {
			messageElement.textContent = '비밀번호 중복 확인 완료';
			messageElement.style.fontSize = "12px"
			messageElement.style.color = 'blue';
			document.getElementById('passCheck2').value = "0";
		}
		else {
			messageElement.textContent = '비밀번호가 일치하지 않습니다';
			messageElement.style.fontSize = "12px"
			messageElement.style.color = 'red';
			document.getElementById('passCheck2').value = "1";	
		}
	} else {
	    messageElement.textContent = '';
		document.getElementById('passCheck2').value = "1";	
	}
});
function openPopup(popupId) {
    document.getElementById(popupId).style.display = "block";
}

function closePopup(popupId) {
    document.getElementById(popupId).style.display = "none";
}

// 페이지 클릭 시 팝업 닫기
window.onclick = function(event) {
    if (event.target.className === "popup") {
        event.target.style.display = "none";
    }
}

document.getElementById('pass').addEventListener('input', function() {
	const pass1 = document.getElementById('pass').value;
	const pass2 = document.getElementById('pass2').value;
	const messageElement = document.getElementById('passmessage');
	const messageElement2 = document.getElementById('passmessage2');
	var regex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()]{8,16}$/;
	if (pass1.length > 0) {
		if (regex.test(pass1)){
			messageElement.textContent = '비밀번호 사용가능';
			messageElement.style.color = 'blue';
			messageElement.style.fontSize = "12px"
			document.getElementById('passCheck').value = "0";
		}
		else {
			messageElement.textContent = '비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.';
			messageElement.style.color = 'red';
			messageElement.style.fontSize = "12px"
			document.getElementById('passCheck').value = "1";
		}
	} else {
	    messageElement.textContent = '';
		document.getElementById('passCheck').value = "1";
	}
		if (pass2.length > 6) {
			if (pass2 == pass1) {
				messageElement2.textContent = '비밀번호 중복 확인 완료';
				messageElement2.style.color = 'blue';
				messageElement.style.fontSize = "12px"
				document.getElementById('passCheck2').value = "0";
			}
			else {
				messageElement2.textContent = '비밀번호가 일치하지 않습니다';
				messageElement2.style.color = 'red';
				messageElement.style.fontSize = "12px"
				document.getElementById('passCheck2').value = "1";	
			}
		} else {
		    messageElement2.textContent = '';
			document.getElementById('passCheck2').value = "1";	
		}
});

$(document).ready(function () {
    $("#email-button").click(function () {
        var emailex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
        var email = $("#email").val();
        var checkcodeform = document.getElementById('codeCheckform').value;
		var timecheck = document.getElementById('timecheck').value;
		$("#email-button").prop('disabled', true);
        if (emailex.test(email)) {
			fetch(`/check-email?email=${email}`)
			    .then(response => response.json())
			    .then(data => {
			        if (data) {
			          alert("이미 사용중인 이메일 입니다")
					  $("#email-button").prop('disabled', false);
			        } else {
			   		if(timecheck == "1"){
			    var emailRequestDto = {
			        email: email
			    };
			    $.ajax({
			        type: "POST",
			        url: "/signup/email",
			        contentType: "application/json",
			        data: JSON.stringify(emailRequestDto),
			        success: function (code) {
			            if (code) {
							$("#email-button").prop('disabled', false);
			                alert("입력하신 메일로 인증번호가 전송되었습니다.");
							$("#email").attr("readonly", true);
							document.getElementById('timecheck').value = "0";
							timeout = setTimeout(function () {
								document.getElementById('timecheck').value = "1";
							                       }, 60000); // 60000ms = 1분
			                if (checkcodeform == "1") {
			                    document.getElementById('codeCheckform').value = "0";
			                    $("#signupemail").append('<input type="text" class="registerMooInput" id="authNum" placeholder="인증번호를 입력하세요" style="margin-top:10px"> <button type="button" id="auth-button" name="auth-button" class="registerMooButton auth-button" style="margin-top:10px; width: 100px;height: 40px; font-size: 12px;">인증번호 확인</button>');
			                }
			            } else {
			                alert("인증번호를 받을 수 없습니다.");
			            }
			        },
			        error: function () {
			            alert("인증번호를 받을 수 없습니다. 입력하신 이메일 형식을 확인해주세요.");
			        }
			    });
			}
			else {
				$("#email-button").prop('disabled', false);
				alert("인증번호는 1분마다 가능합니다")
			}
			        }
			    })
			    .catch(error => {
			        console.error('Error:', error);
			    });
		} else {
			$("#email-button").prop('disabled', false);
            alert("이메일을 다시 확인해주세요");
        }
    });

    $(document).on('click', '#auth-button', function () {
        var email = $("#email").val();
        var authNum = $("#authNum").val();
        var emailCheckDto = {
            email: email,
            authNum: authNum
        };
        $.ajax({
            type: "POST",
            url: "/signup/emailAuth",
            contentType: "application/json",
            data: JSON.stringify(emailCheckDto),
            success: function (message) {
                if (message) {
					document.getElementById('email').readOnly = true;
					document.getElementById('authNum').readOnly = true;
					document.getElementById('authNum').style.backgroundColor = '#a0a0a0';
					document.getElementById('email').style.backgroundColor = '#a0a0a0';
                    alert("이메일 인증에 성공하였습니다.");
                    document.getElementById('emailCheck').value = "0";
                } else {
                    alert("잘못된 인증번호이거나 시간 초과로 인증번호가 만료되었습니다.");
                }
            },
            error: function (xhr, status) {
                if (xhr.status == 500) {
                    alert("잘못된 인증번호이거나 시간 초과로 인증번호가 만료되었습니다.");
                } else {
                    alert("오류가 발생했습니다: " + status);
                }
            }
        });
    });
});

var form = document.querySelector('.registerMooForm');
if (!form) {
 
} else {
    form.addEventListener('submit', function(event) {
        var id = document.getElementById('idCheck').value;
        var pass = document.getElementById('passCheck').value;
        var pass2 = document.getElementById('passCheck2').value;
        var nickname = document.getElementById('nicknameCheck').value;
        var emailCheck = document.getElementById('emailCheck').value;

     

        if (id === '1') {
            alert("아이디를 다시 확인해주세요");
            document.getElementById("id").focus();
            event.preventDefault();
      
        } else if (pass === '1' || pass2 === '1') {
            alert("비밀번호를 다시 확인해주세요");
            document.getElementById("pass").focus();
            event.preventDefault();
           
        } else if (nickname === '1') {
            alert("닉네임을 다시 확인해주세요");
            document.getElementById("nickname").focus();
            event.preventDefault();
         
        } else if (emailCheck === '1') {
            alert("이메일 인증을 다시 확인해주세요");
            document.getElementById("email").focus();
            event.preventDefault();
          
        }
    });
}
