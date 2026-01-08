package ir.kitgroup.request.feature.product.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.huri.jcal.JalaliCalendar
import ir.kitgroup.request.core.utils.extensions.gone
import ir.kitgroup.request.core.utils.extensions.hide
import ir.kitgroup.request.core.utils.extensions.show
import ir.kitgroup.request.databinding.FragmentChangeLogBinding
import ir.kitgroup.request.feature.product.ui.adapter.ChangeLogAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ChangeLogFragment : Fragment() {

    private var _binding: FragmentChangeLogBinding? = null
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var changeLogAdapter: ChangeLogAdapter
    private val args: ChangeLogFragmentArgs by navArgs()
    private var displayDateTime: String = ""
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initAdapter()
        rxBinding()
        setupObservers()
    }

    @SuppressLint("DefaultLocale")
    private fun init() {
        val jalaliDate = JalaliCalendar()
        val dateFormatted =
            String.format("%02d-%02d-%04d", jalaliDate.day, jalaliDate.month, jalaliDate.year)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = timeFormat.format(Date())
        displayDateTime = "$dateFormatted ، $time"
    }

    private fun initAdapter() {
        changeLogAdapter = ChangeLogAdapter()

        binding.rvChangeLog.adapter = changeLogAdapter
        binding.rvChangeLog.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun rxBinding() {
        binding.hfChangeLog.setOnClickImgTwoListener {
            findNavController().navigateUp()
        }

    }

    private fun setupObservers() {
        productViewModel.getChangeLogsForProductByType(args.productId, args.changeType)
            .observe(viewLifecycleOwner) { lists ->
                if (lists.isEmpty()) {
                    binding.tvNoItem.show()
                    binding.rvChangeLog.hide()
                } else {
                    binding.tvNoItem.gone()
                    binding.rvChangeLog.show()
                    changeLogAdapter.setData(lists)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}