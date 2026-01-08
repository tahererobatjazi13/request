package ir.kitgroup.request.feature.product.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.request.R
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.core.utils.component.ConfirmDeleteDialog
import ir.kitgroup.request.databinding.BottomSheetProductBinding
import ir.kitgroup.request.databinding.FragmentProductListBinding
import ir.kitgroup.request.feature.product.ui.adapter.ProductAdapter
import kotlinx.coroutines.launch
import ir.kitgroup.request.core.utils.extensions.show
import ir.kitgroup.request.core.utils.extensions.gone
import ir.kitgroup.request.core.utils.extensions.hide
import ir.kitgroup.request.core.utils.extensions.toEnglishDigits

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClicks()
        initAdapter()
        initRecyclerViews()
        observeData()
    }

    private fun setupClicks() {
        binding.apply {
            hfProduct.setOnClickImgTwoListener {
                findNavController().navigateUp()
            }

            cvAddProduct.setOnClickListener {
                showSheet(null)
            }
        }
    }

    private fun initAdapter() {

        productAdapter = ProductAdapter(
            onChangeLog = { product ->
                val action =
                    ProductFragmentDirections.actionProductFragmentToChangeLogFragment(
                        productId = product.id, 1
                    )
                findNavController().navigate(action)
            },
            onEdit = { showSheet(it) },
            onDelete = { showConfirmDeleteDialog(it) },
        )

    }

    private fun showConfirmDeleteDialog(product: ProductEntity) {
        ConfirmDeleteDialog {
            productViewModel.delete(product)
        }.show(childFragmentManager, "ConfirmDeleteDialog")
    }

    private fun initRecyclerViews() {
        binding.rvProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
    }

    private fun observeData() {
        productViewModel.productList.observe(viewLifecycleOwner) { products ->
            if (products.isEmpty()) {
                binding.info.show()
                binding.info.message(requireContext().getString(R.string.msg_no_data))
                binding.rvProduct.hide()
            } else {
                binding.info.gone()
                binding.rvProduct.show()
                productAdapter.setData(products)
            }
        }
    }


    private fun showSheet(entity: ProductEntity?) {

        val dialog = BottomSheetDialog(requireContext())
        val b = BottomSheetProductBinding.inflate(layoutInflater)
        dialog.setContentView(b.root)

        b.edtPrice.addTextChangedListener(object : TextWatcher {
            private var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            @SuppressLint("DefaultLocale")
            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s.isNullOrEmpty()) return

                isEditing = true

                val cleanString = s.toString().replace(",", "")

                try {
                    val parsed = cleanString.toDoubleOrNull()
                    if (parsed != null) {
                        val formatted = String.format("%,.0f", parsed)
                        b.edtPrice.setText(formatted)
                        b.edtPrice.setSelection(formatted.length)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                isEditing = false
            }
        })

        // ویرایش
        entity?.let {
            b.edtCode.setText(it.code)
            b.edtName.setText(it.name)
            b.edtFeature1.setText(it.feature1)
            b.edtFeature2.setText(it.feature2)
            b.edtFeature3.setText(it.feature3)
            b.edtFeature4.setText(it.feature4)
            b.edtPrice.setText(it.price.toString())
            b.edtUnit.setText(it.unit.toString())
            b.edtUnitName.setText(it.unitName)
        }

        lifecycleScope.launch {

            val feature1List = productViewModel.getFeaturesByType(1)
            val feature2List = productViewModel.getFeaturesByType(2)
            val feature3List = productViewModel.getFeaturesByType(3)
            val feature4List = productViewModel.getFeaturesByType(4)

            b.edtFeature1.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    feature1List
                )
            )

            b.edtFeature2.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    feature2List
                )
            )

            b.edtFeature3.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    feature3List
                )
            )

            b.edtFeature4.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    feature4List
                )
            )
            setupAutoCompleteShowOnClick(b.edtFeature1)
            setupAutoCompleteShowOnClick(b.edtFeature2)
            setupAutoCompleteShowOnClick(b.edtFeature3)
            setupAutoCompleteShowOnClick(b.edtFeature4)

        }

        b.btnSave.setOnClickListener {
            if (!validate(b)) return@setOnClickListener

            val price = b.edtPrice.text.toString()
                .replace(",", "")
                .toEnglishDigits()

            val priceValue = price.toDoubleOrNull() ?: -1.0

            val unit = b.edtUnit.text.toString()
                .replace(",", "")
                .toEnglishDigits()

            val unitValue = unit.toDoubleOrNull() ?: 1.0

            val newEntity = ProductEntity(
                id = entity?.id ?: 0,
                code = b.edtCode.text.toString(),
                name = b.edtName.text.toString(),
                feature1 = b.edtFeature1.text.toString(),
                feature2 = b.edtFeature2.text.toString(),
                feature3 = b.edtFeature3.text.toString(),
                feature4 = b.edtFeature4.text.toString(),
                price = priceValue,
                unitName = b.edtUnitName.text.toString(),
                unit = unitValue
            )

            productViewModel.saveFeatureHistory(1, b.edtFeature1.text.toString())
            productViewModel.saveFeatureHistory(2, b.edtFeature2.text.toString())
            productViewModel.saveFeatureHistory(3, b.edtFeature3.text.toString())
            productViewModel.saveFeatureHistory(4, b.edtFeature4.text.toString())


            if (entity == null) {
                productViewModel.insert(newEntity)
            } else {
                productViewModel.updateProductWithPriceLog(
                    oldProduct = entity,
                    newProduct = newEntity
                )
            }

            dialog.dismiss()
        }
        dialog.show()
    }

    fun setupAutoCompleteShowOnClick(editText: AutoCompleteTextView) {
        editText.setOnClickListener {
            editText.showDropDown()
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editText.showDropDown()
            }
        }
    }

    private fun validate(b: BottomSheetProductBinding): Boolean {
        var isValid = true

        if (b.edtCode.text.isNullOrBlank()) {
            b.edtCode.error = getString(R.string.error_enter_code)
            isValid = false
        }
        if (b.edtName.text.isNullOrBlank()) {
            b.edtName.error = getString(R.string.error_enter_name)
            isValid = false
        }
        if (b.edtFeature1.text.isNullOrBlank()) {
            b.edtFeature1.error = getString(R.string.error_enter_feature1)
            isValid = false
        }
        if (b.edtFeature2.text.isNullOrBlank()) {
            b.edtFeature2.error = getString(R.string.error_enter_feature2)
            isValid = false
        }
        if (b.edtFeature3.text.isNullOrBlank()) {
            b.edtFeature3.error = getString(R.string.error_enter_feature3)
            isValid = false
        }
        if (b.edtFeature4.text.isNullOrBlank()) {
            b.edtFeature4.error = getString(R.string.error_enter_feature4)
            isValid = false
        }
        if (b.edtUnit.text.isNullOrBlank()) {
            b.edtUnit.error = getString(R.string.error_enter_unit)
            isValid = false
        }
        if (b.edtUnitName.text.isNullOrBlank()) {
            b.edtUnitName.error = getString(R.string.error_enter_unit_name)
            isValid = false
        }
        if (b.edtPrice.text.isNullOrBlank()) {
            b.edtPrice.error = getString(R.string.error_enter_price)
            isValid = false
        }
        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}