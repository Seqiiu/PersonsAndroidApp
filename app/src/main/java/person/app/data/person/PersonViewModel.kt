package person.app.data.person


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import person.app.data.PersonRepository
import person.app.data.models.User
import javax.inject.Inject

@HiltViewModel
class DogsViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {

    val uiState: StateFlow<UiState> = personRepository
        .users
        .map<List<User>, UiState> { UiState.Success(data = it) }
        .catch { emit(UiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun addUser(user: User) {
        viewModelScope.launch {
            personRepository.add(user)
        }
    }

}

sealed interface UiState {
    object Loading: UiState
    data class Error(val throwable: Throwable): UiState
    data class Success(val data: List<User>): UiState
}