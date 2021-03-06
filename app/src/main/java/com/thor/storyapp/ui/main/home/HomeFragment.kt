package com.thor.storyapp.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thor.storyapp.R
import com.thor.storyapp.data.preferences.datastore.DataStoreSession
import com.thor.storyapp.databinding.FragmentHomeBinding
import com.thor.storyapp.ui.auth.AuthActivity
import com.thor.storyapp.ui.main.StoryAdapter
import com.thor.storyapp.utils.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by inject()
    private val session: DataStoreSession by inject()
    private val adapter = StoryAdapter {
        findNavController().navigate(HomeFragmentDirections.toDetailFragment(it))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            finishAffinity(requireActivity())
        }
        callback.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swiperefresh.setOnRefreshListener { viewModel.refresh() }
        binding.recyclerview.adapter = adapter

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add_story -> {
                    findNavController().navigate(HomeFragmentDirections.toUploadFragment())
                }
                R.id.action_logout -> {
                    lifecycleScope.launch {
                        session.deleteSession()
                        val intent = Intent(activity, AuthActivity::class.java)
                        activity?.startActivity(intent)
                    }
                }
            }
            false
        }
        viewModel.stateList.observe(viewLifecycleOwner, observerStateList)
        session.userFlow.asLiveData().observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.setToken("Bearer ${it.token}")
            }
            viewModel.refresh()
        }
    }


    private val observerStateList = Observer<HomeState> { itState ->
        binding.swiperefresh.isRefreshing = (itState == HomeState.OnLoading)
        when (itState) {
            is HomeState.OnSuccess -> {
                adapter.setList(itState.list)
            }
            is HomeState.OnError -> {
                session.userFlow.asLiveData().observe(this) {
                    if (!it.token.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), itState.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
            HomeState.OnLoading -> {}
        }
    }
}