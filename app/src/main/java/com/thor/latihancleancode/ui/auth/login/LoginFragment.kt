package com.thor.latihancleancode.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thor.latihancleancode.R
import com.thor.latihancleancode.data.preferences.datastore.DataStoreSession
import com.thor.latihancleancode.databinding.FragmentLoginBinding
import com.thor.latihancleancode.repository.auth.Login
import com.thor.latihancleancode.ui.main.MainActivity
import com.thor.latihancleancode.utils.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by inject()
    private val session: DataStoreSession by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        playAnimation()

        viewModel.stateList.observe(viewLifecycleOwner, observerStateList)
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        binding.myPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.passwordTextInputLayout.errorPassword(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.myBtnLogin.setOnClickListener {
            val email = binding.myEmail.text.toString()
            val password = binding.myPassword.text.toString()
            if (emailIsValid(email) && passwordIsValid(password.length)) {
                showLoading(true)
                viewModel.login(email, password)
            }
        }
    }

    private fun emailIsValid(etMail: String): Boolean =
        when {
            etMail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(etMail)
                .matches()
            ->
                true
            else -> {
                Toast.makeText(context, "Enter valid Email address !", Toast.LENGTH_SHORT).show()
                false
            }
        }

    private fun passwordIsValid(passwordLength: Int): Boolean =
        when {
            passwordLength > 6 -> true
            passwordLength == 0 -> {
                Toast.makeText(context, "Password Is Empty !", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                Toast.makeText(context, "Enter Valid Password !", Toast.LENGTH_SHORT).show()
                false
            }
        }


    private val observerStateList = Observer<LoginState> {
        showLoading(it == LoginState.OnLoading)
        when (it) {
            is LoginState.OnSuccess -> {
                lifecycleScope.launch {
                    session.setUserLogin(
                        Login(
                            it.login.name,
                            it.login.userId,
                            it.login.token
                        )
                    )
                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                }
            }
            is LoginState.OnError -> {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

            }
            LoginState.OnLoading -> {}
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageAnimLogin, View.ROTATION, 2f, -2f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()


        val emailTextView = ObjectAnimator.ofFloat(binding.myEmail, View.ALPHA, 1f).setDuration(300)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.myPassword, View.ALPHA, 1f).setDuration(300)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.myBtnLogin, View.ALPHA, 1f).setDuration(300)
        val textOr = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(300)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login,
                textOr,
                register
            )
            startDelay = 200
        }.start()
    }
}