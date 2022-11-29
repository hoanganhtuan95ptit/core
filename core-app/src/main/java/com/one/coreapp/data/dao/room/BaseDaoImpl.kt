package com.one.coreapp.data.dao.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.one.core.utils.extentions.toJson
import com.one.core.utils.extentions.toListObject
import com.one.core.utils.extentions.toObject
import com.one.core.utils.extentions.validate
import com.one.coreapp.data.dao.BaseDao
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Dao
abstract class BaseDaoImpl<E, R> : BaseDao<E> {

    protected var classRoom: Class<R>
    protected var classEntity: Class<E>

    protected var nameRoom: String
    protected var nameEntity: String

    init {
        classRoom = getClass(1)
        classEntity = getClass(0)

        nameRoom = classRoom.simpleName
        nameEntity = classEntity.simpleName
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getClass(position: Int): Class<T> {
        var genericSuperClass: Type? = javaClass.genericSuperclass

        var parametrizedType: ParameterizedType? = null
        while (parametrizedType == null) {
            if (genericSuperClass is ParameterizedType) {
                parametrizedType = genericSuperClass
            } else {
                genericSuperClass = (genericSuperClass as Class<*>?)!!.genericSuperclass
            }
        }

        return parametrizedType.actualTypeArguments[position] as Class<T>
    }

    override fun saveOrUpdate(vararg ts: E) = insert(ts.toList().toRoom())

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insert(ts: List<R>)

    override fun findAll(): List<E> = doFind("SELECT * FROM $nameRoom")

    protected fun doFind(query: String): List<E> = doFind(SimpleSQLiteQuery(query)).toEntity()

    @RawQuery
    protected abstract fun doFind(query: SupportSQLiteQuery): List<R>

    open fun List<E>.toRoom(): List<R> = toJson().toListObject(classRoom).validate {
        validateRoom(this)
    }

    open fun E.toRoom(): R = toJson().toObject(classRoom).apply {
        validateRoom(this)
    }

    abstract fun validateRoom(r: R)

    open fun List<R>.toEntity(): List<E> = toJson().toListObject(classEntity).validate {
        validateEntity(this)
    }

    open fun R.toEntity(): E = toJson().toObject(classEntity).apply {
        validateEntity(this)
    }

    abstract fun validateEntity(e: E)

}