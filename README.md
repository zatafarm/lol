이 프로젝트는 리그 오브 레전드(LoL) 선수들의 정보를 관리하고, 사용자에게 유용한 기능을 제공하는 웹 애플리케이션입니다. 주요 기능으로는 선수 리스트 출력, 실시간 정보 업데이트, 전적 검색, BJ 등록 기능 등이 있습니다.

주요 기능
1. 티어표
설명: 내부 데이터베이스에 저장된 정보를 활용해 현재 활동 중인 선수들의 리스트를 보여줍니다. 이 기능을 통해 사용자는 선수들의 등급과 정보를 쉽게 확인할 수 있습니다.
2. 엔트리
설명: AJAX를 사용하여 사용자가 요청할 때마다 최신 정보를 받아와서 화면에 표시하며, 게임 결과도 데이터베이스에 저장됩니다. 
3. 전적 검색
설명: 사용자가 입력한 검색어에 맞는 선수의 전적을 출력하는 기능입니다.
4. BJ 등록
설명: 웹 크롤링을 통해 존재하는 BJ(방송인)인지 확인한 후, RIOT API를 이용해 해당 BJ의 리그 오브 레전드 PUUID(플레이어 고유 식별자)를 데이터베이스에 저장합니다.


RIOT API의 커스텀 전적 정보 API가 중단되면서, 선수들의 경기 결과를 직접적으로 확인하는 데 어려움이 생겼습니다.
이에 따라, 웹사이트에서 엔트리를 생성하고 경기 결과를 저장하는 시스템을 개발하게 되었습니다. 
이 프로젝트는 대회를 시청하는 사람들에게 선수들의 전적을 제공하기 위해 만들어졌습니다.

시연 영상 <br>
[BJ등록](https://www.youtube.com/watch?v=HrG38XwgMI4)

[엔트리](https://www.youtube.com/watch?v=l_PiYY2PLWo&t=22s)

[전체](https://youtu.be/2eFRmbX7xBE)

사이트 도메인

[MooLand](https://mooland.xyz)
