document.querySelectorAll('.menu-link').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault(); // 기본 클릭 동작 방지

        // 모든 섹션 숨기기
        document.querySelectorAll('.content-section').forEach(section => {
            section.style.display = 'none';
        });

        // 클릭한 링크와 연결된 섹션 표시
        const targetId = this.getAttribute('data-target');
        document.getElementById(targetId).style.display = 'block';
    });
});
