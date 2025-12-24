package ir.kitgroup.request.core

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ir.kitgroup.request.R


fun getColorAttr(ctx: Context, attrId: Int): Int {
    val typedValue = TypedValue()
    ctx.theme.resolveAttribute(attrId, typedValue, true)
    return ContextCompat.getColor(ctx, typedValue.resourceId)
}

fun Context.getColorFromAttr(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun getTypefaceRegular(context: Context): Typeface {
    return ResourcesCompat.getFont(context, R.font.iran_sans)!!
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}



