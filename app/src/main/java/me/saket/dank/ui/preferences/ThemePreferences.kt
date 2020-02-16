package me.saket.dank.ui.preferences

import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import com.f2prateek.rx.preferences2.Preference
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import io.reactivex.exceptions.Exceptions
import me.saket.dank.R
import java.io.IOException
import javax.inject.Inject

class ThemePreferences @Inject constructor(val preference: Preference<Option>) {

  val preferenceChange: Observable<Option> =
    preference.asObservable().replay().refCount()

  enum class Option(@StringRes val title: Int, @StyleRes val theme: Int, val mode: Int) {
    LIGHT(
      R.string.userprefs_theme_light,
      R.style.DankTheme,
      AppCompatDelegate.MODE_NIGHT_NO
    ),
    DARK(
      R.string.userprefs_theme_dark,
      R.style.DankTheme,
      AppCompatDelegate.MODE_NIGHT_YES
    ),

    // Add new themes here

    @RequiresApi(29)
    SYSTEM(
      R.string.userprefs_theme_system,
      R.style.DankTheme,
      AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    ),
    AUTO(
      R.string.userprefs_theme_auto,
      R.style.DankTheme,
      AppCompatDelegate.MODE_NIGHT_AUTO
    )
  }

  class Converter(private val moshi: Moshi) :
    Preference.Converter<Option> {
    private var adapter: JsonAdapter<Option>? = null
    override fun deserialize(serialized: String): Option {
      val adapter = adapter()
      return try {
        adapter!!.fromJson(serialized)!!
      } catch (e: IOException) {
        throw Exceptions.propagate(e)
      }
    }

    override fun serialize(value: Option): String {
      val adapter = adapter()
      return adapter!!.toJson(value)
    }

    private fun adapter(): JsonAdapter<Option>? {
      if (adapter == null) {
        adapter = moshi.adapter(Option::class.java)
      }
      return adapter
    }

  }
}