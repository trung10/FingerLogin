package com.example.login.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.login.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FingerDialog : AppCompatDialogFragment() {

    private lateinit var image: ImageView
    private lateinit var text: TextView
    private lateinit var root: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.finger_dialog, container, false)
        image = root.findViewById(R.id.button_finger_print)
        text = root.findViewById(R.id.try_again_text_view)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(root)
        }

    }
}