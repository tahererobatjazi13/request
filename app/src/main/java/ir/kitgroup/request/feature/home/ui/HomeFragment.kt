package ir.kitgroup.request.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ir.kitgroup.request.core.utils.extensions.getTodayPersianDate
import dagger.hilt.android.AndroidEntryPoint
import ir.kitgroup.request.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initAdapter()
    }

    private fun init() {
        binding.tvDate.text = getTodayPersianDate()
    }

    private fun initAdapter() {
        binding.rvHomeMenu.layoutManager = LinearLayoutManager(requireContext())
        viewModel.homeMenuItems.observe(viewLifecycleOwner) { items ->
            binding.rvHomeMenu.adapter = HomeMenuAdapter(items) { item ->
                when (item.id) {
                    1 -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToBusinessSideFragment()
                        findNavController().navigate(action)
                    }

                    2 -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToProductFragment()
                        findNavController().navigate(action)
                    }

                    3 -> {
                        /*val action =
                          HomeFragmentDirections.actionHomeFragmentToHeaderOrderFragment(
                               typeCustomer = false,
                               typeOrder = OrderType.Add.value,
                               customerId = 0,
                               customerName = ""
                           )
                       findNavController().navigate(action)*/
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}