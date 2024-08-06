document.addEventListener('DOMContentLoaded', function() {
	var flashMessageDiv = document.getElementById("flash-message");
	      var message = flashMessageDiv.getAttribute("data-message");
	      if (message) {
	          alert(message);
	      }

	
    if (window.location.pathname === '/home' || window.location.pathname === '/entry') {
        return; // /home 경로에서는 아래 코드를 실행하지 않음
    }

    // 아래 모든 코드가 비활성화됩니다.
    const searchInputMoo = document.getElementById('top-search-input-moo');
    const suggestionsContainerMoo = document.getElementById('top-suggestions-moo');
    const searchButtonMoo = document.getElementById('top-search-button-moo');

    let currentFocusMoo = -1;

    searchInputMoo.addEventListener('input', function() {
        const queryMoo = searchInputMoo.value.trim();
        if (queryMoo.length > 0) {
            fetchSuggestionsMoo(queryMoo);
        } else {
            clearSuggestionsMoo();
        }
    });

    searchInputMoo.addEventListener('keydown', function(e) {
        const suggestionsMoo = suggestionsContainerMoo.getElementsByClassName('top-suggestion-item-moo');
        if (e.key === 'ArrowDown') {
            currentFocusMoo++;
            addActiveMoo(suggestionsMoo);
        } else if (e.key === 'ArrowUp') {
            currentFocusMoo--;
            addActiveMoo(suggestionsMoo);
        } else if (e.key === 'Enter') {
            e.preventDefault();
            if (currentFocusMoo > -1) {
                if (suggestionsMoo[currentFocusMoo]) {
                    suggestionsMoo[currentFocusMoo].click();
                }
            }
        }
    });

    searchButtonMoo.addEventListener('click', performSearchMoo);

    function fetchSuggestionsMoo(queryMoo) {
        $.ajax({
            url: '/search-bj', // 실제 서버 엔드포인트로 변경
            method: 'GET',
            data: { search: queryMoo },
            success: function(response) {
                displaySuggestionsMoo(response);
            },
            error: function(error) {
                console.error('Error fetching suggestions:', error);
            }
        });
    }

    function displaySuggestionsMoo(suggestionsMoo) {
        clearSuggestionsMoo();
        suggestionsMoo.forEach(suggestionMoo => {
            const suggestionItemMoo = document.createElement('div');
            suggestionItemMoo.textContent = suggestionMoo;
            suggestionItemMoo.classList.add('top-suggestion-item-moo');
            suggestionItemMoo.addEventListener('click', function() {
                searchInputMoo.value = suggestionMoo;
                clearSuggestionsMoo();
                performSearchMoo(); // suggestion 클릭 시 검색 실행
            });
            suggestionsContainerMoo.appendChild(suggestionItemMoo);
        });
    }

    function clearSuggestionsMoo() {
        suggestionsContainerMoo.innerHTML = '';
        currentFocusMoo = -1;
    }

    function addActiveMoo(suggestionsMoo) {
        if (!suggestionsMoo) return false;
        removeActiveMoo(suggestionsMoo);
        if (currentFocusMoo >= suggestionsMoo.length) currentFocusMoo = 0;
        if (currentFocusMoo < 0) currentFocusMoo = suggestionsMoo.length - 1;
        suggestionsMoo[currentFocusMoo].classList.add('top-suggestion-active');
        searchInputMoo.value = suggestionsMoo[currentFocusMoo].textContent; // 검색어를 변경
    }

    function removeActiveMoo(suggestionsMoo) {
        for (let i = 0; i < suggestionsMoo.length; i++) {
            suggestionsMoo[i].classList.remove('top-suggestion-active');
        }
    }
});

function performSearchMoo() {
    var queryMoo = document.getElementById('top-search-input-moo').value;

    if (queryMoo.trim() !== "") {
        location.href = '/bjsearch?search=' + encodeURIComponent(queryMoo);
    } else {
        alert('검색어를 입력해주세요.');
    }
}

document.getElementById('top-search-input-moo').addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        performSearchMoo();
    }
});
