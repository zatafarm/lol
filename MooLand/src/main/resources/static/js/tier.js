document.addEventListener('DOMContentLoaded', function() {
    const buttons = document.querySelectorAll('.moo-navbar button');
    function getQueryParams() {
        const params = {};
        const queryString = window.location.search.substring(1);
        const queries = queryString.split('&');
        queries.forEach(query => {
            const [key, value] = query.split('=');
            params[key] = decodeURIComponent(value); 
        });
        return params;
    }

    const params = getQueryParams();
    const position = params['position'];

    if (position) {
        const activeButton = document.querySelector(`.moo-navbar button[data-position="${position}"]`);
        if (activeButton) {
            activeButton.style.backgroundColor = 'black';
        }
    }

    buttons.forEach(button => {
        button.addEventListener('click', function() {
            const currentActiveButton = document.querySelector('.moo-navbar button[style*="background-color: black"]');
            if (currentActiveButton) {
                currentActiveButton.style.backgroundColor = '';
            }
            this.style.backgroundColor = 'black';
        });
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const userContainers = document.querySelectorAll('.user-container');

    userContainers.forEach(container => {
        container.addEventListener('mouseenter', function() {
            const popup = this.querySelector('.popup');
            const rect = this.getBoundingClientRect();
            const popupHeight = popup.offsetHeight;
            const viewportHeight = window.innerHeight;

            if (rect.top > popupHeight) {

                popup.style.bottom = '75%';
                popup.style.top = 'auto';
                popup.style.marginBottom = '10px';
            } else if (viewportHeight - rect.bottom > popupHeight) {
         
                popup.style.top = '75%';
                popup.style.bottom = 'auto';
                popup.style.marginTop = '10px';
            } else {
        
                popup.style.bottom = '75%';
                popup.style.top = 'auto';
                popup.style.marginBottom = '10px';
            }

       
            const BJID = popup.getAttribute('data-bjid'); 
            if (BJID) {
                fetch(`api/live?BJID=${BJID}`, { 
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
				.then(response => response.json())
				    .then(data => {
				        
				       
				        const existingLink = popup.querySelector('.live-link');
				        if (!existingLink) {

							const liveLink = document.createElement('a');
							liveLink.href = data.url;
							liveLink.innerHTML = '<br><span style="color: red;">LIVE</span> 방송 중'; // LIVE 텍스트만 빨갛게
							liveLink.classList.add('live-link');
							liveLink.target = '_blank'; 
							popup.appendChild(liveLink);
				        }
				    })
				    .catch(() => {
				    });
				} else {
				    console.error('BJID not found');
				   
				}

        });
    });
});

