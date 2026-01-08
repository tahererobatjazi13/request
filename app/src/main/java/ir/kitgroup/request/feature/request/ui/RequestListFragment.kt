package ir.kitgroup.request.feature.request.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.request.R
import ir.kitgroup.request.core.database.entity.ProductEntity
import ir.kitgroup.request.core.database.entity.RequestHeaderEntity
import ir.kitgroup.request.core.utils.component.ConfirmDeleteDialog
import ir.kitgroup.request.core.utils.extensions.show
import ir.kitgroup.request.core.utils.extensions.gone
import ir.kitgroup.request.core.utils.extensions.hide
import ir.kitgroup.request.databinding.FragmentRequestListBinding
import ir.kitgroup.request.feature.product.ui.ProductFragmentDirections
import ir.kitgroup.request.feature.request.model.RequestHeaderDto
import ir.kitgroup.request.feature.request.ui.adapter.RequestHeaderAdapter

@AndroidEntryPoint
class RequestListFragment : Fragment() {

    private var _binding: FragmentRequestListBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestHeaderAdapter: RequestHeaderAdapter
    private val requestViewModel: RequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestListBinding.inflate(inflater, container, false)
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
            hfRequest.setOnClickImgTwoListener {
                findNavController().navigateUp()
            }

            cvAddRequest.setOnClickListener {
                val action =
                    RequestListFragmentDirections.actionRequestFragmentToRequestRegisterFragment(
                        requestId = 0
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun initAdapter() {

        requestHeaderAdapter = RequestHeaderAdapter(
            onEdit = { request ->
                Log.d("requestId3",request.id.toString())

                val action =
                    RequestListFragmentDirections.actionRequestFragmentToRequestRegisterFragment(
                        requestId = request.id.toInt()
                    )
                findNavController().navigate(action)
            },

            onDelete = { showConfirmDeleteDialog(it) },
        )

    }

    private fun showConfirmDeleteDialog(request: RequestHeaderDto) {
        ConfirmDeleteDialog {
            requestViewModel.delete(request.id)
        }.show(childFragmentManager, "ConfirmDeleteDialog")
    }

    private fun initRecyclerViews() {
        binding.rvRequest.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = requestHeaderAdapter
        }
    }

    private fun observeData() {
        requestViewModel.requestHeaders.observe(viewLifecycleOwner) { products ->
            if (products.isEmpty()) {
                binding.info.show()
                binding.info.message(requireContext().getString(R.string.msg_no_data))
                binding.rvRequest.hide()
            } else {
                binding.info.gone()
                binding.rvRequest.show()
                requestHeaderAdapter.setData(products)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}