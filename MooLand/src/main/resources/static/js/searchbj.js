$(document).ready(function() {
          $('#bj-search-button-moo').click(function() {
              var vsbjName = $('#bj-search-input-moo').val();
var playerbjName = document.getElementById("playerName").textContent;
              $.ajax({
                  url: '/searchvsbj',  // 검색을 처리할 서버의 엔드포인트
                  type: 'GET',
                  data: { bjname: vsbjName,
							playerbjName: playerbjName
				   },
				   success: function(response) {
					if(response.games != 0 && response.vsName != playerbjName){
				                   // 기존 리스트 초기화
								   $('#vs-list').html('');

				                   var listItem = `
								   
								   
				                       <li>
				                           <div class="divvs">
				                               <span>vs ${response.vsName}: ${response.games}전 ${response.win}승 ${response.lose}패 (${response.rate})% </span>
				                           </div>
				                       </li>
				                   `;
				                   $('#vs-list').append(listItem);
				     			 $('#bj-search-input-moo').val("");
								 const blanceSpan = document.getElementById('blance');
								 
								  blanceSpan.innerHTML = "상대전적";								   }
								   else{
									alert("전적이 없거나 BJ 명을 정확히 입력해주세요.")
								   }
				               },
                  error: function(error) {
                      console.error('Error:', error);
                  }
              });
          });
      });


	  
	  document.addEventListener('DOMContentLoaded', function() {
	      const moobjSearchInput = document.getElementById('bj-search-input-moo');
	      const moobjSuggestionsContainer = document.getElementById('bj-suggestions-moo');
	      let moobjCurrentFocus = -1;

	      moobjSearchInput.addEventListener('input', function() {
	          const moobjQuery = moobjSearchInput.value.trim();
	          if (moobjQuery.length > 0) {
	              moobjFetchSuggestions(moobjQuery);
	          } else {
	              moobjClearSuggestions();
	          }
	      });

	      moobjSearchInput.addEventListener('keydown', function(e) {
	          const moobjSuggestions = moobjSuggestionsContainer.getElementsByClassName('suggestion-item-moo');
	          if (e.key === 'ArrowDown') {
	              moobjCurrentFocus++;
	              moobjAddActive(moobjSuggestions);
	          } else if (e.key === 'ArrowUp') {
	              moobjCurrentFocus--;
	              moobjAddActive(moobjSuggestions);
	          } else if (e.key === 'Enter') {
	              e.preventDefault();
	              if (moobjCurrentFocus > -1) {
	                  if (moobjSuggestions[moobjCurrentFocus]) {
	                      moobjSuggestions[moobjCurrentFocus].click();
	                  }
	              }
	          }
	      });

	      function moobjFetchSuggestions(moobjQuery) {
	          $.ajax({
	              url: '/search-bj', // 실제 서버 엔드포인트로 변경
	              method: 'GET',
	              data: { search: moobjQuery },
	              success: function(moobjResponse) {
	                  moobjDisplaySuggestions(moobjResponse);
	              },
	              error: function(error) {
	                  console.error('Error fetching suggestions:', error);
	              }
	          });
	      }

	      function moobjDisplaySuggestions(moobjSuggestions) {
	          moobjClearSuggestions();
	          moobjSuggestions.forEach(moobjSuggestion => {
	              const moobjSuggestionItem = document.createElement('div');
	              moobjSuggestionItem.textContent = moobjSuggestion;
	              moobjSuggestionItem.classList.add('suggestion-item-moo');
	              moobjSuggestionItem.addEventListener('click', function() {
	                  moobjSearchInput.value = moobjSuggestion;
	                  moobjClearSuggestions();
	                  moobjPerformSearch(); // suggestion 클릭 시 검색 실행
	              });
	              moobjSuggestionsContainer.appendChild(moobjSuggestionItem);
	          });
	      }

	      function moobjClearSuggestions() {
	          moobjSuggestionsContainer.innerHTML = '';
	          moobjCurrentFocus = -1;
	      }

	      function moobjAddActive(moobjSuggestions) {
	          if (!moobjSuggestions) return false;
	          moobjRemoveActive(moobjSuggestions);
	          if (moobjCurrentFocus >= moobjSuggestions.length) moobjCurrentFocus = 0;
	          if (moobjCurrentFocus < 0) moobjCurrentFocus = moobjSuggestions.length - 1;
	          moobjSuggestions[moobjCurrentFocus].classList.add('bj-suggestion-active');
	          moobjSearchInput.value = moobjSuggestions[moobjCurrentFocus].textContent; // 검색어를 변경
	      }

	      function moobjRemoveActive(moobjSuggestions) {
	          for (let i = 0; i < moobjSuggestions.length; i++) {
	              moobjSuggestions[i].classList.remove('bj-suggestion-active');
	          }
	      }
	  });

	  // 검색 함수 정의
	  function moobjPerformSearch() {
	      // 입력 필드의 값을 가져옵니다.
	      var moobjQuery = document.getElementById('bj-search-input-moo').value;

	      // 값이 비어있는지 확인합니다.
	      if (moobjQuery.trim() !== "") {
			$('#bj-search-button-moo').click();
	      } else {
	          alert('검색어를 입력해주세요.');
	      }
	  }


	  // 입력 필드에 keydown 이벤트 리스너 추가
	  document.getElementById('bj-search-input-moo').addEventListener('keydown', function(event) {
	      // 엔터 키가 눌렸는지 확인합니다.
	      if (event.key === 'Enter') {
	          moobjPerformSearch();
	      }
	  });
	  function createPagination(totalPages, currentPage, startPage, endPage) {
	      const playerNameElement = document.getElementById('playerName');
	      const playerName = playerNameElement.textContent;
	      const paginationElement = document.getElementById('pagination');
	      paginationElement.innerHTML = '';

	      const createPageButton = (text, page, isActive = false, isNavigation = false) => {
	          const a = document.createElement('a');
	          a.textContent = text;
	          if (isActive && !isNavigation) a.classList.add('active');
	          a.addEventListener('click', (event) => {
	              event.preventDefault();
	              // URL 업데이트 후 페이지 이동
	              window.location.href = `/bjsearch?search=${playerName}&page=${page}`;
	          });
	          return a;
	      };

	      // First button
	      if (currentPage > 1) {
	          paginationElement.appendChild(createPageButton('<<', 1, false, true));
	          paginationElement.appendChild(createPageButton('<', currentPage - 1, false, true));
	      } else {
	          paginationElement.appendChild(createPageButton('<<', 1, true, true));
	          paginationElement.appendChild(createPageButton('<', 1, true, true));
	      }

	      // Page buttons
	      for (let i = startPage; i <= endPage; i++) {
	          if (i > totalPages) break;
	          paginationElement.appendChild(createPageButton(i, i, i === currentPage));
	      }

	      // Next button
	      if (currentPage < totalPages) {
	          paginationElement.appendChild(createPageButton('>', currentPage + 1, false, true));
	          paginationElement.appendChild(createPageButton('>>', totalPages, false, true));
	      } else {
	          paginationElement.appendChild(createPageButton('>', totalPages, true, true));
	          paginationElement.appendChild(createPageButton('>>', totalPages, true, true));
	      }
	  }

	  // 현재 페이지를 URL에서 추출하는 함수
	  function getCurrentPageFromURL() {
	      const urlParams = new URLSearchParams(window.location.search);
	      return parseInt(urlParams.get('page')) || 1;
	  }

	  // 페이지 로드 시 페이징네이션 생성
	  document.addEventListener('DOMContentLoaded', () => {
	      const currentPage = getCurrentPageFromURL();
	      createPagination(totalPageCount, currentPage, startPage, endPage);
	
	  });


