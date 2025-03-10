package com.oscar0819.flowtest

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.oscar0819.flowtest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bt1.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                testColdFlow()
            }
        }

//        lifecycleScope.launch {
//            viewModel.events.collect { signal ->
//                Log.d("hotFlow", "events=$signal")
//            }
//        }

        binding.bt2.setOnClickListener {
            lifecycleScope.launch {
                viewModel.events.collect { signal ->
                    Log.d("hotFlow", "events=$signal")
                }
            }

            lifecycleScope.launch {
                viewModel.testHotFlow(3)
                viewModel.testHotFlow(4)
            }
        }

        binding.bt3.setOnClickListener {
            lifecycleScope.launch {
                viewModel.testHotFlow(5)
                viewModel.testHotFlow(6)
            }

            lifecycleScope.launch {
                viewModel.events.collect { signal ->
                    Log.d("hotFlow", "events=$signal")
                }
            }

            lifecycleScope.launch {
                viewModel.testHotFlow(7)
                viewModel.testHotFlow(8)
            }
        }

    }

    private suspend fun testColdFlow() {
        val coldFlow = flow {
            emit(1)
            emit(2)
            emit(3)
        }

        coldFlow.collect { value ->
            Log.d("coldFlow", "collect=$value")
        }
    }
}