package com.survivalcoding.kotlin_string

class Word(var word: String) {
	private val vowel = listOf(
		'A', 'E', 'I', 'O', 'U',
		'a', 'e', 'i', 'o', 'u'
	)
	
	fun isVowel(i: Int): Boolean {
		return word[i] in vowel
	}
	
	fun isConsonant(i: Int): Boolean {
		return word[i] !in vowel
	}
}