package com.android.tne.ble.ui.old

import androidx.lifecycle.*
import com.android.tne.data.repository.TneRepository
import com.android.tne.domain.model.Bio
import kotlinx.coroutines.launch

class BioViewModel(private val repository: TneRepository) : ViewModel() {
/*
    val allDatas : LiveData<List<Bio>> = repository.allDatas.asLiveData()

    fun insert(bio : Bio) = viewModelScope.launch {
        repository.insert(bio)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }*/


//    private var bleStateLiveData : MutableLiveData<BLEDefine.BluetoothState>? = null
//        fun getBleStateLiveData() : MutableLiveData<BLEDefine.BluetoothState>?{
//            if(bleStateLiveData == null){
//                bleStateLiveData = MutableLiveData()
//            }
//            return bleStateLiveData
//        }
//
//    fun updateValue(bleState: BLEDefine.BluetoothState){
//        when(bleState){
//
//        }
//    }


}

/*
class BioViewModelFactory(private val repository: BioRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BioViewModel::class.java)){
            return BioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/
