package com.cornershop.counterstest.presentation.dialogs

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MessageDialog(private var builder: Builder) : DialogFragment() {


    private var dialogBuilder: AlertDialog.Builder? = null
    var title: String? = null
    private var nameButtonOk: String? = null
    private var nameButtonCancel: String? = null
    private var messageD: String? = null
    private var actionButtonOk: DialogInterface.OnClickListener? = null
    private var actionButtonCancel: DialogInterface.OnClickListener? = null

    init {
        this.title = builder.title
        this.messageD = builder.message
        this.nameButtonOk = builder.nameButtonOk
        this.nameButtonCancel = builder.nameButtonCancel
        this.actionButtonOk = builder.actionButtonOk
        this.actionButtonCancel =
            DialogInterface.OnClickListener { _, _ -> builder.actionButtonCancel }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            this.dialogBuilder = builder.dialog
            dialogBuilder?.setMessage(messageD)
            if (this.nameButtonOk != null) {
                dialogBuilder?.setPositiveButton(
                    this.nameButtonOk,
                    this.actionButtonOk
                )
            }

            if (this.nameButtonCancel != null) {
                dialogBuilder?.setNegativeButton(
                    this.nameButtonCancel,
                    this.actionButtonCancel
                )
            }

            // Create the AlertDialog object and return it
            dialogBuilder?.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    class Builder {
        var dialog: AlertDialog.Builder? = null
            private set
        var title: String? = null
            private set
        var message: String? = null
            private set
        var nameButtonOk: String? = null
            private set
        var nameButtonCancel: String? = null
            private set
        var actionButtonOk: DialogInterface.OnClickListener? = null
            private set
        var actionButtonCancel: DialogInterface.OnClickListener? = null
            private set

        fun dialog(dialogBuild: AlertDialog.Builder) = apply {
            this.dialog = dialogBuild
        }

        fun setTitle(str: String) = apply { this.title = str }

        fun setMessage(message: String) = apply { this.message = message }

        fun setPositiveButton(name: String, listener: DialogInterface.OnClickListener) = apply {
            this.nameButtonOk = name
            this.actionButtonOk = listener
        }

        fun setNegativeButton(name: String, listener: DialogInterface.OnClickListener) = apply {
            this.nameButtonCancel = name
            this.actionButtonCancel = listener
        }

        fun build() = MessageDialog(this)

    }
}