package com.survivalcoding.kotlin_string

class Word(var word: String) {
	private val regex = Regex("^[a-zA-Z]+$")  // 알파벳 분별용 정규표현식
	private val vowel = listOf(
		'A', 'E', 'I', 'O', 'U',
		'a', 'e', 'i', 'o', 'u'
	)
	
	fun isAlphabet(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
	
	fun isVowel(i: Int): Boolean = isAlphabet(word[i]) && word[i] in vowel
	
	fun isConsonant(i: Int): Boolean = isAlphabet(word[i]) && word[i] !in vowel
}