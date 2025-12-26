package ir.kitgroup.request.feature.product.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.request.R
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.core.utils.CustomerRole
import ir.kitgroup.request.core.utils.PersonType
import ir.kitgroup.request.core.utils.SnackBarType
import ir.kitgroup.request.core.utils.component.CustomSnackBar
import ir.kitgroup.request.databinding.BottomSheetBusinessSideBinding
import ir.kitgroup.request.databinding.BottomSheetProductBinding
import ir.kitgroup.request.databinding.FragmentProductListBinding
import ir.kitgroup.request.feature.product.ui.adapter.ProductAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private val productViewModel: ProductViewModel by viewModels()
    private var logoUri: String? = null
    private var currentSelectedType = CustomerRole.ORDER_RECEIVER

    private lateinit var allBusinessSide: List<ProductEntity>
    private lateinit var filteredBusinessSideList: List<ProductEntity>

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

            // دکمه افزودن
            cvAddProduct.setOnClickListener {
                showSheet(null)
            }
        }
    }

    private fun updateListForSelectedTab(selectedType: CustomerRole) {

        /*  filteredBusinessSideList =
              allBusinessSide.filter { it.customerRole == selectedType }*/

        productAdapter.submitList(filteredBusinessSideList)

        binding.info.visibility =
            if (filteredBusinessSideList.isEmpty()) View.VISIBLE else View.GONE
        binding.info.message(getString(R.string.msg_no_data))

    }

    private fun initAdapter() {

        allBusinessSide = listOf()
        filteredBusinessSideList = allBusinessSide

        productAdapter = ProductAdapter(
            onEdit = { showSheet(it) },
            onDelete = { productViewModel.delete(it) }
        )

    }

    private fun initRecyclerViews() {
        binding.rvProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
    }

    private fun observeData() {
        productViewModel.businessSideList.observe(viewLifecycleOwner) { materials ->
            allBusinessSide = materials
            updateListForSelectedTab(currentSelectedType)
        }
    }


    private fun showSheet(entity: ProductEntity?) {

        val dialog = BottomSheetDialog(requireContext())
        val b = BottomSheetProductBinding.inflate(layoutInflater)
        dialog.setContentView(b.root)
        // ویرایش
        entity?.let {
            b.edtCode.setText(it.code)
            b.edtName.setText(it.name)
            b.edtFeature1.setText(it.feature1)
            b.edtFeature2.setText(it.feature2)
            b.edtFeature3.setText(it.feature3)
            b.edtFeature4.setText(it.feature4)
            b.edtPrice.setText(it.feature4)
        }
        lifecycleScope.launch {
            val features = productViewModel.getAllFeatures()

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                features
            )

            b.edtFeature1.setAdapter(adapter)
            b.edtFeature2.setAdapter(adapter)
            b.edtFeature3.setAdapter(adapter)
            b.edtFeature4.setAdapter(adapter)
        }


        b.btnSave.setOnClickListener {
            if (!validate(b)) return@setOnClickListener

            val newEntity = ProductEntity(
                id = entity?.id ?: 0,
                code = b.edtCode.text.toString(),
                name = b.edtName.text.toString(),
                feature1 = b.edtFeature1.text.toString(),
                feature2 = b.edtFeature2.text.toString(),
                feature3 = b.edtFeature3.text.toString(),
                feature4 = b.edtFeature4.text.toString(),
                price = b.edtPrice.text.toString().toDoubleOrNull() ?: 0.0
            )
            lifecycleScope.launch {
                productViewModel.saveFeatureHistory(
                    listOf(
                        b.edtFeature1.text.toString(),
                        b.edtFeature2.text.toString(),
                        b.edtFeature3.text.toString(),
                        b.edtFeature4.text.toString()
                    )
                )
            }

            if (entity == null)
                productViewModel.insert(newEntity)
            else
                productViewModel.update(newEntity)

            dialog.dismiss()
        }

        dialog.show()
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
        if (b.edtPrice.text.isNullOrBlank()) {
            b.edtFeature4.error = getString(R.string.error_enter_price)
            isValid = false
        }
        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}