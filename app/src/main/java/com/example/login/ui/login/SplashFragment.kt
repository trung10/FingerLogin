package com.example.login.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.login.R
import com.example.login.dialog.FingerDialog

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        viewModel.splashDestination.observe(this, Observer { isFirstRun ->
            if (isFirstRun) {
                findNavController().navigate(R.id.action_splashFragment_to_lockScreenFragment)
            }
        })

        viewModel.splashDestination.observe(this, Observer { isShow ->
            if (isShow) {
                fragmentManager?.let { FingerDialog().show(it, "") }
            }
        })
    }

}
