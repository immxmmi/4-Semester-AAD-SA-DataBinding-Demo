package at.technikum_wien.if20b231.databinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class DemoViewModel : ViewModel() {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    val outputText: LiveData<String>
        get() = Transformations.map(entriesInt) { l -> l.joinToString("\n") }
    val loading: LiveData<Boolean>
        get() = loadingInt

    fun reload() {
        loadEntries(entriesInt)
    }

    private fun randomString(length: Int) : String {
        return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun loadEntries(liveData : MutableLiveData<List<String>>) {
        loadingInt.value = true
        viewModelScope.launch {
            liveData.value = listOf()
            delay(2000)
            liveData.value = (0..99).map { randomString(10) }
            loadingInt.value = false
        }
    }

    private val entriesInt : MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>().also {
            loadEntries(it)
        }
    }

    private val loadingInt = MutableLiveData(true)
}