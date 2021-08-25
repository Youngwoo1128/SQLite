package com.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteOpenHelper(context: Context, name: String, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        // onCreate 에서 테이블 생성 쿼리를 작성하고 실행하면 됨
        // 데이터베이스가 생성되어 있으면 더 이상 실행되지 않음
        // 테이블 생성 쿼리를 "String" 으로 입력한후 db의 exeSQL() 메서드에 전달해서 실행함
        val create = "create table memo (" + "no integer primary key, " + "content text, " + "datetime integer" + ")"

        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // onUpgrade는?
        // SQLite에 전달 되는 버전 정보가 변경되었을 때 현재 생성되어 있는 데이터베이스의 버전과 비교해서 더 높으면 호출됨
        // 버전 변경 사항이 없으면 호출되지 않음

    }

    // SQLiteOpenHelper를 이용해서 값을 입력할 때는 코틀린의 Map 클래스 처럼 키&값 형태로 사용되는 ContentValue 클래스를 사용함
    // ContentValues 에 put 메소드 put("컬럼명", 값) 으로 저장함
    fun insertMemo(memo: Memo){
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        // 상속받은 SQLiteOpenHelper에 이미 구현된 writableDatabase 에 테이블명과 함께 앞에서 작성한 값을 전달해서 insert()하고, 사용한 후에는 close()를 호출해서 꼭 닫아줘야함
        val wd = writableDatabase
        wd.insert("memo", null, values)
        wd.close()
    }

    // 조회 메소드는 반환값이 있으므로 메소드의 가장 윗줄에 반환할 값을 변수로 선언하고
    // 가장 아랫줄에서 반환하는 코드를 작성한 후 그 사이에 구현 코드를 작성하는 것이 좋다.
    fun selectMemo(): MutableList<Memo>{
        val list = mutableListOf<Memo>()

        //메모의 전체 데이터를 조회하는 쿼리
        val select = "select * from memo"

        // 읽기 전용 데이터베이스
        var rd = readableDatabase

        // 데이터베이스의 rawQuery() 메소드에 앞에서 작성해둔 쿼리를 담아서 실행하면 커서(cursor) 형태로 값이 반환됨
        // Cursor란? : 데이터셋을 처리할 때 현재 위치를 포함하는 데이터 요소
        // 커서를 사용하면 쿼리를 통해 반환된 데이터셋을 반복문으로 반복하며 하나씩 처리할 수 있음
        // 반복할때마다 커서가 현재 위치를 가리키고 있어 "데이터읽기 -> 다음 줄 이동" 이렇게 단순 로직으로 데이터를 쉽게 처리 할 수 있음
        val cursor = rd.rawQuery(select, null)

        while (cursor.moveToNext()){
            // 반복문 돌면서 테이블에 정의된 3개의 컬럼에서 값을 꺼내기
            val no = cursor.getLong(cursor.getColumnIndex("no"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Memo(no, content, datetime))
        }
        cursor.close()
        rd.close()

        return list
    }

    // 수정 메소드
    // Insert와 동일하게 ContentValues를 사용해서 수정할 값을 저장함
    fun updateMemo(memo: Memo){
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)
        // writeableDatabase의 update 메소드를 사용해서 수정한 다음 close를 호출함
        // update메소드의 파라미터에는 총 4개가 있는데
        // 테이블명, 수정할 값, 수정할 조건 이 순서임
        // 수정할 조건은 PRIMARY KEY로 지정된 컬럼을 사용하며 여기서는 PRIMARY KEY인 컬럼이 no이기 때문에 "no = 숫자" 가 됨
        // 네번째에는 그냥 null

        val wd = writableDatabase
        wd.update("memo", values, "no = ${memo.no}", null)
        wd.close()
    }

    //삭제 메소드
    // SQLiteHelper 클래스르 사용하면 앞에서 처럼 insert(), update()메소드 사용법만 알면 쿼리를 몰라도 DB사용이 가능함
    // 하지만 복잡한 DB를 다룰 때는 쿼리를 직접 작성하면 더 정밀하게 다룰 수 있으므로 쿼리를 공부하는 것은 중요함
    // 삭제 메소드는 (DELETE)는 쿼리를 직접 입력해서 데이터를 삭제하는 코드로 작성해보겠음
    // 삭제 쿼리는 DELETE FROM 테이블명 FROM 조건식 이렇게 생김
    fun deleteMemo(memo: Memo){
        val delete = "delete from memo where no = ${memo.no}"

        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }

}