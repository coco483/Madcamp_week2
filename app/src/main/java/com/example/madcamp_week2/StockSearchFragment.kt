package com.example.madcamp_week2

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.databinding.FragmentStockDetailBinding
import com.example.madcamp_week2.databinding.FragmetStockSearchBinding
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class StockSearchFragment: Fragment() {
    private var _binding: FragmetStockSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmetStockSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchAdapter = StockDataHolder
            .stockList?.let{ArrayAdapter(requireContext(), R.layout.simple_list_item_1, it)}
        binding.stockSearchSearchInputACTV.setAdapter(searchAdapter)
        binding.stockSearchSearchInputACTV.setOnItemClickListener { adapterView, view, i, l ->
            val selectedStock = adapterView.getItemAtPosition(i) as Stock
            openStockDetailFragment(selectedStock)
        }


    }
    private fun openStockDetailFragment(stock: Stock) {
        val stockDetailFragment = StockDetailFragment().apply {
            arguments = Bundle().apply {
                putString("STOCK_ID", stock.id)
                putString("STOCK_NAME", stock.name)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(com.example.madcamp_week2.R.id.blank_container, stockDetailFragment)
            .addToBackStack(null)
            .commit()
    }

}