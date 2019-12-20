package com.example.login.fragment


import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.login.R
import com.example.login.databinding.FragmentLockScreenBinding
import com.example.login.di.InjectionUtil
import com.example.login.extensions.baseConfig
import com.example.login.extensions.isFingerPrintSensorAvailable
import com.example.login.extensions.toast
import com.example.login.views.CodeView
import com.github.ajalt.reprint.core.AuthenticationFailureReason
import com.github.ajalt.reprint.core.AuthenticationListener
import com.github.ajalt.reprint.core.Reprint
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LockScreenFragment : Fragment() {

    private val RECHECK_PERIOD = 3000L
    private val registerHandler = Handler()

    private lateinit var mFingerprintButton: View
    private lateinit var mDeleteButton: View
    private lateinit var mNotSetPassButton: TextView
    private lateinit var mCodeView: CodeView
    private lateinit var titleView: TextView

    lateinit var viewModel: LockScreenViewModel

    private lateinit var binding: FragmentLockScreenBinding

    private var mUseFingerPrint = true
    private var mFingerprintHardwareDetected = false
    private var mIsCreateMode = false

    //private var mCodeCreateListener: OnLockScreenCodeCreateListener? = null
    //private var mLoginListener: OnLockScreenLoginListener? = null

    private var mCode = ""
    private var mCodeValidation = ""
    private var mEncodedPinCode = ""

    //private PFFLockScreenConfiguration mConfiguration;

    //private val mPinCodeViewModel: PinCodeViewModel =  PFPinCodeViewModel()

    private var mOnLeftButtonClickListener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InjectionUtil.setup(activity!!.applicationContext)

        InjectionUtil.inject(this)

        mIsCreateMode = context!!.baseConfig.appPasswordHash.isBlank()
        mUseFingerPrint = Reprint.hasFingerprintRegistered()
        mFingerprintHardwareDetected = context!!.isFingerPrintSensorAvailable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLockScreenBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        mFingerprintButton = binding.buttonFingerPrint
        mDeleteButton = binding.buttonDelete

        //mLeftButton = view.findViewById(R.id.button_left)
        mNotSetPassButton = binding.buttonNotSetPass

        mDeleteButton.setOnClickListener(mOnDeleteButtonClickListener)
        mDeleteButton.setOnLongClickListener(mOnDeleteButtonOnLongClickListener)
        mFingerprintButton.setOnClickListener(mOnFingerprintClickListener)

        titleView = binding.titleTextView

        mCodeView = binding.codeView
        initKeyViews()

        mCodeView.setListener(mCodeListener)

        if (!mUseFingerPrint) {
            mFingerprintButton.visibility = View.GONE
        }

        if (mFingerprintHardwareDetected) checkRegisteredFingerprints()

        applyConfiguration()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.viewModel = viewModel
    }

    private fun initKeyViews() {
        return
        binding.button0.setOnClickListener(mOnKeyClickListener)
        binding.button1.setOnClickListener(mOnKeyClickListener)
        binding.button2.setOnClickListener(mOnKeyClickListener)
        binding.button3.setOnClickListener(mOnKeyClickListener)
        binding.button4.setOnClickListener(mOnKeyClickListener)
        binding.button5.setOnClickListener(mOnKeyClickListener)
        binding.button6.setOnClickListener(mOnKeyClickListener)
        binding.button7.setOnClickListener(mOnKeyClickListener)
        binding.button8.setOnClickListener(mOnKeyClickListener)
        binding.button9.setOnClickListener(mOnKeyClickListener)
    }

    private fun applyConfiguration() {
        if (mIsCreateMode) {
            titleView.text = getString(R.string.set_pass_code)

        } else {
            titleView.text = getString(R.string.input_your_pass_code)
            mNotSetPassButton.visibility = View.GONE
        }
    }

    private val mOnKeyClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            if (v is TextView) {
                val string = (v).text.toString()
                if (string.length != 1) {
                    return
                }
                val codeLength = mCodeView.input(string)
                configureRightButton(codeLength)
            }
        }
    }

    private val mOnDeleteButtonClickListener = View.OnClickListener {
        val codeLength = mCodeView.delete()
        configureRightButton(codeLength)
    }

    private val mOnDeleteButtonOnLongClickListener = View.OnLongClickListener {
        mCodeView.clearCode()
        configureRightButton(0)
        true
    }

    private val mOnFingerprintClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                !mFingerprintHardwareDetected
            ) {
                return
            }


            if (!mUseFingerPrint) {
                // todo show dialog open setting, but i still not implement
                showNoFingerprintDialog()
                return
            }
        }
    }

    private fun configureRightButton(codeLength: Int) {
        if (mIsCreateMode) {
            if (codeLength > 0) {
                mDeleteButton.visibility = View.VISIBLE
                mFingerprintButton.visibility = View.GONE
            } else {
                mDeleteButton.visibility = View.GONE
                mFingerprintButton.visibility = View.VISIBLE
            }
            return
        }

        if (codeLength > 0) {
            mFingerprintButton.visibility = View.GONE
            mDeleteButton.visibility = View.VISIBLE
            mDeleteButton.isEnabled = true
            return
        }

        if (mUseFingerPrint && mFingerprintHardwareDetected) {
            mFingerprintButton.visibility = View.VISIBLE
            mDeleteButton.visibility = View.GONE
        } else {
            mFingerprintButton.visibility = View.GONE
            mDeleteButton.visibility = View.VISIBLE
        }

        mDeleteButton.isEnabled = false

    }

    private fun showNoFingerprintDialog() {
        // todo crete dialog and open setting
    }

    private val mCodeListener = object : CodeView.OnCodeListener {
        override fun onCodeCompleted(code: String) {
            if (mIsCreateMode) {
                if (mCodeValidation == "") {
                    mCodeValidation = code
                    titleView.text = getString(R.string.confirm_pass_code)
                    mNotSetPassButton.visibility = View.GONE
                    cleanCode()
                } else {
                    if (code == mCodeValidation) {
                        mCode = code
                        val encrypt = getHashedPin()
                        context!!.baseConfig.appPasswordHash = encrypt
                        //mCodeCreateListener?.onCodeCreated(encrypt)
                        mIsCreateMode = false
                        titleView.text = getString(R.string.input_your_pass_code)
                        cleanCode()
                        return
                    } else {
                        //mCodeCreateListener?.onNewCodeValidationFailed()
                        errorAction()
                    }
                }

            } else {
                mCode = code

                if (getHashedPin() == context!!.baseConfig.appPasswordHash) {
                    //mLoginListener?.onCodeInputSuccessful()
                    context?.toast("Login success")
                } else {
                    //mLoginListener?.onPinLoginFailed()
                    errorAction()
                }
            }
        }

        override fun onCodeNotCompleted(code: String) {
            if (mIsCreateMode) {
                // Todo
                return
            }
        }
    }

    private fun cleanCode() {
        mCode = ""
        mCodeView.clearCode()
    }

    private fun checkRegisteredFingerprints() {

        Reprint.authenticate(object : AuthenticationListener {
            override fun onSuccess(moduleTag: Int) {
                //mLoginListener?.onFingerprintSuccessful()
                context?.toast("Login fingerprint successful")
            }

            override fun onFailure(
                failureReason: AuthenticationFailureReason,
                fatal: Boolean,
                errorMessage: CharSequence?,
                moduleTag: Int,
                errorCode: Int
            ) {
                //mLoginListener?.onFingerprintLoginFailed()
                when (failureReason) {
                    AuthenticationFailureReason.AUTHENTICATION_FAILED -> context?.toast("Login fingerprint failed")
                    AuthenticationFailureReason.LOCKED_OUT -> context?.toast("Login fingerprint failed")
                }
            }
        })

        registerHandler.postDelayed({
            checkRegisteredFingerprints()
        }, RECHECK_PERIOD)
    }

    override fun onDestroy() {
        super.onDestroy()
        registerHandler.removeCallbacksAndMessages(null)
        Reprint.cancelAuthentication()
    }

    private fun errorAction() {
        // Vibrate during 400 ms
        val v = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(400, 100))
        } else {
            v.vibrate(400)
        }

        // Animation shake
        val animShake = AnimationUtils.loadAnimation(context, R.anim.shake)
        mCodeView.startAnimation(animShake)
    }

    fun setOnLeftButtonClickListener(onLeftButtonClickListener: View.OnClickListener) {
        this.mOnLeftButtonClickListener = onLeftButtonClickListener
    }

    private fun getHashedPin(): String {
        val messageDigest = MessageDigest.getInstance("SHA-1")
        messageDigest.update(mCode.toByteArray(charset("UTF-8")))
        val digest = messageDigest.digest()
        val bigInteger = BigInteger(1, digest)
        return String.format(Locale.getDefault(), "%0${digest.size * 2}x", bigInteger).toLowerCase()
    }

    /**
     * Set OnPFLockScreenCodeCreateListener.
     *
     * @param listener OnPFLockScreenCodeCreateListener object.
     */
    /*fun setCodeCreateListener(listener: OnLockScreenCodeCreateListener) {
        mCodeCreateListener = listener
    }*/

    /**
     * Set OnPFLockScreenLoginListener.
     *
     * @param listener OnPFLockScreenLoginListener object.
     */
    /*fun setLoginListener(listener: OnLockScreenLoginListener) {
        mLoginListener = listener
    }*/

    /**
     * Set Encoded pin code.
     *
     * @param encodedPinCode encoded pin code string, that was created before.
     */
    /*fun setEncodedPinCode(encodedPinCode: String) {
        mEncodedPinCode = encodedPinCode
    }*/


    /**
     * Pin Code create callback interface.
     */
    /*interface OnLockScreenCodeCreateListener {

        */
    /**
     * Callback method for pin code creation.
     *
     * @param encodedCode encoded pin code string.
     *//*
        fun onCodeCreated(encodedCode: String)

        */
    /**
     * This will be called if PFFLockScreenConfiguration#newCodeValidation is true.
     * User need to input new code twice. This method will be called when second code isn't
     * the same as first.
     *//*
        fun onNewCodeValidationFailed()

    }*/


    /**
     * Login callback interface.
     */
    /*interface OnLockScreenLoginListener {

        */
    /**
     * Callback method for successful login attempt with pin code.
     *//*
        fun onCodeInputSuccessful()

        */
    /**
     * Callback method for successful login attempt with fingerprint.
     *//*
        fun onFingerprintSuccessful()

        */
    /**
     * Callback method for unsuccessful login attempt with pin code.
     *//*
        fun onPinLoginFailed()

        */
    /**
     * Callback method for unsuccessful login attempt with fingerprint.
     *//*
        fun onFingerprintLoginFailed()

    }*/
}
