package com.example.controller;

import com.example.model.Priority;
import com.example.model.Status;
import com.example.model.Task;
import com.example.controller.request.CreateTaskRequest;
import com.example.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Получить задачи в зависимости от переданных параметров", description = "1) При отсутствии переданных параметров " +
            "сервер отдаст все существующие задачи 2) При указании любого количества из 3ех параметров фильтрации(" +
            "имя исполнителя задачи, статус задачи, приоритет задачи) сервер отдаст все задачи соответсвующие этим параметрам")
    public Page<Task> findTasks(@RequestParam(required = false)@Parameter(description = "Имя исполнителя") String executorName,
                                @RequestParam(required = false)@Parameter(description = "Статус задачи") Status status,
                                @RequestParam(required = false)@Parameter(description = "Приоритет задачи") Priority priority,
                                Pageable pageable) {
        return taskService.findTasks(executorName, status, priority, pageable);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Получить задачу по ID")
    public ResponseEntity<Task> findTaskById(@PathVariable @Parameter(description = "ID задачи") Long taskId) {
        return taskService.findTaskById(taskId);
    }

    @PostMapping
    @Operation(summary = "Создать задачу", description = "Обязательно указание всех полей шаблона задачи")
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest ctr) {
        return taskService.createTask(ctr);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Изменить существующую задачу")
    public ResponseEntity<Task> editTask(@PathVariable @Parameter(description = "ID задачи") Long taskId,
                                         @Valid @RequestBody CreateTaskRequest ctr) {
        return taskService.editTask(taskId, ctr);
    }

    @DeleteMapping ("/{taskId}")
    @Operation(summary = "Удалить существующую задачу.")
    public ResponseEntity<Void> deleteTask(@PathVariable @Parameter(description = "ID задачи") Long taskId) {
        return taskService.deleteTask(taskId);
    }
}
