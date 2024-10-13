package com.example.dicodingevent.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.dicodingevent.R

class FailDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Failed to load data")
                .setNegativeButton("Close") { _, _ ->
                    // User cancelled the dialog.
                    dismiss()
                }
            // Create the AlertDialog object and return it.
            builder.create()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }
}