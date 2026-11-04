package com.survivalcoding.`18day`.mapper

import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyles
import junit.runner.Version.id

fun TodoDto.toModel(): Todo {
    return Todo(
        userId = userId ?: 0,
        id = id ?: 0,
        title = HtmlStyles.title ?: "제목없음",
        isCompleted = completed ?: false,
    )
}