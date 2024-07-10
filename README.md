# "STOP"

알고리즘 트레이딩 전략을 실험해보는 앱, 간단한 블록 코딩을 통해 나만의 전략을 만들고 과거 성과를 계산해보세요.

## Info

Team: KAIST 22' 김문정 , DGIST 22' 장지원

Duration: 2024.07.04 ~ 2024.07.10

At: KAIST Mad Camp

## Language / IDE

Frontend: Kotlin, Android Studio

Backend: Django, Django REST Framework, sqlite3


## Introduction

<p>알고리즘 트레이딩 전략을 실험해보는 앱 입니다.</p> 

**“STOP”**

STOP 세 가지 의미를 가지고 있습니다.  
1. 주식하기 전 잠깐 STOP!하고, 이 앱으로 연습하자.
2. STOP 어플의 Search, ToolBar, Practice의 약자.
3. Stock ... Stocp ... Stop!?

**“Search”**

Search tab은 주식 종목을 검색할 수 있는 tab 입니다.

**“Tool Bar”**

Tool Bar tab은 나의 전략을 block colding으로 구현할 수 있는 tab 입니다.

**“Practice”**

Practice tab은 나의 거래 전략들을 통해 주식을 연습할 수 있는 tab 입니다.

## Details

### Index
- [Login](#Tab-1)
- [Search](#Tab-2)
- [ToolBar](#Tab-3)
- [Practice](#Tab-4)

### Login 
![gif1](https://github.com/coco483/Madcamp_week2/assets/133734191/d3b4846f-bbd1-4825-8c47-9591e55d08c4)  

  로그인: Splash 화면 이후에는 로그인 화면이 나옵니다. 로그인은 Google API를 사용해서 구현하였습니다. 로그린 인후의 메인 메뉴에서는 Search, ToolBox, Practce로 이동할 수 있습니다. 각 화면으로 이동할 때는 animation이 함께 합니다.


### Search 
![gif4](https://github.com/coco483/Madcamp_week2/assets/133734191/a3062372-bc2b-4e56-93a9-6ac40654e12c)  

  주식 검색: 한국투자증권API 를 활용해서 주식 목록을 받아왔습니다.  autoTextView를 활용하여 검색 기능을 구현하였습니다.  

    
  주식 종목 보기: 한국투자증권API 에서 과거 가격 정보를 가져와 graphview 라이브러리를 이용해 그래프를 그렸습니다. 3개월, 1년, 5년 단위의 그래프를 확인해볼 수 있습니다. 해당 주식의 가격과 가격 변동을 상단에서 확인해볼 수 있습니다.  

    
  즐겨 찾기 목록 추가: User의 즐겨찾기 목록을 추가해 줍니다. 로컬의 즐겨찾기 목록에 추가한 이후, 해당 리스트를 `JSON` 파일로 변경하여 `server`에 `PUT` 해줍니다.  

    
  즐겨 찾기 목록: User의 즐겨 찾기 목록을 확인할 수 있습니다. recyclerView 를 활용하여 사명, 종목코드와 상장사를 보여줍니다.
  

### ToolBar
![gif3](https://github.com/coco483/Madcamp_week2/assets/133734191/d12bb009-29fe-4a9e-aabf-02876b489666)  

  블록 코딩: Scratch와 비슷한 인터페이스의 블록 코딩을 통해 거래 조건들을 추가하고 거래 조건들을 묶어 전략으로 등록할 수 있습니다.  

  계산: 한국투자증권API 를 활용해서 블록 코딩 결과를 계산합니다. 이후 팝업창을 통해서 초기 투자금에 대한 수익률과 자산 구성을 보여줍니다.


### Practice
![gif2](https://github.com/coco483/Madcamp_week2/assets/133734191/2f754018-799f-42f0-8671-5535276a0857)  

  전략 목록 보기: Tool Bar에서 저장한 전략 목록을 보여줍니다. 이 전략 목록에서는 최고 수익률을 함께 볼 수 있습니다. 태그를 통해 각 전략과 관련이 있는 주식을 표시해 줍니다.  

  전략 수정: 전략 수정을 누르면 다시 블록 코딩 화면으로 돌아가게 됩니다.


