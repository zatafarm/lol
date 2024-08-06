document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('search-input-moo');
    const suggestionsContainer = document.getElementById('suggestions-moo');
    let currentFocus = -1;

    searchInput.addEventListener('input', function() {
        const query = searchInput.value.trim();
        if (query.length > 0) {
            fetchSuggestions(query);
        } else {
            clearSuggestions();
        }
    });

    searchInput.addEventListener('keydown', function(e) {
        const suggestions = suggestionsContainer.getElementsByClassName('suggestion-item-moo');
        if (e.key === 'ArrowDown') {
            currentFocus++;
            addActive(suggestions);
        } else if (e.key === 'ArrowUp') {
            currentFocus--;
            addActive(suggestions);
        } else if (e.key === 'Enter') {
            e.preventDefault();
            if (currentFocus > -1) {
                if (suggestions[currentFocus]) {
                    suggestions[currentFocus].click();
                }
            }
        }
    });

    function fetchSuggestions(query) {
        $.ajax({
            url: '/search-bj', // 실제 서버 엔드포인트로 변경
            method: 'GET',
            data: { search : query },
            success: function(response) {
                displaySuggestions(response);
            },
            error: function(xhr) {
				if (xhr.status === 404) {
				                        alert(xhr.responseText); // 서버로부터 받은 에러 메시지 표시
				                    } else {
				                        console.error('Error fetching suggestions:', xhr);
				                    }
            }
        });
    }

    function displaySuggestions(suggestions) {
        clearSuggestions();
        suggestions.forEach(suggestion => {
            const suggestionItem = document.createElement('div');
            suggestionItem.textContent = suggestion;
            suggestionItem.classList.add('suggestion-item-moo');
            suggestionItem.addEventListener('click', function() {
                searchInput.value = suggestion;
                clearSuggestions();
                performSearch(); // suggestion 클릭 시 검색 실행
            });
            suggestionsContainer.appendChild(suggestionItem);
        });
    }

    function clearSuggestions() {
        suggestionsContainer.innerHTML = '';
        currentFocus = -1;
    }

    function addActive(suggestions) {
        if (!suggestions) return false;
        removeActive(suggestions);
        if (currentFocus >= suggestions.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = suggestions.length - 1;
        suggestions[currentFocus].classList.add('suggestion-active');
        searchInput.value = suggestions[currentFocus].textContent; // 검색어를 변경
    }

    function removeActive(suggestions) {
        for (let i = 0; i < suggestions.length; i++) {
            suggestions[i].classList.remove('suggestion-active');
        }
    }
});

// 검색 함수 정의
function performSearch() {
    // 입력 필드의 값을 가져옵니다.
    var query = document.getElementById('search-input-moo').value;

    // 값이 비어있는지 확인합니다.
    if (query.trim() !== "") {
        // location.href를 사용하여 페이지를 이동합니다.
        location.href = '/bjsearch?search=' + encodeURIComponent(query);
    } else {
        alert('검색어를 입력해주세요.');
    }
}

// 버튼 클릭 이벤트 리스너 추가
document.getElementById('search-button-moo').addEventListener('click', performSearch);

// 입력 필드에 keydown 이벤트 리스너 추가
document.getElementById('search-input-moo').addEventListener('keydown', function(event) {
    // 엔터 키가 눌렸는지 확인합니다.
    if (event.key === 'Enter') {
        performSearch();
    }
});
