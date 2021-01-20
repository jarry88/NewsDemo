package com.example.newsdemoapplication.room

import android.content.Context

/**
 * DAO被传递到存储库构造函数，而不是整个数据库。这是因为它只需要访问DAO，因为DAO包含数据库的所有读/写方法。无需将整个数据库公开到存储库。
Todo列表是公共财产。通过LiveData从Room获取单词列表进行初始化；之所以可以这样做，是因为我们定义了getAlphabetizedTodoList返回LiveData的方法。Room在单独的线程上执行所有查询。然后，当LiveData数据已更改时，observed 将在主线程上通知观察者。
该suspend修饰符告诉编译器，这需要从协同程序或其他暂停功能调用。
 */
class WordRepository(val WordDao:WordDao) {
    val allWord =WordDao.getAllWords()
}