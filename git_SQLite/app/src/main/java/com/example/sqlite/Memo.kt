package com.example.sqlite

data class Memo(var no: Long?, var content: String, var datetime: Long)
// no와 datetime의 타입을 데이터베이스 에서는 INTEGER로 정의 했는데 여기서는 Long임
// 숫자의 범위가 서로 다르기 때문에 특별한 이유가 없다면 SQLite에서 INTEGER로 선언한 것은 소스 코드에서는 Long을 사용함

// no를 Null을 허용한 이유는 PRIMARY KEY 옵션으로 값이 자동으로 증가 되기 떄문에 데이터 삽입 INSERT시에는 필요하지 않아서임

// Memo 클래스는 나아가 Insert, Select, Update, Delete에 모두 사용함
