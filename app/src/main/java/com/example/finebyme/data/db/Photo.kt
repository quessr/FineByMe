package com.example.finebyme.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

//사용자가 즐겨찾기한 사진들
@Parcelize
/**@Parcelize 어노테이션은 Kotlin에서 Parcelable 인터페이스의 구현을 자동화해 주는 기능입니다.*/
@Entity(tableName = "favorite_photos")
data class Photo(
    @PrimaryKey()
    var id: String,
    val title: String,
    val description: String?,
    val fullUrl: String,
    val thumbUrl: String,
) : Parcelable
/**Parcelable은 Android에서 객체를 직렬화하기 위한 인터페이스입니다.
 * 객체를 직렬화하면 객체의 상태를 저장하거나 다른 컴포넌트 간에 객체를 전달할 수 있게 됩니다.
 * 특히, Android에서는 Parcelable 인터페이스를 사용하여 객체를 Intent나 Bundle을 통해 전달합니다.
 *
 * 요약 정리:
 * Parcelable은 객체를 직렬화하여 Android 컴포넌트 간에 데이터를 전달하기 위한 인터페이스입니다.
 * @Parcelize는 Parcelable 구현을 자동화해 주는 Kotlin의 어노테이션으로, 이를 통해 직렬화 관련 코드를 자동으로 생성할 수 있습니다.*/