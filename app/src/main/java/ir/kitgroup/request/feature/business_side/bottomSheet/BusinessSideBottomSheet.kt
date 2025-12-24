/*
package ir.kitgroup.request.feature.business_side.bottomSheet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.request.databinding.BottomSheetBusinessSideBinding

@AndroidEntryPoint
@SuppressLint("UseCompatLoadingForDrawables")
class BusinessSideBottomSheet(
    private val onDismissCallback: (() -> Unit)? = null
) : BottomSheetDialogFragment() {
   // private val customerViewModel: CustomerViewModel by viewModels()

    private var _binding: BottomSheetBusinessSideBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBusinessSideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupClicks()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClicks() {
        binding.apply {
            binding.ivBack.setOnClickListener {
                dismiss()
            }

            */
/*      binding.btnSave.setOnClickListener {
              viewModel.insertCustomer(
                  code = binding.edtCode.text.toString(),
                  name = binding.edtName.text.toString(),
                  address = binding.edtAddress.text.toString(),
                  phone = binding.edtPhone.text.toString(),
                  logoPath = null,
                  nationalCode = binding.edtNationalCode.text.toString(),
                  personType = PersonType.REAL,
                  role = CustomerRole.ORDER_GIVER
              )*//*



          }




        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke()
    }
    companion object {
        const val REQ_CLICK_ITEM = "click_item_request"
        const val ARG_Code = "customerId"
        const val ARG_CUSTOMER_NAME = "customerName"
        fun newInstance(onDismiss: (() -> Unit)? = null) =
            BusinessSideBottomSheet(onDismiss)
    }
}
*/
