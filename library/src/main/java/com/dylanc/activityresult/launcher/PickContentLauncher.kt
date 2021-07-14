@file:Suppress("unused")

package com.dylanc.activityresult.launcher

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import com.dylanc.callbacks.Callback0
import com.dylanc.callbacks.Callback2
import java.io.File

/**
 * @author Dylan Cai
 */
class PickContentLauncher(caller: ActivityResultCaller) :
  BaseActivityResultLauncher<String, Uri>(caller, PickContentContract()) {

  private val requestPermissionLauncher = RequestPermissionLauncher(caller)

  @JvmOverloads
  fun launch(
    input: String,
    onActivityResult: ActivityResultCallback<Uri?>,
    onPermissionDenied: Callback0,
    onExplainRequestPermission: Callback0? = null
  ) {
    requestPermissionLauncher.launch(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      onGranted = { launch(input, onActivityResult) },
      onPermissionDenied,
      onExplainRequestPermission
    )
  }

  fun launch(input: String?, onActivityResult: Callback2<Uri?, File?>) {
    launch(input) { uri ->
      if (uri != null) {
        onActivityResult(uri, uri.copyToCacheFile(context))
      } else {
        onActivityResult(null, null)
      }
    }
  }

  @JvmOverloads
  fun launch(
    input: String,
    onActivityResult: Callback2<Uri?, File?>,
    onPermissionDenied: Callback0,
    onExplainRequestPermission: Callback0? = null
  ) {
    requestPermissionLauncher.launch(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      onGranted = { launch(input, onActivityResult) },
      onPermissionDenied,
      onExplainRequestPermission
    )
  }

  @JvmOverloads
  fun launchForImage(
    onActivityResult: ActivityResultCallback<Uri?>,
    onPermissionDenied: Callback0,
    onExplainRequestPermission: Callback0? = null
  ) = launch("image/*", onActivityResult, onPermissionDenied, onExplainRequestPermission)

  @JvmOverloads
  fun launchForImage(
    onActivityResult: Callback2<Uri?, File?>,
    onPermissionDenied: Callback0,
    onExplainRequestPermission: Callback0? = null
  ) = launch("image/*", onActivityResult, onPermissionDenied, onExplainRequestPermission)

  @JvmOverloads
  fun launchForVideo(
    onActivityResult: ActivityResultCallback<Uri?>,
    onPermissionDenied: Callback0,
    onExplainRequestPermission: Callback0? = null
  ) = launch("video/*", onActivityResult, onPermissionDenied, onExplainRequestPermission)

  @JvmOverloads
  fun launchForVideo(
    onActivityResult: Callback2<Uri?, File?>,
    onPermissionDenied: Callback0,
    onExplainRequestPermission: Callback0? = null
  ) = launch("video/*", onActivityResult, onPermissionDenied, onExplainRequestPermission)
}

class PickContentContract : ActivityResultContract<String, Uri>() {
  override fun createIntent(context: Context, input: String?) =
    Intent(Intent.ACTION_PICK).apply {
      type = input
    }

  override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
    return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data!!
  }
}