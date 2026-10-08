package com.survivalcoding.kotlin_string

class Word(var word: String) {
	private val vowel = listOf('a', 'e', 'i', 'o', 'u')
	
	fun isVowel(i: Int): Boolean {
		return word[i] in vowel
	}
	
	fun isConsonant(i: Int): Boolean {
		return word[i] !in vowel
	}
}