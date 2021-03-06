package me.saket.dank.utils

import android.content.Context
import androidx.core.content.ContextCompat
import me.saket.dank.R
import me.saket.dank.widgets.span.CenterAlignedImageSpan
import net.dean.jraw.models.VoteDirection

object ColorReplicationIcons {
  @JvmStatic fun pushIcon(context: Context, builder: Truss, sizeDimenResId: Int,
                          drawableResId: Int, tintColor: Int, lineSpacingExtra: Int) {
    val size = context.resources.getDimensionPixelSize(sizeDimenResId)
    val icon = context.resources.getDrawable(drawableResId, null).mutate()
    icon.setTint(tintColor)
    icon.setBounds(0, 0, size, size)

    builder
        .pushSpan(CenterAlignedImageSpan(icon, lineSpacingExtra))
        .append("icon")
        .popSpan()
  }

  @JvmStatic fun pushVoteIcon(context: Context, builder: Truss, vote: VoteDirection?, color: Int, iconSizeResId: Int, lineSpacingExtra: Int) {
    val icon = when (vote) {
      VoteDirection.DOWN -> R.drawable.ic_arrow_downward_24dp
      VoteDirection.UP -> R.drawable.ic_arrow_upward_24dp
      VoteDirection.NONE, null -> return
    }

    pushIcon(context, builder, iconSizeResId, icon, color, lineSpacingExtra)
  }

  @JvmStatic fun pushOPCommentIcon(context: Context, builder: Truss) {
    val color = ContextCompat.getColor(context, R.color.submission_comment_byline_author_op)
    pushIcon(context, builder, R.dimen.submission_comment_byline, R.drawable.ic_person_12dp, color, 0)
  }
}
