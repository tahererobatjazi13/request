package ir.kitgroup.request.feature.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.kitgroup.request.feature.home.model.HomeMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.request.R
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    private val _homeMenuItem = MutableLiveData<List<HomeMenuItem>>()
    val homeMenuItems: LiveData<List<HomeMenuItem>> get() = _homeMenuItem

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {
        _homeMenuItem.value = listOf(
            HomeMenuItem(1, R.string.label_business_side, R.drawable.ic_launcher_foreground),
            HomeMenuItem(2, R.string.label_commodity, R.drawable.ic_launcher_foreground),
            HomeMenuItem(3, R.string.label_submit_request, R.drawable.ic_launcher_foreground),
        )
    }
}


