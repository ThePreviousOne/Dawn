package me.saket.dank.ui.preferences

import android.content.Context
import com.f2prateek.rx.preferences2.Preference
import me.saket.dank.utils.NestedOptionsPopupMenu
import me.saket.dank.utils.NestedOptionsPopupMenu.MenuStructure.SingleLineItem
import me.saket.dank.utils.Optional

class ThemePreferencePopup(c: Context, val preferences: Preference<ThemePreferences.Option>): NestedOptionsPopupMenu(c) {

  init {
    val options = mutableListOf<ThemePreferences.Option>()
    for (option in ThemePreferences.Option.values()) {
      options.add(option)
    }
    val menuOptions = ArrayList<SingleLineItem>()
    for (index in options.indices) {
      val option = options[index]
      menuOptions.add(
        SingleLineItem.create(
          index, c.getString(option.title), android.R.color.transparent
        )
      )
    }
    val menuStructure = MenuStructure.create(Optional.empty(), menuOptions)
    createMenuLayout(c, menuStructure)
  }

  override fun handleAction(c: Context, index: Int) {
    val clickedOption: ThemePreferences.Option = ThemePreferences.Option.values()[index]
    preferences.set(clickedOption)
    dismiss()
  }

  class Builder(val preferences: Preference<ThemePreferences.Option>) {
    fun build(c: Context): ThemePreferencePopup {
      return ThemePreferencePopup(c, preferences)
    }
  }

  companion object {
    @JvmStatic
    fun builder(preferences: Preference<ThemePreferences.Option>): Builder {
      return Builder(preferences)
    }
  }
}