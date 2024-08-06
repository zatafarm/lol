window.addEventListener('keydown', disableRefresh);
$(document).ready(function() {
    $('.player-input').on('input', function() {
        var inputField = $(this);
        var playerName = inputField.val();
        var vs;
        var id = inputField.attr('id');
        if (id.includes("t1")) {
            vs = id.replace("t1", "t2");
        } else if (id.includes("t2")) {
            vs = id.replace("t2", "t1");
        }	
        var parentDiv = inputField.closest('div');
        var imgTag = parentDiv.find('img');

        if (playerName.length >= 2) {
            var encodedPlayerName = encodeURIComponent(playerName);
            
            $.ajax({
                url: `/getPlayerData?playerName=${encodedPlayerName}`,
                type: 'GET',
                success: function(response) {
                    var data = response.data;
                    if (data) {
                        imgTag.attr('src', data.imgUrl);

                        if (data.imgUrl != null) {
                            var playerDivId = inputField.attr('id') + 'div';
							var encodedPlayerName2 = encodeURIComponent(data.bjname);
                            var playerDiv = $('#' + playerDivId);
							playerDiv.innerHTML = '';
                            var positionText = `포지션: ${data.lolmposition}`;
                            var vs2div = vs + 'div';
                            var playerDiv2 = $('#' + vs2div);
                            var inputElements = playerDiv2.find('input').val();
                            if (data.lolsposition != '없음') {
                                positionText += `, ${data.lolsposition}`;
                            }
							
							playerDiv.html(`
							    <input type="text" value="${data.bjname}" style="display:none;"/>
							    <a target='_blank' href='https://fow.kr/find/${data.lolnickName1}-${data.lolnickName2}' style="font-size:22px; margin-top:-15px; font-weight:bold;">${data.lolnickName}</a>
							    <span>14-2S: ${data.loltier} ${data.lolrank} 14-1S: ${data.lolbtier}</span>
							    <span>${positionText}</span>
						
							`);  
                         $.ajax({
							
                                url: `/getAdditionalData?playerName=${encodedPlayerName2}&vsName=${inputElements}`
							,
                                type: 'GET',
                                success: function(response2) {
                                    var data2 = response2;
									var data3 = response2.Sgsmap2;
									var data4 = response2.gsmapvs;
									
                                    if (data2) {
										if(data3.games != 0) {
											var formattedResults = "";
											var matchResults = data2.totalgame;
											var length = matchResults.length;
											for (let i = 0; i < length; i++) {
											    const char = matchResults[i];
											    let formattedChar;
											    if (char === "승") {
											        formattedChar = `<span style="color: red;">${char}</span>`;
											    } else if (char === "패") {
											        formattedChar = `<span style="color: #007BFF;">${char}</span>`;
											    } else {
											        formattedChar = char;
											    }

												if (i === 0) {
												      formattedResults += `<span style="font-weight: bold; color: white; font-size:16px;">●</span>`;
												  }

											    formattedResults += formattedChar;

											    // 마지막 문자 뒤에는 "-"를 추가하지 않음
											    if (i < length - 1) {
											        formattedResults += "-";
											    }
											}
											const winRate3 = ((data3.win / data3.games) * 100).toFixed(1); // 승률 계산
											
											playerDiv.find("span.gamesmov").remove();
											playerDiv.append(`
											    <span class="gamesmov" style="font-size:15px;">${data3.games}전  <span style="color:red;">${data3.win}승</span><span style="color:#007BFF;"> ${data3.lose}패 </span>승률 ${winRate3}%/ 최근 5경기:<span>(</span> ${formattedResults})</span>
											`);
											var playerDiv2 = $('#' + vs2div);

																	
																			if(inputElements != "undefined" &&data4 !=null && data4.totalGames != 0){
																				playerDiv2.find('span.movvs').remove();
																				playerDiv.find('span.movvs').remove();
																				const winRate = ((data4.win / data4.games) * 100).toFixed(1); // 승률 계산
																				const winRate2 = ((data4.lose / data4.games) * 100).toFixed(1); // 승률 계산
																				playerDiv.append(`
						<span class="movvs" style="font-size:15px;">vs ${inputElements}: ${data4.games}전 <span style="color:red;">${data4.win}승</span><span style="color:#007BFF;"> ${data4.lose}패</span> 승률 ${winRate}% </span>
						`);
				
						playerDiv2.append(`
										<span class="movvs"  style="font-size:15px;">vs ${data.bjname}: ${data4.games}전 <span style="color:red;">${data4.lose}승</span>  <span style="color:#007BFF;">${data4.win}패 </span> 승률 ${winRate2}% </span>
																				`);	
																		}
										}
								
                                    } else {
                                        console.log('추가 데이터가 없습니다.');
                                    }
                                },
                                error: function(xhr, status, error) {
                                    console.error('추가 요청 실패:', error);
                                }
                            }); 
							}
                        
						else {
						                       imgTag.attr('src', '/bootstrap/img/defult.png');
						                       var playerDivId = inputField.attr('id') + 'div';
						                       var playerDiv = $('#' + playerDivId);
						                       playerDiv.html('');
						                   }
                    }
                },
                error: function(xhr, status, error) {
                    console.error('요청 실패:', error);
                }
            });
        }
		else {
							                  imgTag.attr('src', '/bootstrap/img/defult.png');
							                    var playerDivId = inputField.attr('id') + 'div';
							                    var playerDiv = $('#' + playerDivId);
							                    playerDiv.html('');
							                   }

    });
});



let selectedMode = 1;
let redscroe = 0;
let bluescroe = 0;
let gameid = null;
let startcheck = false;
let t1 = '';
let t2 = '';
function swapIds(id1, id2) {
	if (startcheck == false) {
	    var element1 = document.getElementById(id1).value;
	    var element2 = document.getElementById(id2).value;
	    document.getElementById(id1).value = element2;
	    document.getElementById(id2).value = element1;

	    $('#' + id1).trigger('input');
	    setTimeout(function() {
	        $('#' + id2).trigger('input');
	    }, 50);
	}

 else {
	alert("경기 진행중 스왑은 불가합니다. 경기가 끝난 후에 눌러주세요");
 }
}

function swapall() {
	var oksswap = confirm("진형을 바꾸시겠습니까 ?");
						if(oksswap === true){
			
		
		for (let i = 1; i <= 5; i++) {
		  var id1 = 't1p' + i; 
		  var id2 = 't2p' + i; 
		  var element1 = document.getElementById(id1).value;
		  var element2 = document.getElementById(id2).value;
		  document.getElementById(id1).value = element2;
		  document.getElementById(id2).value = element1;
		}
		 var span1 = document.getElementById('rtsn');
		 var span2 = document.getElementById('btsn');
		var tempContent = span1.innerText;
		span1.innerText = span2.innerText;
		span2.innerText = tempContent;
		 var spans1 = document.getElementById('rts');
		 var spans2 = document.getElementById('bts');
		var tempContents = spans1.innerText;
		spans1.innerText = spans2.innerText;
		spans2.innerText = tempContents;
		  var telement1 = document.getElementById('t1').value;
		  var telement2 = document.getElementById('t2').value;
		  document.getElementById('t1').value = telement2;
		  document.getElementById('t2').value = telement1;
		$('.player-input').trigger('input');
		}
	}

	function start() {
	        var blueTeamButton = document.getElementById('bbutton');
	        var redTeamButton = document.getElementById('rbutton');
			var mtl = document.getElementById('gameModeSelect').value;
			var checktimename = document.getElementById('t1').value;
			var checktimename2 = document.getElementById('t2').value;
			var bes = document.getElementById('bts');
			var res = document.getElementById('rts');
			redscroe = 0;
			bluescroe = 0;
			bes.innerText = bluescroe;
			res.innerText = redscroe;
			if (!blueTeamButton.classList.contains('hidden') && !redTeamButton.classList.contains('hidden')) {
			          alert("게임이 이미 진행중 입니다");
					
			          return;
			       }
			for(var b = 1; b < 3 ; b++){
				for(var a = 1; a <6 ; a++){
				     var check = document.getElementById('t' + b + 'p' + a);
					 if(check.value === ''){
						alert("참여 BJ 이름을 모두 입력해주세요");
						document.getElementById('t' + b + 'p' + a).focus();
						return;
					 }
				}
				}
			if(checktimename === '' || checktimename2 === ''){
				alert("팀 이름을 설정해주세요");
		
				return;
				}
				if (checktimename.length > 6 || checktimename2.length > 6) {
							       alert("팀 이름을 6글자 이하로 설정해주세요");
							       return;
							   }
			if(mtl === "null"){
				alert("게임 모드를 설정해주세요");
	
				return;
			}
	  
				t1 = document.getElementById('t1').value;
				t2 = document.getElementById('t2').value;
				var button = document.getElementById('startbutton');
				alert("게임을 시작합니다");
				button.classList.add('styled-button3'); 
				var dataload = document.getElementById('dataload');
				dataload.classList.add('hidden'); 
				button.innerHTML = '무승부'; 
				button.onclick = tie; 
				 creategame();
			
}

function tie() {
    alert('경기를 무승부로 설정합니다');
												finalgamesave("tie");
}
function creategame() {
	var mtl = document.getElementById('gameModeSelect').value;
	var scoreborad = document.getElementById('scoreboard');

	if(mtl === "three"){
													scoreborad.classList.remove('hidden');
													selectedMode = 2;
												}
									else if(mtl === "five"){
														scoreborad.classList.remove('hidden');
														selectedMode = 3;
													}
												else {								
					scoreborad.classList.add('hidden');
				}
				var mlt =  selectedMode;
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '/entry/creategame', true);
				xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

				xhr.onreadystatechange = function () {
					if (xhr.readyState === 4) {
						if (xhr.status === 200) {
							try {
								gameid = parseInt(xhr.responseText);
								savegame();
							} catch (e) {
								console.error('Error parsing response:', e);
								handleRequestError(xhr.status, 'Error parsing response');
							}
						} else {
							// 요청이 실패한 경우
							handleRequestError(xhr.status, xhr.statusText);
						}
					}
				};


				var data = JSON.stringify({
				  rtn : t1,
				  ltn : t2,
				  mlt : mlt
				});

				xhr.send(data);
		    }

			function savegame() {
					var t1p1 = getValueOrDefault('#t1p1div input', 't1p1');
					var t1p2 = getValueOrDefault('#t1p2div input', 't1p2');
					var t1p3 = getValueOrDefault('#t1p3div input', 't1p3');
					var t1p4 = getValueOrDefault('#t1p4div input', 't1p4');
					var t1p5 = getValueOrDefault('#t1p5div input', 't1p5');
					var t2p1 = getValueOrDefault('#t2p1div input', 't2p1');
					var t2p2 = getValueOrDefault('#t2p2div input', 't2p2');
					var t2p3 = getValueOrDefault('#t2p3div input', 't2p3');
					var t2p4 = getValueOrDefault('#t2p4div input', 't2p4');
					var t2p5 = getValueOrDefault('#t2p5div input', 't2p5');
					var blueTeamButton = document.getElementById('bbutton');
					var redTeamButton = document.getElementById('rbutton');
					var rstn = document.getElementById('rtsn');
					var bstn = document.getElementById('btsn');
					var xhr = new XMLHttpRequest();
							xhr.open('POST', '/entry/save', true);
							xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
						
							xhr.onreadystatechange = function () {
							    if (xhr.readyState === 4) { // 요청이 완료된 경우
							        if (xhr.status === 200) { // 요청이 성공적으로 완료된 경우
							            var response = xhr.responseText;
							            if (response === "ok") { // 응답이 "ok"인 경우
											startcheck = true;
											rstn.innerText = document.getElementById('t1').value;
											bstn.innerText = document.getElementById('t2').value;
											blueTeamButton.classList.remove('hidden');
											redTeamButton.classList.remove('hidden');
											document.getElementById('t1').readOnly = true;
											document.getElementById('t2').readOnly = true;
											document.getElementById('gameModeSelect').disabled = true;
							            } else { // 응답이 "no"인 경우
							                handleRequestError(xhr.status, xhr.statusText);
							            }
							        } else { // 요청이 실패한 경우
							            handleRequestError(xhr.status, xhr.statusText);
							        }
							    }
							};

							var data = JSON.stringify({
							    t1p1: t1p1,
							    t1p2: t1p2,
							    t1p3: t1p3,
							    t1p4: t1p4,
							    t1p5: t1p5,
								t2p1: t2p1,
							    t2p2: t2p2,
							    t2p3: t2p3,
							    t2p4: t2p4,
							    t2p5: t2p5,
								t1 : t1,
								t2 : t2,
								sendgameid: gameid
							});
							xhr.send(data);
					    }
						
						function winlosesave(winteam) {
							var t1p1 = getValueOrDefault('#t1p1div input', 't1p1');
							var t1p2 = getValueOrDefault('#t1p2div input', 't1p2');
							var t1p3 = getValueOrDefault('#t1p3div input', 't1p3');
							var t1p4 = getValueOrDefault('#t1p4div input', 't1p4');
							var t1p5 = getValueOrDefault('#t1p5div input', 't1p5');
							var t2p1 = getValueOrDefault('#t2p1div input', 't2p1');
							var t2p2 = getValueOrDefault('#t2p2div input', 't2p2');
							var t2p3 = getValueOrDefault('#t2p3div input', 't2p3');
							var t2p4 = getValueOrDefault('#t2p4div input', 't2p4');
							var t2p5 = getValueOrDefault('#t2p5div input', 't2p5');
							
									var winset = winteam;
									win(winteam);
									var xhr = new XMLHttpRequest();
									xhr.open('POST', '/entry/setwin', true);
									xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
								
									xhr.onreadystatechange = function () {
									    if (xhr.readyState === 4) { // 요청이 완료된 경우
									        if (xhr.status === 200) { // 요청이 성공적으로 완료된 경우
									            var response = xhr.responseText;
									            if (response === "ok") { // 응답이 "ok"인 경우

									            } else { // 응답이 "no"인 경우
									                handleRequestError(xhr.status, xhr.statusText);
									            }
									        } else { // 요청이 실패한 경우
									            handleRequestError(xhr.status, xhr.statusText);
									        }
									    }
									};

									var data = JSON.stringify({
									    t1p1: t1p1,
									    t1p2: t1p2,
									    t1p3: t1p3,
									    t1p4: t1p4,
									    t1p5: t1p5,
										t2p1: t2p1,
									    t2p2: t2p2,
									    t2p3: t2p3,
									    t2p4: t2p4,
									    t2p5: t2p5,
										winteam: winset,
										sendgameid: gameid,
										t1 : t1,
										t2 : t2,
										redscore : redscroe,
										bluescore : bluescroe
									});
									xhr.send(data);
							    }
								
						function reset() {
							if(startcheck == false){
						           location.reload();
						        }
								else {
									alert("게임 진행중에는 새로고침을 하실수 없습니다")
								}
								}
								
								function disableRefresh(event) {
								       if ((event.keyCode === 116) || (event.ctrlKey && event.keyCode === 82)) {
										if(startcheck == true){
											event.preventDefault();
											           event.returnValue = '';
													   alert("게임 진행중에는 새로고침을 하실수 없습니다")
											        }
												
								       }
									   if (event.key === "Enter") {
									               event.preventDefault(); // 기본 동작을 막음
									           }
								   }

			function finalgamesave(finalteam) {
				var ct1 = document.getElementById('t1').value;
				var ct2 = document.getElementById('t2').value;
				var finalwin = finalteam;
				
				if(finalteam === 'red' && t1 != ct1){
					finalwin = "blue";
					var ex = redscroe;
					redscroe = bluescroe;
					bluescroe = ex;
				}
				else if(finalteam === 'blue' && t2 != ct2){
									finalwin = "red";
									var ex = bluescroe;
										bluescroe = redscroe;
										 redscroe = ex;
								}
				else if(finalteam === 'tie') {
					finalwin = "tie";
					redscroe = 0;
					bluescroe = 0;
					var scoreborad = document.getElementById('scoreboard');

																	scoreborad.classList.add('hidden');
				}
								
							var xhr = new XMLHttpRequest();
							xhr.open('POST', '/entry/finalwin', true);
							xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
							xhr.onreadystatechange = function () {
							    if (xhr.readyState === 4) { // 요청이 완료된 경우
							        if (xhr.status === 200) { // 요청이 성공적으로 완료된 경우
							            var response = xhr.responseText;
							            if (response === "ok") { // 응답이 "ok"인 경우
											gameid = null;
											var button = document.getElementById('startbutton');
												button.classList.remove('styled-button3'); 
												button.innerHTML = '경기 시작'; 
												button.onclick = start; 
												var blueTeamButton = document.getElementById('bbutton');
													var redTeamButton = document.getElementById('rbutton');
													blueTeamButton.classList.add('hidden');
													redTeamButton.classList.add('hidden');
													document.getElementById('t2').readOnly = false;
													document.getElementById('t1').readOnly = false;
													selectedMode = 1;
													startcheck = false;
													
													var dataload = document.getElementById('dataload');
															dataload.classList.remove('hidden'); 
													document.getElementById('gameModeSelect').disabled = false;
							            } else { // 응답이 "no"인 경우
							                handleRequestError(xhr.status, xhr.statusText);
							            }
							        } else { // 요청이 실패한 경우
							            handleRequestError(xhr.status, xhr.statusText);
							        }
							    }
							};
							
							var data = JSON.stringify({
							  win : finalwin,
							  sendgameid: gameid,
							  t1 : t1,
							  t2 : t2,
							  redscore : redscroe,
							  bluescore : bluescroe
							});

							xhr.send(data);
					    }


						function getValueOrDefault(selector, defaultId) {
						    var element = document.querySelector(selector);
						    if (!element || !element.value) {
						        element = document.getElementById(defaultId);
						    }
						    return element.value;
						}

						function handleRequestError(status, statusText) {
						    // 에러 처리 로직을 여기에 추가합니다.
						    console.error('Request failed with status:', status, 'and statusText:', statusText);
						    alert('요청이 실패했습니다. 다시 시도해 주세요.');
						}
							
	function win(id) {
		var bes = document.getElementById('bts');
		var res = document.getElementById('rts');

	var winteamname = '';
	if(id === 'red'){

		redscroe =  parseInt(res.innerText, 10);
		redscroe += 1;
		res.innerText = redscroe;
		winteamname = document.getElementById('t1').value; 
	}
	if(id === 'blue'){	

		bluescroe =  parseInt(bes.innerText, 10);
		bluescroe += 1;	
		bes.innerText = bluescroe;
			winteamname = document.getElementById('t2').value;
			
	}
	redscroe = parseInt(res.innerText, 10);
	bluescroe = parseInt(bes.innerText, 10);
	var finalscore =   parseInt(bes.innerText, 10) + parseInt(res.innerText, 10);;
	if(selectedMode>1 && selectedMode >= redscroe && selectedMode >= bluescroe){
					alert(finalscore + ' 세트는 ' + winteamname + ' 팀이 승리');
					if(selectedMode>1 && selectedMode > redscroe && selectedMode > bluescroe){
						swapall();
				
					}
				}
	if(selectedMode == redscroe || selectedMode == bluescroe){
			if(redscroe>bluescroe){
			winteamname = document.getElementById('t1').value;
			}
			else {
				winteamname = document.getElementById('t2').value;
			}
			alert("최종 승리 " +  winteamname + ' 팀')
			finalgamesave(id);
	
			
	} 

		
		


	}
	function dataload() {
	      // AJAX 요청
	      $.ajax({
	          url: '/entryloaddata', // 데이터를 요청할 URL (실제 API URL로 변경해야 함)
	          method: 'GET', // 요청 방식
	          success: function(response) {
				if (response) { // response가 null이 아닐 때만 처리
				       const {
				           t1p1, t1p2, t1p3, t1p4, t1p5,
				           t2p1, t2p2, t2p3, t2p4, t2p5,
				           t1, t2
				       } = response; // 응답에서 데이터를 구조 분해 할당

				       // 입력 필드에 값 설정
				       $('#t1').val(t1);
				       $('#t2').val(t2);
				       $('#t1p1').val(t1p1);
				       $('#t1p2').val(t1p2);
				       $('#t1p3').val(t1p3);
				       $('#t1p4').val(t1p4);
				       $('#t1p5').val(t1p5);
				       $('#t2p1').val(t2p1);
				       $('#t2p2').val(t2p2);
				       $('#t2p3').val(t2p3);
				       $('#t2p4').val(t2p4);
				       $('#t2p5').val(t2p5);
					   $('.player-input').trigger('input');
				   } else {
				       // response가 null일 경우 경고창 띄우기
				       alert('데이터가 없습니다.');
				   }
	          },
	          error: function(jqXHR, textStatus, errorThrown) {
	              // 오류 처리
	              alert('데이터 로드 실패: ' + textStatus);
	          }
	      });
	  }