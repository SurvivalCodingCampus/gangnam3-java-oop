package kotlin_basic


/*fun check  ( i:Int,hi:Int){
    if(i-hi>=0){
        throw IllegalArgumentException("유효하지 않은 범위입니다 범위 최대값 초과")
    }
    else if(hi<0){
        throw IllegalArgumentException("유효하지 않은 범위입니다 범위 음수")
    }

}
*/
//함수로 특정 값을 조절하는것은 힘들어 보인다

////
class word(var word: String) {
    var hi = word.length

    fun isVowel(): Boolean {
        var hi = word.length
        var j = 0
        var k = 0;
        repeat(hi) {
//hello
            /* when (word[j]){
                 "a","e","i","o","u"->return true;
             } */
            //substring(a,b) replace("a","b"),split(c),length,uppercase(),lowercase(),indexOf('k'),contains('L'),toString("L") string[a] 하면 char 가 튀어나오므로 string 으로 교정
            when (word[j].toString()) {
                "a", "e", "i", "o", "u", "A", "E", "I", "O", "U" -> println("${j + 1}번째 글씨가 모음입니다")
            }
            j = j + 1
        }
        if (k > 0) {
            return true;
        } else {
            return false
        }
    }

    fun isConstant(i: Int): Boolean {

        var xi = i
        var hi = hi
        if (i > hi) {
            println("배열을 초과한 범위입니다")
            return false
        }
        if (i < 0) {
            println("배열범위가 음수입니다")
            return false
        }
        //check(xi,hi)
        when (word[i - 1].toString()) {
            "b", "B", "c", "C", "d", "D", "f", "F", "g", "G", "H", "h", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "p", "P", "Q", "q", "r", "R", "s", "S", "T", "t", "V", "v", "w", "W", "X", "x", "Y", "y", "z", "Z" -> {
                println("${i}번쨰 글자는 자음입니다")
                return true
            }

            else -> {
                println("${i}번째 글자는 모음입니다")
                return false
            }
        }
    }

    fun isConstant2(i: Int): Boolean {
        var xi = i
        var hi = hi
        if (i > hi) {
            println("배열을 초과한 범위입니다")
            return false
        }
        if (i < 0) {
            println("배열범위가 음수입니다")
            return false
        }
        //check(xi,hi)
        if (word[i - 1].toString().lowercase() !in "aeiou") {
            println("${i}번쨰 글자는 자음입니다")
            return true
        } else {
            println("${i}번째 글자는 모음입니다")
            return false
        }

    }
}

fun main() {
    var x = word("hello")
    x.isVowel()
    x.isConstant(3)
    x.isConstant(2)
    x.isConstant2(2)
    x.isConstant(7)
    x.isConstant(-7)
    x.isConstant2(7)
    x.isConstant2(-7)
}////////