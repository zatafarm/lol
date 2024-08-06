document.getElementById('regit').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼 제출을 막음
	
    var bjName = document.getElementById('bjname').value;
    var bjId = document.getElementById('bjid').value;
    var lolNickname = document.getElementById('loln').value;
    start();
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/bj/bjregit', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onload = function() {
        if (xhr.status === 200) {
            if (xhr.responseText === "ok") {
                alert('등록 성공!');
            } 
			else if (xhr.responseText === "same") {
			              alert('해당 BJ는 이미 존재합니다');
						  resetProgress(); // 등록 실패 시 진행 상태 0으로 설정
			          } 
					  else if (xhr.responseText === "bjnull") {
					  	              alert('BJ아이디와 방송국 이름을 다시 확인해주세요');
					  				  resetProgress(); // 등록 실패 시 진행 상태 0으로 설정
					  	          } 
								  else if (xhr.responseText === "samelol") {
								  			  	              alert('해당 롤 닉네임은 이미 존재합니다');
								  			  				  resetProgress(); // 등록 실패 시 진행 상태 0으로 설정
								  			  	          } 
			else {
                alert('등록 실패! 입력하신 내용을 다시 확인해주세요');
                resetProgress(); // 등록 실패 시 진행 상태 0으로 설정
            }
        } else {
            alert('등록 실패! 입력하신 내용을 다시 확인해주세요');
            resetProgress(); // 등록 실패 시 진행 상태 0으로 설정
        }
    };

    xhr.send('bjname=' + encodeURIComponent(bjName) + '&bjid=' + encodeURIComponent(bjId) + '&loln=' + encodeURIComponent(lolNickname));
});

function updateProgress() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/bj/progress/status', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            var progress = parseInt(xhr.responseText);
            document.getElementById('progress-bar-fill').style.width = progress + '%';
			document.getElementById('loader').style.display = 'block'; // 로딩 애니메이션 보이기
            var progressText = progress + '%';
            switch (progress) {
				case 10:
				                  progressText += ' - BJ 정보 확인중';
				                  break;
                case 20:
                    progressText += ' - BJ 정보 등록중';
                    break;
                case 40:
                    progressText += ' - 롤 닉네임 등록중';
                    break;
                case 60:
                    progressText += ' - 롤 티어 등록중';
                    break;
                case 80:
                    progressText += ' - 롤 포지션 등록중';
                    break;
                case 100:
                    progressText += ' - 등록 완료';
                    clearInterval(interval); // 진행이 완료되면 주기적 업데이트 중지
					document.getElementById('loader').style.display = 'none'; 
                    break;
            }
            document.getElementById('progress-bar-text').innerText = progressText;
        }
    };
    xhr.send();
}

function resetProgress() {
    clearInterval(interval); // 진행 상태 업데이트 중지
    document.getElementById('progress-bar-fill').style.width = '0%';
    document.getElementById('progress-bar-text').innerText = '0%';
	document.getElementById('loader').style.display = 'none';
}

var interval;
function start() {
    updateProgress(); // 처음 한번 업데이트
	alert("등록 시작");
    interval = setInterval(updateProgress, 1000); // 주기적으로 진행 상태를 확인
}
