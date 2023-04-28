package com.xuqingkai.common

import android.content.Context
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.customwheelpicker.CustomWheelPickerDialog
import com.kongzue.dialogx.customwheelpicker.interfaces.OnCustomWheelPickerSelected
import com.kongzue.dialogx.datepicker.DatePickerDialog
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener
import com.kongzue.dialogx.style.IOSStyle

class Dialog {
    private val ALERT_TITLE_TEXT = "提示";
    private val ALERT_OK_TEXT = "确定";

    private val CONFIRM_TITLE_TEXT = "确认";
    private val CONFIRM_OK_TEXT = "确定";
    private val CONFIRM_CANCEL_TEXT = "取消";
    private val CONFIRM_OTHER_TEXT = "其他";
    private lateinit var mContext: Context;
    private var mDarkTheme = false;
    open var title: String? = null;
    open var message: String? = null;
    open var okText: String? = null;
    open var cancelText: String? = null;
    open var otherText: String? = null;

    constructor(){}
    constructor(context: Context) {
        mContext = context
    }

    fun setTitle(text:String?): Dialog{
        title = text;
        return this;
    }
    fun setMessage(text:String?): Dialog{
        message = text;
        return this;
    }
    fun setOkText(text:String?): Dialog{
        okText = text;
        return this;
    }
    fun setCancelText(text:String?): Dialog{
        cancelText = text;
        return this;
    }
    fun tip(ok: (()->Unit) = {}){
        tipDialogX(ok);
    }

    fun alert(ok: (()->Unit) = {}){
        alertDialogX(ok);
    }
    fun alert(msg:String?=null, ok: (()->Unit) = {}){
        message = msg;
        alert(ok);
    }
    fun confirm(ok: (()->Unit) = {}, cancel: (()->Unit) = {}, other: (()->Unit)? = null){
        confirmDialogX(ok, cancel ,other);
    }
    fun confirm(msg:String?=null, ok: (()->Unit) = {}, cancel: (()->Unit) = {}, other: (()->Unit)? = null){
        message = msg;
        confirm(ok, cancel ,other);
    }
    fun prompt(ok: ((result: String)->Unit) = {}, cancel: (()->Unit) = {}){
        promptDialogX(ok, cancel);
    }
    private fun tipDialogX(ok: (()->Unit) = {}){
        PopTip.show(title).showShort();
    }
    private fun alertDialogX(ok: (()->Unit) = {}){
        var dialog = MessageDialog.build();
        dialog.isCancelable = true;
        dialog.style = IOSStyle.style()
        if(mDarkTheme){ dialog.theme = DialogX.THEME.DARK; }
        if(!title.isNullOrEmpty()){ dialog.title = title ?: ALERT_TITLE_TEXT; }
        if(!message.isNullOrEmpty()){ dialog.message = "\r\n" + message; }
        dialog.setOkButton(ALERT_OK_TEXT) { baseDialog, view ->
            ok();
            false
        };
        dialog.show();
    }
    private fun confirmDialogX(ok: (()->Unit) = {}, cancel: (()->Unit) = {}, other: (()->Unit)? = null){
        var dialog = MessageDialog.build();
        dialog.isCancelable = true;
        dialog.style = IOSStyle.style()
        if(mDarkTheme){ dialog.theme = DialogX.THEME.DARK; }
        if(!title.isNullOrEmpty()){ dialog.title = title ?: CONFIRM_TITLE_TEXT; }
        if(!message.isNullOrEmpty()){ dialog.message = "\r\n" + message; }
        dialog.setOkButton(okText ?: CONFIRM_OK_TEXT) { baseDialog, view ->
            ok();
            false
        }
        dialog.setCancelButton(cancelText ?: CONFIRM_CANCEL_TEXT) { baseDialog, view ->
            cancel();
            false
        }
        if(other != null){
            dialog.setOtherButton(otherText ?: CONFIRM_OTHER_TEXT) { baseDialog, view ->
                other();
                false
            }
        }
        dialog.show();
    }

    var inputHintText: String? = null;
    fun setInputHintText(text:String?): Dialog{
        inputHintText = text;
        return this;
    }
    var inputText: String? = null;
    fun setInputText(text:String?): Dialog{
        inputText = text;
        return this;
    }
    private fun promptDialogX(ok: ((result: String)->Unit) = {}, cancel: (()->Unit) = {}){
        var dialog = InputDialog.build();
        dialog.isCancelable = true;
        dialog.isAutoShowInputKeyboard = false;
        dialog.style = IOSStyle.style()
        if(mDarkTheme){ dialog.theme = DialogX.THEME.DARK; }
        if(!title.isNullOrEmpty()){ dialog.title = title ?: CONFIRM_TITLE_TEXT; }
        if(!message.isNullOrEmpty()){ dialog.message = "\r\n" + message; }
        if(!inputHintText.isNullOrEmpty()){ dialog.inputHintText = inputHintText;}
        if(!inputText.isNullOrEmpty()){ dialog.inputText = inputText;}
        dialog.setOkButton(CONFIRM_OK_TEXT) { baseDialog, view, text ->
            ok(text);
            false
        }
        dialog.setCancelButton(CONFIRM_CANCEL_TEXT) { baseDialog, view, text ->
            cancel();
            false
        }
        dialog.show();
    }


    private var mDefaultDate: String? = null;
    fun setDefaultDate(text:String?): Dialog{
        mDefaultDate = text;
        return this;
    }
    private var mMinDate: String? = null;
    fun setMinDate(text:String?): Dialog{
        mMinDate = text;
        return this;
    }
    private var mMaxDate: String? = null;
    fun setMaxDate(text:String?): Dialog{
        mMaxDate = text;
        return this;
    }

    fun datePicker(callback: ((date: String?)->Unit) = {}){
        datePickerDialogX(callback);
    }

    private fun datePickerDialogX(callback: ((date: String?)->Unit) = {}){
        var dialog = DatePickerDialog.build();
        if(!title.isNullOrEmpty()){ dialog.setTitle(title); }
        if(mDefaultDate != null){
            var arr = mDefaultDate!!.split("-");
            if(arr.size>=3){
                dialog.setDefaultSelect(arr[0].toInt(), arr[1].toInt(), arr[2].toInt());
            }
        }
        if(mMinDate != null){
            var arr = mMinDate!!.split("-");
            if(arr.size>=3){
                dialog.setMinTime(arr[0].toInt(), arr[1].toInt(), arr[2].toInt());
            }
        }
        if(mMaxDate != null){
            var arr = mMaxDate!!.split("-");
            if(arr.size>=3){
                dialog.setMaxTime(arr[0].toInt(), arr[1].toInt(), arr[2].toInt());
            }
        }
        dialog.show(object :OnDateSelected() {
            override fun onSelect(text: String?, year: Int, month: Int, day: Int) {
                callback(text);
            }
        })
    }

    private var mWheelList = mutableListOf<MutableList<String>>();
    fun addWheelList(text:String, wheelIndex: Int = 0): Dialog{
        if(mWheelList.size<=wheelIndex){ mWheelList.add(mutableListOf()); }
        mWheelList[wheelIndex].add(text);
        return this;
    }
    fun addWheelList(list: MutableList<String>): Dialog{
        mWheelList.add(list);
        return this;
    }
    fun setWheelList(list: MutableList<String>, wheelIndex: Int): Dialog{
        if(wheelIndex < mWheelList.size){
            mWheelList[wheelIndex] = list;
        }else{
            mWheelList.add(list);
        }
        return this;
    }
    fun wheelPickerDialogX(changeArray:((wheelIndex: Int, itemIndex:Int, itemText: String, wheelList: MutableList<MutableList<String>>)->Unit)? = null, callback: ((text: String?, selectedIndex:IntArray?)->Unit) = {_,_->}){
        var dialog = CustomWheelPickerDialog.build();
        if(!title.isNullOrEmpty()){ dialog.setTitle(title); }
        for (i in 0 until mWheelList.size){
            dialog.addWheel(mWheelList[i].toTypedArray())
        }
        dialog.setOnWheelChangeListener{ picker, wheelIndex, originWheelData, itemIndex, itemText ->
            if(changeArray != null){
                changeArray(wheelIndex, itemIndex, itemText, mWheelList);
                picker.setDefaultSelect(wheelIndex, itemIndex);
                var next = wheelIndex+1;
                if(mWheelList.size > next){
                    for (i in next until mWheelList.size){
                        picker.setWheel(i, mWheelList[i].toTypedArray());
                        picker.setDefaultSelect(i, 0);
                    }
                }

            }
        }.show(object : OnCustomWheelPickerSelected() {
            override fun onSelected(picker: CustomWheelPickerDialog?, text: String?, selectedTexts: Array<String?>?, selectedIndex: IntArray?) {
                callback(text, selectedIndex);
            }
        });
    }
}