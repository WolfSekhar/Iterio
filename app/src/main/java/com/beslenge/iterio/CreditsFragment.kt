package com.beslenge.iterio

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class CreditsFragment(context: Context) : DialogFragment() {
    private lateinit var creditView: View
    private val myContext: Context = context
    private lateinit var myDialog: MaterialAlertDialogBuilder

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @Override
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
        creditView = View.inflate(requireContext(), R.layout.credits_view, null)
        dialog.setView(creditView)
        myDialog = dialog

        buttonActions()

        return dialog.create()
    }

    private fun buttonActions() {
        val rateButton: Button = creditView.findViewById(R.id.button_playstore_rating)
        val shareButton: Button = creditView.findViewById(R.id.button_share_app)
        rateButton.setOnClickListener {
            dismiss()
            rate()
        }

        shareButton.setOnClickListener {
            dismiss()
            shareApp()
        }

    }

    private fun rate() {
        val playStoreIntent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.beslenge.iterio")
        playStoreIntent.setPackage("com.android.vending")
        playStoreIntent.data = uri
        startActivity(playStoreIntent)

    }

    private fun shareApp() {
        val url = "https://play.google.com/store/apps/details?id=com.beslenge.iterio"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        try {
            startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(requireView(), getString(R.string.can_not_share), Snackbar.LENGTH_LONG)
                .show()
        }
    }


}


