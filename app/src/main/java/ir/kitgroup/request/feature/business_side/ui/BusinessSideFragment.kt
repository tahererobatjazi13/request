package ir.kitgroup.request.feature.business_side.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.request.R
import ir.kitgroup.request.core.database.entity.BusinessSideEntity
import ir.kitgroup.request.core.utils.CustomerRole
import ir.kitgroup.request.core.utils.PersonType
import ir.kitgroup.request.core.utils.SnackBarType
import ir.kitgroup.request.core.utils.component.CustomSnackBar
import ir.kitgroup.request.databinding.BottomSheetBusinessSideBinding
import ir.kitgroup.request.databinding.FragmentBusinessSideBinding
import ir.kitgroup.request.feature.business_side.ui.adapter.BusinessSideAdapter

@AndroidEntryPoint
class BusinessSideFragment : Fragment() {

    private var _binding: FragmentBusinessSideBinding? = null
    private val binding get() = _binding!!

    private lateinit var businessSideAdapter: BusinessSideAdapter
    private val businessSideViewModel: BusinessSideViewModel by viewModels()
    private var logoUri: String? = null
    private var currentSelectedType = CustomerRole.ORDER_RECEIVER

    private lateinit var allBusinessSide: List<BusinessSideEntity>
    private lateinit var filteredBusinessSideList: List<BusinessSideEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessSideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClicks()
        initTabButtons()
        initAdapter()
        initRecyclerViews()
        observeData()
    }

    private fun initTabButtons() {

        val btnOrderReceiver = binding.btnOrderReceiver
        val btnOrderGiver = binding.btnOrderGiver

        if (currentSelectedType == CustomerRole.ORDER_RECEIVER) {
            selectTab(btnOrderReceiver, btnOrderGiver, true)
        } else {
            selectTab(btnOrderReceiver, btnOrderGiver, false)
        }

        btnOrderReceiver.setOnClickListener {
            currentSelectedType = CustomerRole.ORDER_RECEIVER
            selectTab(btnOrderReceiver, btnOrderGiver, true)
            updateListForSelectedTab(currentSelectedType)
        }

        btnOrderGiver.setOnClickListener {
            currentSelectedType = CustomerRole.ORDER_GIVER
            selectTab(btnOrderReceiver, btnOrderGiver, false)
            updateListForSelectedTab(currentSelectedType)
        }
    }

    private fun selectTab(
        btnOrderReceiver: TextView,
        btnOrderGiver: TextView,
        isSelected: Boolean
    ) {
        val selectedBg = ContextCompat.getDrawable(requireContext(), R.drawable.bg_selected_tab)
        val unselectedBg = ContextCompat.getDrawable(requireContext(), R.drawable.bg_unselected_tab)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)

        if (isSelected) {
            btnOrderReceiver.background = selectedBg
            btnOrderReceiver.setTextColor(whiteColor)

            btnOrderGiver.background = unselectedBg
            btnOrderGiver.setTextColor(blackColor)
        } else {
            btnOrderGiver.background = selectedBg
            btnOrderGiver.setTextColor(whiteColor)

            btnOrderReceiver.background = unselectedBg
            btnOrderReceiver.setTextColor(blackColor)
        }
    }

    private fun setupClicks() {
        binding.apply {
            hfBusinessSide.setOnClickImgTwoListener {
                findNavController().navigateUp()
            }

            cvAddBusinessSide.setOnClickListener {
                showSheet(null)
            }
        }
    }

    private fun updateListForSelectedTab(selectedType: CustomerRole) {
        filteredBusinessSideList =
            allBusinessSide.filter { it.customerRole == selectedType }

        businessSideAdapter.submitList(filteredBusinessSideList)

        binding.info.visibility =
            if (filteredBusinessSideList.isEmpty()) View.VISIBLE else View.GONE
        binding.info.message(getString(R.string.msg_no_data))
    }

    private fun initAdapter() {
        allBusinessSide = listOf()
        filteredBusinessSideList = allBusinessSide

        businessSideAdapter = BusinessSideAdapter(
            onEdit = { showSheet(it) },
            onDelete = { businessSideViewModel.delete(it) }
        )
    }

    private fun initRecyclerViews() {
        binding.rvCustomer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = businessSideAdapter
        }
    }

    private fun observeData() {
        businessSideViewModel.businessSideList.observe(viewLifecycleOwner) { lists ->
            allBusinessSide = lists
            updateListForSelectedTab(currentSelectedType)
        }
    }

    private fun updateTabUIByRole(role: CustomerRole) {
        if (role == CustomerRole.ORDER_GIVER) {
            selectTab(binding.btnOrderReceiver, binding.btnOrderGiver, false)
        } else {
            selectTab(binding.btnOrderReceiver, binding.btnOrderGiver, true)
        }
    }

    private fun showSheet(entity: BusinessSideEntity?) {

        val dialog = BottomSheetDialog(requireContext())
        val b = BottomSheetBusinessSideBinding.inflate(layoutInflater)
        dialog.setContentView(b.root)

        entity?.let {
            b.edtCode.setText(it.code)
            b.edtName.setText(it.name)
            b.edtAddress.setText(it.address)
            b.edtPhone.setText(it.phone)
            b.edtMobile.setText(it.mobile)
            b.edtNationalCode.setText(it.nationalOrEconomicCode)

            if (it.personType == PersonType.REAL) b.rbReal.isChecked = true
            else b.rbLegal.isChecked = true

            if (it.customerRole == CustomerRole.ORDER_GIVER) b.rbOrderGiver.isChecked = true
            else b.rbOrderReceiver.isChecked = true

            logoUri = it.logoPath
        }

        b.imgLogo.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        b.btnSave.setOnClickListener {
            if (!validateBusinessSide(b)) return@setOnClickListener

            val personType =
                if (b.rbReal.isChecked) PersonType.REAL else PersonType.LEGAL

            val role =
                if (b.rbOrderGiver.isChecked)
                    CustomerRole.ORDER_GIVER
                else CustomerRole.ORDER_RECEIVER

            val newEntity = BusinessSideEntity(
                id = entity?.id ?: 0,
                code = b.edtCode.text.toString(),
                name = b.edtName.text.toString(),
                address = b.edtAddress.text.toString(),
                phone = b.edtPhone.text.toString(),
                mobile = b.edtMobile.text.toString(),
                logoPath = logoUri,
                nationalOrEconomicCode = b.edtNationalCode.text.toString(),
                personType = personType,
                customerRole = role
            )

            if (entity == null)
                businessSideViewModel.insert(newEntity)
            else
                businessSideViewModel.update(newEntity)

            currentSelectedType = role
            updateTabUIByRole(role)
            updateListForSelectedTab(role)
            dialog.dismiss()
        }
        dialog.show()
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            logoUri = it?.toString()
        }

    private fun validateBusinessSide(b: BottomSheetBusinessSideBinding): Boolean {

        var isValid = true

        if (b.edtCode.text.isNullOrBlank()) {
            b.edtCode.error = getString(R.string.error_enter_code)
            isValid = false
        }
        if (b.edtName.text.isNullOrBlank()) {
            b.edtName.error = getString(R.string.error_enter_name)
            isValid = false
        }
        if (b.edtMobile.text.isNullOrBlank()) {
            b.edtMobile.error = getString(R.string.error_enter_mobile)
            isValid = false
        }
        if (b.edtPhone.text.isNullOrBlank()) {
            b.edtPhone.error = getString(R.string.error_enter_phone)
            isValid = false
        }
        if (b.edtAddress.text.isNullOrBlank()) {
            b.edtPhone.error = getString(R.string.error_enter_address)
            isValid = false
        }
        if (b.edtNationalCode.text.isNullOrBlank()) {
            b.edtNationalCode.error = getString(R.string.error_enter_national_economic_cod)
            isValid = false
        }

        return isValid
    }


    private fun showBottomSheetError(message: String) {
        CustomSnackBar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            SnackBarType.Error.value
        )?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}