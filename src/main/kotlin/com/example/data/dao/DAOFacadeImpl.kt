package com.example.data.dao

import com.example.data.DatabaseSingleton.dbQuery
import com.example.models.Article
import com.example.models.ArticleTable
import com.example.models.ArticleTable.id
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl : DAOFacade {
    override suspend fun allArticles(): List<Article> = dbQuery {
        return@dbQuery ArticleTable.selectAll().map(::article)
    }

    override suspend fun article(id: Int): Article? {
        return dbQuery {
            ArticleTable
                .select { ArticleTable.id eq id }
                .map(::article)
                .singleOrNull()
        }
    }

    override suspend fun addNewArticle(title: String, body: String): Int = dbQuery {
        return@dbQuery ArticleTable.insert {
            it[ArticleTable.title] = title
            it[ArticleTable.body] = body
        } get id
    }

    override suspend fun editArticle(id: Int, title: String, body: String): Boolean {
        return dbQuery {
            ArticleTable.update({ ArticleTable.id eq id }) {
                it[ArticleTable.title] = title
                it[ArticleTable.body] = body
            } > 0
        }
    }

    override suspend fun deleteArticle(id: Int): Boolean {
        return dbQuery {
            ArticleTable.deleteWhere { ArticleTable.id eq id } > 0
        }
    }
}

private fun article(row: ResultRow): Article {
    return Article(
        id = row[ArticleTable.id],
        title = row[ArticleTable.title],
        body = row[ArticleTable.body]
    )
}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allArticles().isEmpty()) {
            try {
                addNewArticle("The drive to develop!", "...it's what keeps me going.")
                //allArticles()
            } catch (exception: Exception) {
                println(exception)
            }
        }
    }
}