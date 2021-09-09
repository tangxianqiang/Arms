package com.blcodes.mvvm_framework.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blcodes.mvvm_framework.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

object SettingUtil {
    private const val MANUFACTURER_HUAWEI = "huawei"
    private const val MANUFACTURER_MEIZU = "meizu"
    private const val MANUFACTURER_SONY = "sony"
    private const val MANUFACTURER_OPPO = "oppo"
    private const val MANUFACTURER_LG = "lg"
    private const val MANUFACTURER_LETV = "letv"
    private const val MANUFACTURER_360 = "360"
    private const val MANUFACTURER_XIAOMI = "xiaomi"

    @SuppressLint("InflateParams")
    @JvmStatic
    fun jumpPermissionSetting(context: Context, cancel: (() -> Unit)? = null, launchSetting: (intent: Intent) -> Unit) {
        val builder = AlertDialog.Builder(context, R.style.DialogSetting)
        val dialog: AlertDialog?
        builder.setPositiveButton("确定") { dialog, _ ->
            val intent = getJumpIntent(context)
            if (intent == null) {
                showErrorSettingDialog(context as Activity)
                return@setPositiveButton
            } else {
                dialog.dismiss()
                launchSetting.invoke(intent)
            }
        }
        builder.setNegativeButton("取消") { _, _ ->
            cancel?.invoke()
        }
        builder.setTitle("系统提示")
        builder.setMessage(R.string.permission_fail)
        dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED)
        dialog.setCancelable(false)
    }

    fun jumpPermissionSetting(view: View) {
        Snackbar.make(view, R.string.permission_fail, Snackbar.LENGTH_LONG).apply {
            setAction(R.string.confirm) {
                val intent = getJumpIntent(view.context)
                try {
                    view.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            show()
        }
    }

    private fun showErrorSettingDialog(activity: Activity) {
        val builder = AlertDialog.Builder(activity, R.style.DialogSetting)
        builder.setPositiveButton("确定") { _, _ ->
            activity.finish()
        }
        builder.setNegativeButton("取消") { _, _ ->
            activity.finish()
        }
        builder.setTitle("系统提示")
        builder.setMessage("没有找到入口，请到系统设置界面打开权限")
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED)
        dialog.setCancelable(false)
    }

    /**
     * Get the Intent of the Special room.
     */
    @JvmStatic
    fun getJumpIntent(context: Context): Intent? {
        val pkg = context.packageName
        val pm = context.packageManager
        val sysManufacturer = Build.MANUFACTURER.toLowerCase(Locale.getDefault())
        var settingIntent: Intent? = null

        if (contains(sysManufacturer, MANUFACTURER_HUAWEI)) {
            val intent = Intent()
            intent.putExtra("packageName", pkg)
            intent.component = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            }
        } else if (contains(sysManufacturer, MANUFACTURER_MEIZU)) {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", pkg)
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            }
        } else if (contains(sysManufacturer, MANUFACTURER_SONY)) {
            val intent = Intent()
            intent.putExtra("packageName", pkg)
            intent.component =
                ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            }
        } else if (contains(sysManufacturer, MANUFACTURER_OPPO)) {
            val intent = Intent()
            intent.putExtra("packageName", pkg)
            intent.component = ComponentName(
                "com.color.safecenter",
                "com.color.safecenter.permission.PermissionManagerActivity"
            )
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            }
        } else if (contains(sysManufacturer, MANUFACTURER_LG)) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.putExtra("packageName", pkg)
            intent.component = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AccessLockSummaryActivity"
            )
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            }
        } else if (contains(sysManufacturer, MANUFACTURER_LETV)) {
            val intent = Intent()
            intent.putExtra("packageName", pkg)
            intent.component = ComponentName(
                "com.letv.android.letvsafe",
                "com.letv.android.letvsafe.PermissionAndApps"
            )
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            }
        } else if (contains(sysManufacturer, MANUFACTURER_360)) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.putExtra("packageName", pkg)
            intent.component = ComponentName(
                "com.qihoo360.mobilesafe",
                "com.qihoo360.mobilesafe.ui.index.AppEnterActivity"
            )
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            }
        } else if (contains(sysManufacturer, MANUFACTURER_XIAOMI)) {
            // v5
            var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$pkg")
            if (checkIntent(pm, intent)) {
                settingIntent = intent
            } else {
                intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                intent.putExtra("extra_pkgname", pkg)

                // v6 and v7
                intent.setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
                )
                if (checkIntent(pm, intent)) {
                    settingIntent = intent
                } else {
                    // v8
                    intent.setClassName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity"
                    )
                    if (checkIntent(pm, intent)) {
                        settingIntent = intent
                    }
                }
            }
        } else {
            return getCommonSettingJumpIntent(context)
        }

        if (settingIntent == null) {
            getCommonSettingJumpIntent(context)
        }

        return settingIntent
    }

    /**
     * Get the Intent of the normal rom.
     */
    private fun getCommonSettingJumpIntent(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return if (checkIntent(context.packageManager, intent)) intent else {
            Intent(Settings.ACTION_SETTINGS)
        }
    }

    private fun contains(sysManufacturer: String, targetManufacturer: String) =
        sysManufacturer.contains(targetManufacturer)

    private fun checkIntent(pm: PackageManager, intent: Intent) = pm.queryIntentActivities(
        intent, PackageManager.MATCH_DEFAULT_ONLY
    ).size > 0
}