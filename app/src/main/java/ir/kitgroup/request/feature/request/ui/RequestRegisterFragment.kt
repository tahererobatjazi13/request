package ir.kitgroup.request.feature.request.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.request.R
import ir.kitgroup.request.core.utils.CustomerRole
import ir.kitgroup.request.databinding.FragmentRequestRegisterBinding
import ir.kitgroup.request.feature.request.model.CustomerSpinnerItem
import ir.kitgroup.request.feature.request.ui.adapter.SpinnerAdapter

@AndroidEntryPoint
class RequestRegisterFragment : Fragment() {

    private var _binding: FragmentRequestRegisterBinding? = null
    private val binding get() = _binding!!

    private val requestViewModel: RequestViewModel by viewModels()
    private val orderGiverItems = mutableListOf<CustomerSpinnerItem>()
    private val orderReceiverItems = mutableListOf<CustomerSpinnerItem>()
    private val args: RequestRegisterFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupClicks()
    }


    private fun observeData() {
        requestViewModel.businessSideList.observe(viewLifecycleOwner) { list ->

            // ---------- ORDER GIVER ----------
            orderGiverItems.clear()
            val giverTitles = mutableListOf<String>()
            list.filter { it.customerRole == CustomerRole.ORDER_GIVER }.forEach {
                orderGiverItems.add(CustomerSpinnerItem(it.id, it.name))
                giverTitles.add(it.name)
            }
            binding.spOrderGiver.adapter = SpinnerAdapter(requireContext(), giverTitles)

            // ---------- ORDER RECEIVER ----------
            orderReceiverItems.clear()
            val receiverTitles = mutableListOf<String>()
            list.filter { it.customerRole == CustomerRole.ORDER_RECEIVER }.forEach {
                orderReceiverItems.add(CustomerSpinnerItem(it.id, it.name))
                receiverTitles.add(it.name)
            }
            binding.spOrderReceiver.adapter = SpinnerAdapter(requireContext(), receiverTitles)

            // ---------- Listeners ----------
            binding.spOrderGiver.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    orderGiverItems.getOrNull(position)?.id?.let { requestViewModel.setOrderGiver(it) }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            binding.spOrderReceiver.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    orderReceiverItems.getOrNull(position)?.id?.let { requestViewModel.setOrderReceiver(it) }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            // ---------- load request for edit ----------
            loadRequestIfEdit()
        }
    }

    private fun loadRequestIfEdit() {
        if (args.requestId == 0) return // حالت افزودن
Log.d("requestId2",args.requestId.toString())
        requestViewModel.getRequestById(args.requestId).observe(viewLifecycleOwner) { request ->
            request?.let {
                binding.edtMaximumCapacityUnit1.setText(it.maxCapacity1.toString())
                binding.edtMaximumCapacityUnit2.setText(it.maxCapacity2.toString())
                binding.edtMaximumPrice.setText(it.maxPrice.toString())
                binding.edtDescription.setText(it.description)

                // ست کردن اسپینرها
                val giverIndex = orderGiverItems.indexOfFirst { item -> item.id == it.orderGiverId }
                if (giverIndex != -1) binding.spOrderGiver.setSelection(giverIndex)

                val receiverIndex = orderReceiverItems.indexOfFirst { item -> item.id == it.orderReceiverId }
                if (receiverIndex != -1) binding.spOrderReceiver.setSelection(receiverIndex)
            }
        }
    }

    private fun setupClicks() {
        binding.apply {
            hfRequestRegister.setOnClickImgTwoListener { findNavController().navigateUp() }

            // فرمت کردن قیمت
            edtMaximumPrice.addTextChangedListener(object : TextWatcher {
                private var isEditing = false
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                @SuppressLint("DefaultLocale")
                override fun afterTextChanged(s: Editable?) {
                    if (isEditing || s.isNullOrEmpty()) return
                    isEditing = true
                    val cleanString = s.toString().replace(",", "")
                    val parsed = cleanString.toDoubleOrNull()
                    parsed?.let {
                        val formatted = String.format("%,.0f", it)
                        edtMaximumPrice.setText(formatted)
                        edtMaximumPrice.setSelection(formatted.length)
                    }
                    isEditing = false
                }
            })

            cvContinue.setOnClickListener {
                if (!validate()) return@setOnClickListener

                requestViewModel.saveRequest(
                    maxCapacity1 = edtMaximumCapacityUnit1.text.toString().toIntOrNull() ?: 0,
                    maxCapacity2 = edtMaximumCapacityUnit2.text.toString().toIntOrNull() ?: 0,
                    maxPrice = edtMaximumPrice.text.toString().replace(",", "").toLongOrNull() ?: 0,
                    description = edtDescription.text.toString()
                )
                findNavController().navigateUp()
            }
        }
    }
    private fun validate(): Boolean {
        var isValid = true

        if (binding.edtMaximumCapacityUnit1.text.isNullOrBlank()) {
            binding.edtMaximumCapacityUnit1.error =
                getString(R.string.error_enter_maximum_capacity_unit1)
            isValid = false
        }
        if (binding.edtMaximumCapacityUnit2.text.isNullOrBlank()) {
            binding.edtMaximumCapacityUnit2.error =
                getString(R.string.error_enter_maximum_capacity_unit2)
            isValid = false
        }
        if (binding.edtMaximumPrice.text.isNullOrBlank()) {
            binding.edtMaximumPrice.error = getString(R.string.error_enter_maximum_price)
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}