package com.bhavya.smsrelay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhavya.smsrelay.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _b: FragmentHistoryBinding? = null
    private val b get() = _b!!
    private lateinit var adapter: LogsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentHistoryBinding.inflate(inflater, container, false)
        adapter = LogsAdapter()
        b.rvLogs.layoutManager = LinearLayoutManager(requireContext())
        b.rvLogs.adapter = adapter
        return b.root
    }

    override fun onResume() {
        super.onResume()
        adapter.submit(LogStore.readAll(requireContext()))
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}