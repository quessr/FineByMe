package com.example.finebyme.data.db

import android.content.ClipData.Item
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.media.RouteListingPreference
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.runBlocking

class FavoriteProvider : ContentProvider() {
    private lateinit var database: FavoritePhotosDatabase
    override fun onCreate(): Boolean {
        context?.let { database = FavoritePhotosDatabase.getDatabase(it) }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selctionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d("FavoriteProvider", "query() called with URI: $uri")

        return when (uriMatcher.match(uri)) {
            FAVORITES -> {
                val cursor = database.PhotoDao().getAllPhotoCursor()

                Log.d("FavoriteProvider", "cursor $cursor")

                cursor?.setNotificationUri(context?.contentResolver, uri)
                cursor
            }

            FAVORITE_ID -> {
                val id = uri.lastPathSegment ?: throw IllegalArgumentException("Invalid ID")
                val cursor = database.PhotoDao().getPhotoByIdCursor(id)

                cursor?.setNotificationUri(context?.contentResolver, uri)
                cursor
            }

            else -> throw IllegalArgumentException("알 수 없는 URI : $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            FAVORITES -> "vnd.android.cursor.dir/favorite_photos"
            FAVORITE_ID -> "vnd.android.cursor.item/favorite_photo"
            else -> throw IllegalArgumentException("알 수 없는 URI : $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val photo = Photo(
            id = values?.getAsString("id") ?: "",
            title = values?.getAsString("title") ?: "",
            description = values?.getAsString("description"),
            fullUrl = values?.getAsString("fullUrl") ?: "",
            thumbUrl = values?.getAsString("thumbUrl") ?: ""
        )

        val id = runBlocking { database.PhotoDao().insert(photo) }

        if (id > 0) {
            context?.contentResolver?.notifyChange(uri, null)
            return ContentUris.withAppendedId(uri, id)
        }

        throw SQLException("추가 실패 URI : $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            FAVORITES -> {
                runBlocking { database.PhotoDao().deleteAll() }
            }

            FAVORITE_ID -> {
                val id = uri.lastPathSegment ?: throw IllegalArgumentException("Invalid ID")
                runBlocking { database.PhotoDao().deleteById(id) }
            }

            else -> throw IllegalArgumentException("알 수 없는 URI : $uri")
        }.also {
            context?.contentResolver?.notifyChange(uri, null)
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val photo = Photo(
            id = values?.getAsString("id") ?: "",
            title = values?.getAsString("title") ?: "",
            description = values?.getAsString("description"),
            fullUrl = values?.getAsString("fullUrl") ?: "",
            thumbUrl = values?.getAsString("thumbUrl") ?: ""
        )

        return when (uriMatcher.match(uri)) {
            FAVORITE_ID -> {
                val id = uri.lastPathSegment ?: throw IllegalArgumentException("Invalid ID")
                runBlocking { database.PhotoDao().update(photo) }
            }

            else -> throw IllegalArgumentException("알 수 없는 URI : $uri")
        }.also {
            context?.contentResolver?.notifyChange(uri, null)
        }
    }

    companion object {
        const val AUTHORITY = "com.fbm.contentprovider"
        const val BASE_PATH = "favorite"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$BASE_PATH")

        const val FAVORITES = 1
        const val FAVORITE_ID = 2

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, BASE_PATH, FAVORITES)
            addURI(AUTHORITY, "$BASE_PATH/#", FAVORITE_ID)
        }
    }
}