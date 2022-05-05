package win.hapsunday.mplayer.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import win.hapsunday.mplayer.model.ConfigModel
import win.hapsunday.mplayer.model.UpdateModel

fun Any?.printMsg() {
    Log.e("xxxxxxH", "$this")
}

const val KEY_ACCOUNT = "KEY_ACCOUNT"
const val KEY_PASSWORD = "KEY_PASSWORD"
const val KEY_IS_LOGIN = "KEY_IS_LOGIN"

const val KEY_CONFIG = "KEY_CONFIG"
const val KEY_UPDATE = "KEY_UPDATE"

const val KEY_AD_INVOKE_TIME = "KEY_AD_INVOKE_TIME"
const val KEY_AD_REAL_TIME = "KEY_AD_REAL_TIME"
const val KEY_AD_SHOWN = "KEY_AD_SHOWN"
const val KEY_AD_SHOWN_INDEX = "KEY_AD_SHOWN_INDEX"
const val KEY_AD_LAST_TIME = "KEY_AD_LAST_TIME"

val mmkv by lazy {
    MMKV.defaultMMKV()
}

val gson by lazy {
    Gson()
}


private var config
    get() = mmkv.getString(KEY_CONFIG, "") ?: ""
    set(value) {
        mmkv.putString(KEY_CONFIG, value)
    }

var configModel
    get() = (config.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, ConfigModel::class.java)
    }
    set(value) {
        config = gson.toJson(value)
    }

private var update
    get() = mmkv.getString(KEY_UPDATE, "") ?: ""
    set(value) {
        mmkv.putString(KEY_UPDATE, value)
    }

var updateModel
    get() = (update.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, UpdateModel::class.java)
    }
    set(value) {
        update = gson.toJson(value)
    }

var account
    get() = mmkv.getString(KEY_ACCOUNT, "") ?: ""
    set(value) {
        mmkv.putString(KEY_ACCOUNT, value)
    }

var password
    get() = mmkv.getString(KEY_PASSWORD, "") ?: ""
    set(value) {
        mmkv.putString(KEY_PASSWORD, value)
    }

var isLogin
    get() = mmkv.getBoolean(KEY_IS_LOGIN, false)
    set(value) {
        mmkv.putBoolean(KEY_IS_LOGIN, value)
    }

var adInvokeTime
    get() = mmkv.getInt(KEY_AD_INVOKE_TIME, 0)
    set(value) {
        mmkv.putInt(KEY_AD_INVOKE_TIME, value)
    }

var adRealTime
    get() = mmkv.getInt(KEY_AD_REAL_TIME, 0)
    set(value) {
        mmkv.putInt(KEY_AD_REAL_TIME, value)
    }

private var adShown
    get() = mmkv.getString(KEY_AD_SHOWN, "") ?: ""
    set(value) {
        mmkv.putString(KEY_AD_SHOWN, value)
    }

var adShownList
    get() = (adShown.ifBlank {
        "{}"
    }).let {
        gson.fromJson<List<Boolean>>(it, object : TypeToken<List<Boolean>>() {}.type)
    }
    set(value) {
        adShown = gson.toJson(value)
    }

var adShownIndex
    get() = mmkv.getInt(KEY_AD_SHOWN_INDEX, 0)
    set(value) {
        mmkv.putInt(KEY_AD_SHOWN_INDEX, value)
    }

var adLastTime
    get() = mmkv.getLong(KEY_AD_LAST_TIME, 0)
    set(value) {
        mmkv.putLong(KEY_AD_LAST_TIME, value)
    }