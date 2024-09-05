package dev.wazapps.timetodo.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wazapps.timetodo.data.TodoDataStore
import dev.wazapps.timetodo.data.models.Priority
import dev.wazapps.timetodo.data.models.ToDoTask
import dev.wazapps.timetodo.data.repositories.TodoRepository
import dev.wazapps.timetodo.utils.Action
import dev.wazapps.timetodo.utils.Constants.MAX_TITLE_LENGTH
import dev.wazapps.timetodo.utils.states.RequestState
import dev.wazapps.timetodo.utils.states.SearchAppBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskSharedViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val todoDataStore: TodoDataStore
) : ViewModel() {

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    private var id by mutableIntStateOf(0)
    // TODO : maybe extract these values in a dedicated VM
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val priority = mutableStateOf(Priority.LOW)

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks = _allTasks.asStateFlow()

    private val _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks = _searchedTasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<ToDoTask?>(null)
    val selectedTask = _selectedTask.asStateFlow()

    val searchAppBarState = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState = mutableStateOf("")

    private val _sortState: MutableStateFlow<RequestState<Priority>> = MutableStateFlow(RequestState.Idle)
    val sortState = _sortState.asStateFlow()

    val tasksSortedByLowPriority: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val tasksSortedByHighPriority: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    /**
     * Used in conjunction with the search bar
     */
    fun searchDatabase(searchQuery : String){
        _searchedTasks.value = RequestState.Loading
        viewModelScope.launch {
            repository.searchDatabase(searchQuery = "%$searchQuery%")
                .flowOn(Dispatchers.IO)
                .catch { error ->
                    _searchedTasks.emit(RequestState.Error(error))
                }
                .collect { searchedTasks ->
                    _searchedTasks.emit(RequestState.Success(searchedTasks))
                }
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    fun persistSortingState(priority: Priority){
        viewModelScope.launch(Dispatchers.IO) {
            todoDataStore.persistSortState(priority)
        }
    }

    fun readSortingState(){
        _sortState.value = RequestState.Loading
        viewModelScope.launch {
            todoDataStore.readSortState
                .flowOn(Dispatchers.IO)
                .map { Priority.valueOf(it) }
                .catch { error -> _sortState.emit(RequestState.Error(error)) }
                .collect {
                    _sortState.emit(RequestState.Success(it))
                }
        }
    }

    fun getAllTasks(){
        _allTasks.value = RequestState.Loading
        viewModelScope.launch {
            repository.getAllTasks
                .flowOn(Dispatchers.IO)
                .catch { error ->
                    _allTasks.emit(RequestState.Error(error))
                }
                .collect { taskList ->
                    _allTasks.emit(RequestState.Success(taskList))
            }
        }
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId)
                .flowOn(Dispatchers.IO)
                .catch { error ->
                   Log.e("TaskSharedViewModel", "Error while retrieving the selected task : ${error.message}")
                }
                .collect { task ->
                    _selectedTask.update { task }
                }
        }
    }

    fun executeAction(action: Action) {
        when(action) {
            Action.ADD -> addTask()
            Action.UPDATE -> updateTask()
            Action.DELETE -> deleteTask()
            Action.DELETE_ALL -> deleteAllTask()
            Action.UNDO -> addTask()
            else -> {
                // Do nothing... For now
            }
        }
    }

    private fun deleteAllTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTaskToDelete = ToDoTask(
                id = id,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.deleteTask(toDoTaskToDelete)
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTaskToUpdate = ToDoTask(
                id,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.updateTask(toDoTaskToUpdate)
        }
    }



    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(
                ToDoTask(
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    //TODO : maybe extract this to a dedicated TaskVM
    fun updateTaskFields(selectedTask: ToDoTask?){
        if (selectedTask != null){
            id = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
            /*
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateTask(
                    selectedTask.copy(
                        id = id.value,
                        title = title.value,
                        description = description.value,
                        priority = priority.value
                    ))
            }*/
        } else {
            id = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }

    fun updateAction(newAction: Action){
        this.action = newAction
    }

    fun validateFields() : Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

}