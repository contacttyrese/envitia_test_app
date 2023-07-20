package com.example.envintiatestapp.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.envintiatestapp.databinding.ActivityMainBinding
import com.example.envintiatestapp.model.CapturedText
import com.example.envintiatestapp.viewmodel.MainUserAction
import com.example.envintiatestapp.viewmodel.MainViewModel
import com.example.envintiatestapp.viewmodel.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val _texts = arrayListOf<CapturedText>()
    private val textAdapter = TextAdapter(_texts)
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        createRecycler()
        createObservation()

        binding.submitButton.setOnClickListener {
            val text: String = binding.editText.text.toString()
            if (text.isNotEmpty()) {
                val time = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(Calendar.getInstance().time)
                val capturedText = CapturedText(time, text)
                viewModel.userActionSubject.onNext(MainUserAction.TextSubmittedSuccess(capturedText))
                binding.editText.setText("")
            }
        }
    }

    private fun createRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.setItemViewCacheSize(1)
        binding.recyclerView.adapter = textAdapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )
        binding.recyclerView.findViewHolderForItemId(0)
    }

    private fun createObservation() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                MainViewState.AwaitingLoading -> {
                    Log.i("observe_awaiting", "awaiting text")
                }
                MainViewState.Loading -> {
                    Log.i("observe_loading", "loading text")
                }
                is MainViewState.LoadingError -> {
                    Log.e("observe_error", "loading error ${state.throwable}")
                }
                is MainViewState.LoadingSuccess -> {
                    _texts.removeAll(state.texts.toSet())
                    _texts.addAll(state.texts)
                    binding.recyclerView.adapter?.let {
                        it.notifyItemRangeChanged(0, it.itemCount-1)
                        Log.i("recycler_update_success", "sent notification of data change in recycler")
                    } ?: kotlin.run {
                        Log.e("recycler_update_fail", "adapter was null so notification not sent")
                    }
                    Log.i("observe_loaded", "texts loaded")
                    viewModel.userActionSubject.onNext(MainUserAction.AwaitingTextSubmission)
                }
            }
        }
    }
}