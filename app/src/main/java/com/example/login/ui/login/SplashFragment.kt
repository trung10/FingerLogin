package com.example.login.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.login.ui.login.SplashDestination.CONFIRM_PASS_CODE
import com.example.login.ui.login.SplashDestination.SET_UP_PASS_CODE


import com.example.login.R
import com.example.login.dialog.FingerDialog
import com.example.login.result.Event
import com.example.login.result.EventObserver
import com.example.login.util.checkAllMatched

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        viewModel.splashDestination.observe(this, EventObserver { destination ->
            when (destination) {
                CONFIRM_PASS_CODE -> fragmentManager?.let { FingerDialog().show(it, "") }
                SET_UP_PASS_CODE -> findNavController().navigate(R.id.action_splashFragment_to_lockScreenFragment)
            }.checkAllMatched
        })

    }

}
