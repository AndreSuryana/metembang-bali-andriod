package com.andresuryana.metembangbali.helper

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.andresuryana.metembangbali.R
import com.andresuryana.metembangbali.data.model.Submission
import com.andresuryana.metembangbali.data.model.Tembang
import com.andresuryana.metembangbali.ui.auth.signin.SignInActivity
import com.andresuryana.metembangbali.utils.ErrorStateConstants.UNAUTHORIZED
import com.andresuryana.metembangbali.utils.Ext.spaceCamelCase
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


object Helpers {

    fun formatCategory(tembang: Tembang): String {
        return if (tembang.subCategory != null) {
            "${tembang.category.spaceCamelCase()}, ${tembang.subCategory.spaceCamelCase()}"
        } else {
            tembang.category.spaceCamelCase()
        }
    }

    fun formatSubmissionCategory(submission: Submission): String {
        return if (submission.subCategory != null) {
            "${submission.category.spaceCamelCase()}, ${submission.subCategory.spaceCamelCase()}"
        } else {
            submission.category.spaceCamelCase()
        }
    }

    fun formatDate(date: Date, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
    }

    fun snackBarError(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
        return Snackbar.make(view, message, duration).apply {
            setBackgroundTint(ContextCompat.getColor(view.context, R.color.color_danger))
        }
    }

    fun snackBarNetworkError(
        view: View,
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        onClickListener: (View) -> Unit
    ): Snackbar {
        return Snackbar.make(view, message, duration).apply {
            setBackgroundTint(ContextCompat.getColor(view.context, R.color.color_danger))
            setAction(R.string.btn_try_again, onClickListener)
            setActionTextColor(Color.WHITE)
        }
    }

    fun snackBarWarning(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
        return Snackbar.make(view, message, duration).apply {
            setBackgroundTint(ContextCompat.getColor(view.context, R.color.color_warning))
        }
    }

    fun snackBarSuccess(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
        return Snackbar.make(view, message, duration).apply {
            setBackgroundTint(ContextCompat.getColor(view.context, R.color.color_success))
        }
    }

    fun generateFilename(prefix: String, extension: String): String {
        val formatted = SimpleDateFormat("yyyyMMdd_hhmmss", Locale.getDefault()).format(Date())
        return "/${prefix}_${formatted}.${extension}"
    }

    fun checkErrorState(view: View, error: String) {
        when (error.lowercase()) {
            UNAUTHORIZED -> {
                snackBarError(view, error).show()
                Timer().schedule(1500L) {
                    Intent(view.context, SignInActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        view.context.startActivity(it)
                    }
                }
            }
        }
    }
}